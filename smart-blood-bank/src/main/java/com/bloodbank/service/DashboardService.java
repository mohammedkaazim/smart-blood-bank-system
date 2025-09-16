package com.bloodbank.service;

import com.bloodbank.entity.BloodRequest;
import java.util.List;
import java.util.Map;

public interface DashboardService {
    long getTotalDonors();
    long getTotalHospitals();
    long getTotalRequests();
    Map<String, Long> getRequestStats();
    Map<String, Integer> getBloodStockSummary();
    List<BloodRequest> getRecentRequests();
}
