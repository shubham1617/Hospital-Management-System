package com.pm.billingservice.service;

import com.pm.billingservice.BillingServiceApplication;
import com.pm.billingservice.dtos.BillingRequestDTO;
import com.pm.billingservice.dtos.BillingResponseDTO;
import com.pm.billingservice.dtos.PatientResponse;
import com.pm.billingservice.mapper.BillingMapper;
import com.pm.billingservice.model.Billing;
import com.pm.billingservice.repository.BillingRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BillingService
{
    private final Logger logger = getLogger(BillingServiceApplication.class);
    private final BillingRepository billingRepository;
    private final WebClient webClient;

    public BillingService(BillingRepository billingRepository, WebClient webClient)
    {
        this.billingRepository = billingRepository;
        this.webClient = webClient;
    }

    public List<BillingResponseDTO> getAllBilling()
    {
        List<Billing> allBillings = billingRepository.findAll();
        return allBillings.stream().map(BillingMapper::entityToResponse).toList();
    }

    public BillingResponseDTO getBillingById(String id)
    {
        Billing billingById = billingRepository.findById(id).orElseThrow(() -> new RuntimeException("Billing id not found"));
        return BillingMapper.entityToResponse(billingById);
    }

    public List<BillingResponseDTO> getBillingByPatientId(String patientId)
    {
        List<Billing> patientById = billingRepository.findByPatientId(patientId);
        if(patientById.isEmpty())
        {
            throw new RuntimeException("Patient id not found!!");
        }
        return patientById.stream().map(BillingMapper::entityToResponse).toList();

    }

    @CircuitBreaker(name = "patientBillingService", fallbackMethod = "handlePatientServiceDown")
    public BillingResponseDTO createBilling(BillingRequestDTO billingRequestDTO) {

        // Clean, reactive call to patient service to verify patient existence before creating billing record
        webClient.get()
                .uri("http://localhost:8081/api/v1/patient/" + billingRequestDTO.getPatientId())
                .retrieve()
                .bodyToMono(PatientResponse.class)
                .timeout(Duration.ofSeconds(5)) // Reduced to a realistic production timeout
                .block();

        // Create and save billing record if the above call succeeds
        Billing billingDetails = Billing.builder()
                .id(UUID.randomUUID().toString())
                .amount(billingRequestDTO.getAmount())
                .status(billingRequestDTO.getStatus())
                .description(billingRequestDTO.getDescription())
                .patientId(billingRequestDTO.getPatientId())
                .billingDate(LocalDateTime.now())
                .build();

        Billing saveDetail = billingRepository.save(billingDetails);
        return BillingMapper.entityToResponse(saveDetail);
    }

    // Fallback method
    public BillingResponseDTO handlePatientServiceDown(BillingRequestDTO billingRequestDTO, Exception e) throws Exception {
        // If the exception is a 404 NotFound, rethrow it directly!
        if (e instanceof org.springframework.web.reactive.function.client.WebClientResponseException.NotFound) {
            throw e;
        }

        logger.error("Circuit breaker fallback triggered. Error: {}", e.getMessage());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Patient service is currently unavailable. Please try again later.");
    }


    public  BillingResponseDTO updateBilling(BillingRequestDTO billingRequestDTO, String id)
    {
        Billing billing = billingRepository.findById(id).orElseThrow(() -> new RuntimeException("Billing Id not found: " + id));

        if(billing.getAmount() != null && !billing.getAmount().equals(billingRequestDTO.getAmount()))
        {
            billing.setAmount(billingRequestDTO.getAmount());
        }
        if(billing.getStatus() != null && !billing.getStatus().equals(billingRequestDTO.getStatus()))
        {
            billing.setStatus(billingRequestDTO.getStatus());
        }
        if (billing.getBillingDate() != null )
        {
            billing.setBillingDate(billing.getBillingDate());
        }
        if (billing.getDescription() != null && !billing.getDescription().equals(billingRequestDTO.getDescription()))
        {
            billing.setDescription(billingRequestDTO.getDescription());
        }
        if(billing.getPatientId() != null && !billing.getPatientId().equals(billingRequestDTO.getPatientId()))
        {
            throw new RuntimeException("You cannot change your patient !");
        }

        billingRepository.save(billing);
        return BillingMapper.entityToResponse(billing);
    }

    public void  deleteBillingById(String id)
    {
        Billing billing = billingRepository.findById(id).orElseThrow(() -> new RuntimeException("Billing Id not found: " + id));
        billingRepository.deleteById(id);
    }


}
