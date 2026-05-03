package com.pm.billingservice.mapper;

import com.pm.billingservice.dtos.BillingResponseDTO;
import com.pm.billingservice.model.Billing;

public class BillingMapper
{
    public static BillingResponseDTO entityToResponse(Billing  billing)
    {
        BillingResponseDTO billingResponseDTO = new BillingResponseDTO();
        billingResponseDTO.setId(billing.getId());
        billingResponseDTO.setAmount(billing.getAmount());
        billingResponseDTO.setDescription(billing.getDescription());
        billingResponseDTO.setBillingDate(billing.getBillingDate());
        billingResponseDTO.setPatientId(billing.getPatientId());
        billingResponseDTO.setStatus(billing.getStatus());
        return billingResponseDTO;
    }

    public static Billing responseToEntity(BillingResponseDTO billingResponseDTO)
    {
        Billing billing = new Billing();
        billing.setId(billingResponseDTO.getId());
        billing.setAmount(billingResponseDTO.getAmount());
        billing.setDescription(billingResponseDTO.getDescription());
        billing.setBillingDate(billingResponseDTO.getBillingDate());
        billing.setPatientId(billingResponseDTO.getPatientId());
        billing.setStatus(billingResponseDTO.getStatus());
        return billing;
    }


}
