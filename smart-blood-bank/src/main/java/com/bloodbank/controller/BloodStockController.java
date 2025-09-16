package com.bloodbank.controller;

import com.bloodbank.entity.BloodStock;
import com.bloodbank.service.BloodStockService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bloodstock")
@PreAuthorize("hasRole('ADMIN')")
public class BloodStockController {

    private final BloodStockService stockService;

    public BloodStockController(BloodStockService stockService) {
        this.stockService = stockService;
    }

    // List all blood stock
    @GetMapping
    public String listStock(Model model) {
        model.addAttribute("bloodStocks", stockService.getAllStock());
        return "bloodstock-list";
    }

    // Show form to add new stock
    @GetMapping("/add")
    public String newStockForm(Model model) {
        model.addAttribute("stock", new BloodStock());
        return "bloodstock-add";
    }

    // Save or update stock
    @PostMapping("/save")
    public String saveStock(@ModelAttribute BloodStock stock) {
        stockService.saveOrUpdate(stock);
        return "redirect:/bloodstock?success";
    }

    @PostMapping("/update")
    public String updateStock(@ModelAttribute BloodStock stock) {
        stockService.saveOrUpdate(stock);
        return "redirect:/bloodstock?updated";
    }

    // Edit existing stock
    @GetMapping("/edit/{id}")
    public String editStock(@PathVariable Long id, Model model) {
        BloodStock stock = stockService.getById(id);
        model.addAttribute("stock", stock);
        return "bloodstock-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteStock(@PathVariable Long id) {
        stockService.deleteById(id);
        return "redirect:/bloodstock";
    }
}