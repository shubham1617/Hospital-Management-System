package com.pm.billingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingResponseDTO
{
    private String id;
    private String patientId;
    private Double amount;
    private String status; // PENDING, PAID, FAILED
    private LocalDateTime billingDate;
    private String description;
}
