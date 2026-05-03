package com.pm.billingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Billing
{
    @Id
    @Column(length = 36)
    private String id;

    @Column(length = 36, nullable = false)
    private String patientId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String status; // PENDING, PAID, FAILED

    @Column(nullable = false)
    private LocalDateTime billingDate;

    private String description;
}
