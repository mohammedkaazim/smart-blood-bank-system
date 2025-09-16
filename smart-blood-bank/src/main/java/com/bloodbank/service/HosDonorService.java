package com.bloodbank.service;

import com.bloodbank.entity.HosDonor;
import com.bloodbank.entity.Hospital;
import org.springframework.data.domain.Page;

public interface HosDonorService {

    HosDonor create(HosDonor donor);

    HosDonor update(Long id, HosDonor donor);

    void delete(Long id);

    HosDonor getById(Long id);

    Page<HosDonor> listByHospital(Hospital hospital, int page, int size, String sort, String dir);

    Page<HosDonor> searchByName(Hospital hospital, String name, int page, int size);

    Page<HosDonor> filterByBloodGroup(Hospital hospital, String bloodGroup, int page, int size);

    Page<HosDonor> searchByNameCityPincode(Hospital hospital, String keyword, int page, int size);

    Page<HosDonor> searchByNameCityPincodeAndBlood(Hospital hospital, String keyword, String bloodGroup, int page, int size);
}
