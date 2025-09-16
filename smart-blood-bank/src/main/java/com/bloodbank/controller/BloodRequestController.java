package com.bloodbank.controller;

import com.bloodbank.entity.BloodRequest;
import com.bloodbank.service.BloodRequestService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/requests")
public class BloodRequestController {

    private final BloodRequestService service;

    public BloodRequestController(BloodRequestService service) {
        this.service = service;
    }

    @GetMapping
    public String listRequests(Model model, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("requests", service.getAll());
        } else {
            model.addAttribute("requests", service.getByRequester(auth.getName()));
        }
        return "request-list";
    }

    @GetMapping("/new")
    public String newRequestForm(Model model, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("ROLE_DONOR") || a.getAuthority().equals("ROLE_HOSPITAL"))) {
            model.addAttribute("request", new BloodRequest());
            return "request-form";
        }
        return "redirect:/requests";
    }

    @PostMapping
    public String saveRequest(@ModelAttribute BloodRequest request, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("ROLE_DONOR") || a.getAuthority().equals("ROLE_HOSPITAL"))) {
            request.setRequesterName(auth.getName());
            service.save(request);
        }
        return "redirect:/requests";
    }

    @GetMapping("/approve/{id}")
    public String approveRequest(@PathVariable Long id, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            service.updateStatus(id, BloodRequest.Status.APPROVED);
        }
        return "redirect:/requests";
    }

    @GetMapping("/reject/{id}")
    public String rejectRequest(@PathVariable Long id, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            service.updateStatus(id, BloodRequest.Status.REJECTED);
        }
        return "redirect:/requests";
    }
}
