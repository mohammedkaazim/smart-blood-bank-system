package com.bloodbank.service;

import com.bloodbank.dto.HospitalDashboardStats;
import com.bloodbank.entity.Hospital;
import com.bloodbank.repository.HospitalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HospitalService {

    Hospital create(Hospital hospital);
    Hospital update(Long id, Hospital hospital);
    void delete(Long id);
    Hospital getById(Long id);

    Page<Hospital> list(String q, int page, int size, String sortField, String sortDir) ;

    HospitalDashboardStats getDashboardStats(Long hospitalId);
}
