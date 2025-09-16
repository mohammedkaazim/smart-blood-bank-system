package com.bloodbank.repository;

import com.bloodbank.entity.HosDonor;
import com.bloodbank.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HosDonorRepository extends JpaRepository<HosDonor, Long> {

    Page<HosDonor> findByHospital(Hospital hospital, Pageable pageable);

    Page<HosDonor> findByHospitalAndFullNameContainingIgnoreCase(Hospital hospital, String name, Pageable pageable);

    Page<HosDonor> findByHospitalAndBloodGroup(Hospital hospital, String bloodGroup, Pageable pageable);

    Page<HosDonor> findByHospitalAndFullNameContainingIgnoreCaseOrHospitalAndCityContainingIgnoreCaseOrHospitalAndPincodeContaining(
            Hospital hospital, String name, Hospital hospital2, String city, Hospital hospital3, String pincode, Pageable pageable);

    Page<HosDonor> findByHospitalAndBloodGroupAndFullNameContainingIgnoreCaseOrHospitalAndBloodGroupAndCityContainingIgnoreCaseOrHospitalAndBloodGroupAndPincodeContaining(
            Hospital hospital, String bloodGroup, String name, Hospital hospital2, String bloodGroup2, String city,
            Hospital hospital3, String bloodGroup3, String pincode, Pageable pageable);
}