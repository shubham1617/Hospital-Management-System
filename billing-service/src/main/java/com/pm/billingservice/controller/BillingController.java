package com.pm.billingservice.controller;

import com.pm.billingservice.dtos.BillingRequestDTO;
import com.pm.billingservice.dtos.BillingResponseDTO;
import com.pm.billingservice.dtos.validations.CreateBillingValidator;
import com.pm.billingservice.dtos.validations.UpdateBillingValidator;
import com.pm.billingservice.service.BillingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
public class BillingController
{
    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/")
    public List<BillingResponseDTO> getBilling()
    {
        return billingService.getAllBilling();
    }

    @GetMapping("/{id}")
    public BillingResponseDTO getBillingById(@PathVariable String id)
    {
        return billingService.getBillingById(id);
    }

    @GetMapping("/patient/{id}")
    public List<BillingResponseDTO> getBillingByPatientId(@PathVariable String id)
    {
        return billingService.getBillingByPatientId(id);
    }

    @PostMapping("/add")
    public BillingResponseDTO addBilling(@Validated({CreateBillingValidator.class}) @RequestBody BillingRequestDTO billingRequestDTO)
    {
        return  billingService.createBilling(billingRequestDTO);
    }

    @PutMapping("/update/{id}")
    public BillingResponseDTO updateBilling(@Validated({UpdateBillingValidator.class}) @RequestBody BillingRequestDTO billingRequestDTO,@PathVariable String id)
    {
        return  billingService.updateBilling(billingRequestDTO,id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>  deleteBilling(@PathVariable String id)
    {
        billingService.deleteBillingById(id);
        return new ResponseEntity<>("Bill Deleted",HttpStatus.OK);
    }


}
