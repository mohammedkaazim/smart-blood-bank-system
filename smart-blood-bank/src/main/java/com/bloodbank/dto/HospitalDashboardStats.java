package com.bloodbank.dto;

import java.util.Map;

public class HospitalDashboardStats {
    private Long totalDonors;
    private Long pendingRequests;
    private Map<String, Integer> stockByGroup;
    private int lowStockGroups;

    public HospitalDashboardStats(long totalDonors, long pendingRequests,
                                  Map<String, Integer> stockByGroup, int lowStockGroups) {
        this.totalDonors = totalDonors;
        this.pendingRequests = pendingRequests;
        this.stockByGroup = stockByGroup;
        this.lowStockGroups = lowStockGroups;
    }

    // getters
    public Long getTotalDonors() { return totalDonors; }
    public long getPendingRequests() { return pendingRequests; }
    public Map<String, Integer> getStockByGroup() { return stockByGroup; }
    public int getLowStockGroups() { return lowStockGroups; }
}
