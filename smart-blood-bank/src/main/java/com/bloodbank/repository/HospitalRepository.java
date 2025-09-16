package com.bloodbank.repository;

import com.bloodbank.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Page<Hospital> findByNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrPincodeContainingIgnoreCase (
            String name, String city, String pincode, Pageable pageable);

    boolean existsByEmail(String email);
}
