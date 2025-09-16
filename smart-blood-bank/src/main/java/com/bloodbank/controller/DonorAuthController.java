package com.bloodbank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/donors/auth")
public class DonorAuthController {

    @GetMapping("/login")
    public String donorLogin() {
        return "donor-login";
    }

    @GetMapping("/dashboard")
    public String donorDashboard() {
        return "donor-dashboard";
    }
}