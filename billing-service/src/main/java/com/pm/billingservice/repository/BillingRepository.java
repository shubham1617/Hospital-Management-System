package com.pm.billingservice.repository;

import com.pm.billingservice.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, String>
{
    List<Billing> findByPatientId(String patientId);
}
