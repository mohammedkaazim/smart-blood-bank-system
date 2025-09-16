package com.bloodbank.controller;

import com.bloodbank.entity.HosDonor;
import com.bloodbank.entity.Hospital;
import com.bloodbank.service.HosDonorService;
import com.bloodbank.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hospitals/{hospitalId}/donors")
public class HosDonorController {

    private final HosDonorService donorService;
    private final HospitalService hospitalService;

    public HosDonorController(HosDonorService donorService, HospitalService hospitalService) {
        this.donorService = donorService;
        this.hospitalService = hospitalService;
    }


    @GetMapping
    public String list(@PathVariable Long hospitalId,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "fullName") String sort,
                       @RequestParam(defaultValue = "asc") String dir,
                       @RequestParam(required = false) String q,
                       @RequestParam(required = false) String bloodGroup,
                       Model model) {

        Hospital hospital = hospitalService.getById(hospitalId);
        Page<HosDonor> donors;

        if (q != null && !q.isEmpty() && bloodGroup != null && !bloodGroup.isEmpty()) {
            donors = donorService.searchByNameCityPincodeAndBlood(hospital, q, bloodGroup, page, size);
        } else if (q != null && !q.isEmpty()) {
            donors = donorService.searchByNameCityPincode(hospital, q, page, size);
        } else if (bloodGroup != null && !bloodGroup.isEmpty()) {
            donors = donorService.filterByBloodGroup(hospital, bloodGroup, page, size);
        } else {
            donors = donorService.listByHospital(hospital, page, size, sort, dir);
        }

        model.addAttribute("hospital", hospital);
        model.addAttribute("donors", donors.getContent());
        model.addAttribute("page", donors);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("q", q);
        model.addAttribute("bloodGroup", bloodGroup);

        return "hospital-donor-list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    public String showAddForm(@PathVariable Long hospitalId, Model model) {
        model.addAttribute("donor", new HosDonor());
        model.addAttribute("hospitalId", hospitalId);
        return "hospital-donor-add";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    public String save(@PathVariable Long hospitalId,
                       @Valid @ModelAttribute("donor") HosDonor donor,
                       BindingResult binding,
                       Model model) {

        Hospital hospital = hospitalService.getById(hospitalId);
        if (hospital == null) {
            throw new IllegalArgumentException("Hospital not found for ID " + hospitalId);
        }

        if (binding.hasErrors()) {
            model.addAttribute("hospitalId", hospitalId);

            return (donor.getId() == null) ? "hospital-donor-add" : "hospital-donor-edit";
        }

        donor.setHospital(hospital);

        if (donor.getId() == null) {
            donorService.create(donor);
        } else {
            donorService.update(donor.getId(), donor);
        }

        return "redirect:/hospitals/" + hospitalId + "/donors";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    public String edit(@PathVariable Long hospitalId, @PathVariable Long id, Model model) {
        HosDonor donor = donorService.getById(id);
        model.addAttribute("donor", donor);
        model.addAttribute("hospitalId", hospitalId);
        return "hospital-donor-edit";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    public String update(@PathVariable Long hospitalId,
                         @PathVariable Long id,
                         @Valid @ModelAttribute("donor") HosDonor donor,
                         BindingResult binding,
                         Model model) {
        Hospital hospital = hospitalService.getById(hospitalId);

        if (hospital == null) {
            throw new IllegalArgumentException("Hospital not found for ID " + hospitalId);
        }

        if (binding.hasErrors()) {
            model.addAttribute("hospitalId", hospitalId);
            return "hospital-donor-edit";
        }

        donor.setId(id);
        donor.setHospital(hospital);
        donorService.update(id, donor);

        return "redirect:/hospitals/" + hospitalId + "/donors";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    public String confirmDelete(@PathVariable Long hospitalId, @PathVariable Long id, Model model) {
        HosDonor donor = donorService.getById(id);
        model.addAttribute("donor", donor);
        model.addAttribute("hospitalId", hospitalId);
        return "hospital-donor-delete"; // your confirmation page
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long hospitalId, @PathVariable Long id, Model model) {
        HosDonor donor = donorService.getById(id);
        model.addAttribute("donor", donor);
        model.addAttribute("hospitalId", hospitalId);
        return "hospital-donor-view";
    }
}