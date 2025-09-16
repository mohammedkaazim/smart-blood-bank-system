package com.bloodbank.controller;

import com.bloodbank.dto.HospitalDashboardStats;
import com.bloodbank.entity.Hospital;
import com.bloodbank.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalService service;

    public HospitalController(HospitalService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "name") String sort,
                       @RequestParam(defaultValue = "asc") String dir,
                       @RequestParam(required = false) String q,
                       Model model) {

        Page<Hospital> result = service.list(q, page, size, sort, dir);

        model.addAttribute("page", result);
        model.addAttribute("hospitals", result.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("q", q);
        return "hospital-list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    public String showAdd(Model model) {
        model.addAttribute("hospital", new Hospital());
        return "hospital-add";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    public String save(@Valid @ModelAttribute("hospital") Hospital hospital,
                       BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            System.out.println("Validation errors: " + binding.getAllErrors());
            return "redirect:/hospitals";
        }
        try {
            if (hospital.getId() == null) {
                service.create(hospital);
            } else {
                service.update(hospital.getId(), hospital);
            }
            return "redirect:/hospitals";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Something went wrong: " + e.getMessage());
            return "redirect:/hospitals";
        }
    }


    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("hospital", service.getById(id));
        return "hospital-edit";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/hospitals";
    }

    @PostMapping("/update/{id}")
    public String updateHospital(@PathVariable Long id,
                                 @ModelAttribute("hospital") Hospital hospital) {
        service.update(id, hospital);
        return "redirect:/hospitals"; // back to list
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        try {
            Hospital hospital = service.getById(id);
            model.addAttribute("hospital", hospital);

            model.addAttribute("donors", hospital.getDonors());

            return "hospital-view";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Hospital not found!");
            return "hospital-list";
        }
    }



    @GetMapping("/{id}/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    public String dashboard(@PathVariable Long id, Model model) {
        model.addAttribute("hospital", service.getById(id));
        HospitalDashboardStats stats = service.getDashboardStats(id);
        model.addAttribute("stats", stats);
        return "dashboard";
    }

}
