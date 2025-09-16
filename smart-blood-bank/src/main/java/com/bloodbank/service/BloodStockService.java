package com.bloodbank.service;

import com.bloodbank.entity.BloodStock;

import java.util.List;
import java.util.Optional;

public interface BloodStockService {
    List<BloodStock> getAllStock();
    Optional<BloodStock> getByBloodGroup(String bloodGroup);
    BloodStock saveOrUpdate(BloodStock stock);
    BloodStock getById(Long id);
    void deleteById(Long id);
}
