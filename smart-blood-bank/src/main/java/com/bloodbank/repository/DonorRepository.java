package com.bloodbank.repository;

import com.bloodbank.entity.Donor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonorRepository extends JpaRepository<Donor, Long> {

    Optional<Donor> findByEmail(String email);

    Page<Donor> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
    Page<Donor> findByCityContainingIgnoreCase(String city, Pageable pageable);
    Page<Donor> findByPincodeContainingIgnoreCase(String pincode, Pageable pageable);

    Page<Donor> findByBloodGroup(String bloodGroup, Pageable pageable);
    Page<Donor> findByBloodGroupAndAvailable(String bloodGroup, boolean available, Pageable pageable);

    Page<Donor> findByAvailable(boolean available, Pageable pageable);

    Page<Donor> findByFullNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrPincodeContainingIgnoreCase(
            String fullName, String city, String pincode, Pageable pageable);

}
