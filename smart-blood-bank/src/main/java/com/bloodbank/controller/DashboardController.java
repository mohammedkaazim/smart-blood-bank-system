package com.bloodbank.controller;

import com.bloodbank.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("donors", dashboardService.getTotalDonors());
        stats.put("hospitals", dashboardService.getTotalHospitals());
        stats.put("requests", dashboardService.getTotalRequests());
        stats.put("units", dashboardService.getBloodStockSummary()
                .values().stream().mapToInt(Integer::intValue).sum());
        stats.put("stock", dashboardService.getBloodStockSummary());

        // mock monthly donations data (replace with real later)
        stats.put("donations", new int[]{5, 8, 12, 7, 14, 20, 18, 10});

        model.addAttribute("stats", stats);
        return "dashboard";
    }
}
