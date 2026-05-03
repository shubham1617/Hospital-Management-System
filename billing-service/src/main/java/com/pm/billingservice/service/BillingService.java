package com.pm.billingservice.service;

import com.pm.billingservice.dtos.BillingRequestDTO;
import com.pm.billingservice.dtos.BillingResponseDTO;
import com.pm.billingservice.dtos.PatientResponse;
import com.pm.billingservice.mapper.BillingMapper;
import com.pm.billingservice.model.Billing;
import com.pm.billingservice.repository.BillingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BillingService
{
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

    public BillingResponseDTO createBilling(BillingRequestDTO billingRequestDTO)
    {
        try {
            webClient.get()
                    .uri("http://localhost:8081/api/v1/patient/" + billingRequestDTO.getPatientId())
                    .retrieve()
                    .bodyToMono(PatientResponse.class)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("Patient not found for Id: " + billingRequestDTO.getPatientId());
        }

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
