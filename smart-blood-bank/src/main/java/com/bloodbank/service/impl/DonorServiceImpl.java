package com.bloodbank.service.impl;

import com.bloodbank.entity.Donor;
import com.bloodbank.repository.DonorRepository;
import com.bloodbank.service.DonorService;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.time.Period;

@Service
public class DonorServiceImpl implements DonorService {

    private final DonorRepository donorRepository;
    private final PasswordEncoder passwordEncoder;

    public DonorServiceImpl(DonorRepository donorRepository, PasswordEncoder passwordEncoder) {
        this.donorRepository = donorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Donor register(Donor donor) {
        donor.setPassword(passwordEncoder.encode(donor.getPassword()));

        if (donor.getRole() == null || donor.getRole().isBlank()) {
            donor.setRole("DONOR");
        }

        if (donor.getDob() != null) {
            int years = Period.between(donor.getDob(), LocalDate.now()).getYears();
            donor.setAge(years);
            donor.setEligible(years >= 18 && years <= 65);
        }
        
        return donorRepository.save(donor);
    }

    @Override
    public Donor update(Long id, Donor donor) {
        Donor existing = donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        existing.setFullName(donor.getFullName());
        existing.setAge(donor.getAge());
        existing.setDob(donor.getDob());  // 👈 Save Date of Birth
        existing.setGender(donor.getGender());
        existing.setContactNumber(donor.getContactNumber());
        existing.setEmail(donor.getEmail());
        existing.setCity(donor.getCity());
        existing.setState(donor.getState());
        existing.setPincode(donor.getPincode());
        existing.setBloodGroup(donor.getBloodGroup());
        existing.setLastDonationDate(donor.getLastDonationDate());
        existing.setHasChronicDiseases(donor.isHasChronicDiseases());
        existing.setHasRecentSurgery(donor.isHasRecentSurgery());
        existing.setHasAllergies(donor.isHasAllergies());
        existing.setOtherMedicalConditions(donor.getOtherMedicalConditions());
        existing.setAvailable(donor.isAvailable());

        // Eligibility based on donation date
        if (donor.getLastDonationDate() != null) {
            long months = ChronoUnit.MONTHS.between(donor.getLastDonationDate(), LocalDate.now());
            existing.setEligible(months >= 3);
        } else {
            existing.setEligible(true);
        }

        return donorRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        donorRepository.deleteById(id);
    }

    @Override
    public Optional<Donor> getById(Long id) {
        return donorRepository.findById(id);
    }

    @Override
    public Optional<Donor> getByEmail(String email) {
        return donorRepository.findByEmail(email);
    }

    @Override
    public Page<Donor> listAll(int page, int size, String sort, String dir) {
        Sort.Direction direction = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return donorRepository.findAll(pageable);
    }

    @Override
    public Page<Donor> searchByName(String name, int page, int size, String sort, String dir ) {
        Pageable pageable = PageRequest.of(page, size);
        return donorRepository.findByFullNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<Donor> searchByCity(String city, int page, int size, String sort, String dir) {
        Pageable pageable = PageRequest.of(page, size);
        return donorRepository.findByCityContainingIgnoreCase(city, pageable);
    }

    @Override
    public Page<Donor> searchByPincode(String pincode, int page, int size, String sort, String dir) {
        Pageable pageable = PageRequest.of(page, size);
        return donorRepository.findByPincodeContainingIgnoreCase(pincode, pageable);
    }

    @Override
    public Page<Donor> filterByBloodGroup(String bloodGroup, int page, int size, String sort, String dir) {
        Pageable pageable = PageRequest.of(page, size);
        return donorRepository.findByBloodGroup(bloodGroup, pageable);
    }

    @Override
    public Page<Donor> filterByBloodGroupAndAvailability(String bloodGroup, boolean available, int page, int size, String sort, String dir) {
        Pageable pageable = PageRequest.of(page, size);
        return donorRepository.findByBloodGroupAndAvailable(bloodGroup, available, pageable);
    }

    @Override
    public Page<Donor> filterByAvailability(boolean available, int page, int size, String sort, String dir) {
        Pageable pageable = PageRequest.of(page, size);
        return donorRepository.findByAvailable(available, pageable);
    }

    @Override
    public Page<Donor> searchByKeyword(String keyword, int page, int size, String sort, String dir) {
        Sort.Direction direction = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return donorRepository
                .findByFullNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrPincodeContainingIgnoreCase(
                        keyword, keyword, keyword, pageable);
    }
}