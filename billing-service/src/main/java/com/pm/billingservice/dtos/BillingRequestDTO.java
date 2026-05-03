package com.pm.billingservice.dtos;

import com.pm.billingservice.dtos.validations.CreateBillingValidator;
import com.pm.billingservice.dtos.validations.UpdateBillingValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingRequestDTO
{
    @NotBlank(message = "Cannot  be blank", groups = {CreateBillingValidator.class})
    private String patientId;
    @NotNull(message = "Cannot  be blank", groups = {CreateBillingValidator.class, UpdateBillingValidator.class})
    private Double amount;
    @NotBlank(message = "Cannot  be blank", groups = {CreateBillingValidator.class, UpdateBillingValidator.class})
    private String status;
    @NotBlank(message = "Cannot  be blank", groups = {CreateBillingValidator.class,UpdateBillingValidator.class})// PENDING, PAID, FAILED
    private String description;
}
