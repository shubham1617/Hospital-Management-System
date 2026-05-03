package com.pm.doctorservice.repository;

import com.pm.doctorservice.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {

    /**
     * Find doctor by email
     */
    Optional<Doctor> findByDoctorEmail(String doctorEmail);

    /**
     * Find doctor by phone
     */
    Optional<Doctor> findByDoctorPhone(String doctorPhone);

    /**
     * Find doctors by specialization
     */
    List<Doctor> findBySpecialization(String specialization);

    /**
     * Find active doctors by specialization
     */
    List<Doctor> findBySpecializationAndIsActiveTrue(String specialization);

    /**
     * Check if doctor exists by email
     */
    boolean existsByDoctorEmail(String doctorEmail);

    /**
     * Check if doctor exists by phone
     */
    boolean existsByDoctorPhone(String doctorPhone);

    /**
     * Find all active doctors
     */
    List<Doctor> findByIsActiveTrue();

    /**
     * Find all inactive doctors
     */
    List<Doctor> findByIsActiveFalse();

    /**
     * Find doctors with pagination
     */
    Page<Doctor> findAll(Pageable pageable);

    /**
     * Find active doctors with pagination
     */
    Page<Doctor> findByIsActiveTrue(Pageable pageable);

    /**
     * Custom query to find doctors by specialization with pagination
     */
    @Query("SELECT d FROM Doctor d WHERE d.specialization = :specialization AND d.isActive = true")
    Page<Doctor> findActiveBySpecialization(@Param("specialization") String specialization, Pageable pageable);

    /**
     * Custom query to search doctors by name or email
     */
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.doctorName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(d.doctorEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "AND d.isActive = true")
    Page<Doctor> searchDoctors(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Count active doctors
     */
    long countByIsActiveTrue();

    /**
     * Count doctors by specialization
     */
    long countBySpecialization(String specialization);
}
