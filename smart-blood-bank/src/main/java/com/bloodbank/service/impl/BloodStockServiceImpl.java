package com.bloodbank.service.impl;

import com.bloodbank.entity.BloodStock;
import com.bloodbank.repository.BloodStockRepository;
import com.bloodbank.service.BloodStockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodStockServiceImpl implements BloodStockService {

    private final BloodStockRepository stockRepository;

    public BloodStockServiceImpl(BloodStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<BloodStock> getAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public Optional<BloodStock> getByBloodGroup(String bloodGroup) {
        return stockRepository.findByBloodGroup(bloodGroup);
    }

    @Override
    public BloodStock saveOrUpdate(BloodStock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public BloodStock getById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blood stock not found with id " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new RuntimeException("Blood stock not found with id " + id);
        }
        stockRepository.deleteById(id);
    }
}
