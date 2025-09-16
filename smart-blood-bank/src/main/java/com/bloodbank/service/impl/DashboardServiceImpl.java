package com.bloodbank.service.impl;

import com.bloodbank.entity.BloodRequest;
import com.bloodbank.repository.BloodRequestRepository;
import com.bloodbank.repository.BloodStockRepository;
import com.bloodbank.repository.DonorRepository;
import com.bloodbank.repository.HospitalRepository;
import com.bloodbank.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DonorRepository donorRepo;
    private final HospitalRepository hospitalRepo;
    private final BloodRequestRepository requestRepo;
    private final BloodStockRepository stockRepo;

    public DashboardServiceImpl(DonorRepository donorRepo,
                                HospitalRepository hospitalRepo,
                                BloodRequestRepository requestRepo,
                                BloodStockRepository stockRepo) {
        this.donorRepo = donorRepo;
        this.hospitalRepo = hospitalRepo;
        this.requestRepo = requestRepo;
        this.stockRepo = stockRepo;
    }

    @Override
    public long getTotalDonors() {
        return donorRepo.count();
    }

    @Override
    public long getTotalHospitals() {
        return hospitalRepo.count();
    }

    @Override
    public long getTotalRequests() {
        return requestRepo.count();
    }

    @Override
    public Map<String, Long> getRequestStats() {
        return Arrays.stream(BloodRequest.Status.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        status -> requestRepo.countByStatus(status)
                ));
    }

    @Override
    public Map<String, Integer> getBloodStockSummary() {
        return stockRepo.findAll()
                .stream()
                .collect(Collectors.toMap(
                        stock -> stock.getBloodGroup(),
                        stock -> stock.getUnitsAvailable()
                ));
    }

    @Override
    public List<BloodRequest> getRecentRequests() {
        return requestRepo.findTop5ByOrderByCreatedAtDesc();
    }
}
