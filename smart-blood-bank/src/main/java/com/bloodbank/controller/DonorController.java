package com.bloodbank.controller;

import com.bloodbank.entity.Donor;
import com.bloodbank.service.DonorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/donors")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @GetMapping("/login")
    public String showDonorLoginPage() {
        return "donor-login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("donor", new Donor());
        return "donor-register";
    }

    @PostMapping("/register")
    public String register(@Validated(Donor.OnCreate.class) @ModelAttribute("donor") Donor donor,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "donor-register";
        }
        donorService.register(donor);
        return "redirect:/donors/login?success";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('DONOR')")
    public String viewProfile(Model model, Principal principal) {
        Donor donor = donorService.getByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        model.addAttribute("donor", donor);
        return "donor-profile";
    }

    @GetMapping("/profile/edit")
    @PreAuthorize("hasRole('DONOR')")
    public String editProfileForm(Model model, Principal principal) {
        Donor donor = donorService.getByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        model.addAttribute("donor", donor);
        return "donor-edit";
    }

    @PostMapping("/profile/edit")
    @PreAuthorize("hasRole('DONOR')")
    public String updateProfile(@Validated(Donor.OnUpdate.class) @ModelAttribute("donor") Donor donor,
                                BindingResult result,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "donor-edit";
        }

        Donor existing = donorService.getByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        donor.setId(existing.getId());
        donor.setEmail(existing.getEmail());
        donor.setPassword(existing.getPassword());
        donor.setRole(existing.getRole());
        donor.setVerified(existing.isVerified());
        donor.setEligible(existing.isEligible());

        donorService.update(donor.getId(), donor);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/donors/profile";
    }

    @PostMapping("/profile/delete")
    @PreAuthorize("hasRole('DONOR')")
    public String deleteOwnAccount(Principal principal) {
        donorService.getByEmail(principal.getName())
                .ifPresent(d -> donorService.delete(d.getId()));
        return "redirect:/donors/login?deleted";
    }

    // Admin-only mappings
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "fullName") String sort,
                       @RequestParam(defaultValue = "asc") String dir,
                       @RequestParam(required = false) String q,
                       @RequestParam(required = false) String city,
                       @RequestParam(required = false) String pincode,
                       @RequestParam(required = false) String bloodGroup,
                       @RequestParam(required = false) Boolean available,
                       Model model) {

        Page<Donor> donors;

        if (q != null && !q.isEmpty()) {
            donors = donorService.searchByKeyword(q, page, size, sort, dir);
        } else if (bloodGroup != null && !bloodGroup.isEmpty() && available != null) {
            donors = donorService.filterByBloodGroupAndAvailability(bloodGroup, available, page, size, sort, dir);
        } else if (bloodGroup != null && !bloodGroup.isEmpty()) {
            donors = donorService.filterByBloodGroup(bloodGroup, page, size, sort, dir);
        } else if (available != null) {
            donors = donorService.filterByAvailability(available, page, size, sort, dir);
        } else {
            donors = donorService.listAll(page, size, sort, dir);
        }

        model.addAttribute("donors", donors.getContent());
        model.addAttribute("page", donors);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

        // Keep filters in model so they stay selected
        model.addAttribute("q", q);
        model.addAttribute("city", city);
        model.addAttribute("pincode", pincode);
        model.addAttribute("bloodGroup", bloodGroup);
        model.addAttribute("available", available);

        return "donor-list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    public String view(@PathVariable Long id, Model model) {
        Donor donor = donorService.getById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        model.addAttribute("donor", donor);
        return "donor-view";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDonor(@PathVariable Long id) {
        donorService.delete(id);
        return "redirect:/donors?deleted";
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminViewDonor(@PathVariable Long id, Model model) {
        Donor donor = donorService.getById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        model.addAttribute("donor", donor);
        return "donor-view";
    }
}