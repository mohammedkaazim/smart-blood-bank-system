package com.bloodbank.service.impl;

import com.bloodbank.entity.HosDonor;
import com.bloodbank.entity.Hospital;
import com.bloodbank.repository.HosDonorRepository;
import com.bloodbank.service.HosDonorService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class HosDonorServiceImpl implements HosDonorService {

    private final HosDonorRepository repo;

    public HosDonorServiceImpl(HosDonorRepository repo) {
        this.repo = repo;
    }

    @Override
    public HosDonor create(HosDonor donor) {
        return repo.save(donor);
    }

    @Override
    public HosDonor update(Long id, HosDonor incoming) {
        HosDonor current = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Donor not found: " + id));

        current.setFullName(incoming.getFullName());
        current.setAge(incoming.getAge());
        current.setGender(incoming.getGender());
        current.setContactNumber(incoming.getContactNumber());
        current.setEmail(incoming.getEmail());
        current.setAddress(incoming.getAddress());
        current.setCity(incoming.getCity());
        current.setState(incoming.getState());
        current.setPincode(incoming.getPincode());
        current.setBloodGroup(incoming.getBloodGroup());
        current.setLastDonationDate(incoming.getLastDonationDate());
        current.setHasChronicDiseases(incoming.isHasChronicDiseases());
        current.setHasRecentSurgery(incoming.isHasRecentSurgery());
        current.setHasAllergies(incoming.isHasAllergies());
        current.setOtherMedicalConditions(incoming.getOtherMedicalConditions());
        current.setEligible(incoming.isEligible());

        return repo.save(current);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public HosDonor getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Donor not found"));
    }

    @Override
    public Page<HosDonor> listByHospital(Hospital hospital, int page, int size, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField == null ? "fullName" : sortField);
        sort = "desc".equalsIgnoreCase(sortDir) ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findByHospital(hospital, pageable);
    }

    @Override
    public Page<HosDonor> searchByName(Hospital hospital, String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByHospitalAndFullNameContainingIgnoreCase(hospital, q, pageable);
    }

    @Override
    public Page<HosDonor> filterByBloodGroup(Hospital hospital, String bloodGroup, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByHospitalAndBloodGroup(hospital, bloodGroup, pageable);
    }

    @Override
    public Page<HosDonor> searchByNameCityPincode(Hospital hospital, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByHospitalAndFullNameContainingIgnoreCaseOrHospitalAndCityContainingIgnoreCaseOrHospitalAndPincodeContaining(
                hospital, keyword, hospital, keyword, hospital, keyword, pageable);
    }

    @Override
    public Page<HosDonor> searchByNameCityPincodeAndBlood(Hospital hospital, String keyword, String bloodGroup, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByHospitalAndBloodGroupAndFullNameContainingIgnoreCaseOrHospitalAndBloodGroupAndCityContainingIgnoreCaseOrHospitalAndBloodGroupAndPincodeContaining(
                hospital, bloodGroup, keyword, hospital, bloodGroup, keyword, hospital, bloodGroup, keyword, pageable);
    }
}
