package com.bloodbank.service.impl;

import com.bloodbank.dto.HospitalDashboardStats;
import com.bloodbank.entity.Hospital;
import com.bloodbank.repository.HospitalRepository;
import com.bloodbank.service.HospitalService;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository repo;

    public HospitalServiceImpl(HospitalRepository repo) {
        this.repo = repo;
    }

    @Override
    public Hospital create(@Valid Hospital hospital) {
        if (hospital.getEmail() != null && repo.existsByEmail(hospital.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + hospital.getEmail());
        }
        return repo.save(hospital);
    }

    @Override
    public Hospital update(Long id, @Valid Hospital incoming) {
        Hospital current = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found: " + id));

        if (incoming.getEmail() != null
                && !incoming.getEmail().equalsIgnoreCase(current.getEmail())
                && repo.existsByEmail(incoming.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + incoming.getEmail());
        }

        current.setName(incoming.getName());
        current.setEmail(incoming.getEmail());
        current.setAddress(incoming.getAddress());
        current.setCity(incoming.getCity());
        current.setState(incoming.getState());
        current.setPincode(incoming.getPincode());
        current.setContactNumber(incoming.getContactNumber());

        return repo.save(current);
    }


    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Hospital getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Page<Hospital> list(String q, int page, int size, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField == null || sortField.isBlank() ? "name" : sortField);
        sort = "desc".equalsIgnoreCase(sortDir) ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);

        if (StringUtils.hasText(q)) {
            return repo.findByNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrPincodeContainingIgnoreCase(q, q, q, pageable);
        }
        return repo.findAll(pageable);
    }


    @Override
    public HospitalDashboardStats getDashboardStats(Long hospitalId) {
        // TODO: replace mock with real queries once Donor/Stock/Request modules are ready
        Map<String, Integer> stock = new LinkedHashMap<>();
        stock.put("A+", 0);
        stock.put("A-", 0);
        stock.put("B+", 0);
        stock.put("B-", 0);
        stock.put("AB+", 0);
        stock.put("AB-", 0);
        stock.put("O+", 0);
        stock.put("O-", 0);

        long donors = 0L;
        long pending = 0L;
        int lowGroups = 0;

        return new HospitalDashboardStats(donors, pending, stock, lowGroups);
    }
}