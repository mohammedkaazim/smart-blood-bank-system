package com.bloodbank.service;

import com.bloodbank.entity.Donor;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface DonorService {

    Donor register(Donor donor);
    Donor update(Long id, Donor donor);
    void delete(Long id);
    Optional<Donor> getById(Long id);
    Optional<Donor> getByEmail(String email);

    Page<Donor> listAll(int page, int size, String sort, String dir);
    Page<Donor> searchByName(String name, int page, int size, String sort, String dir);
    Page<Donor> searchByCity(String city, int page, int size, String sort, String dir);
    Page<Donor> searchByPincode(String pincode, int page, int size, String sort, String dir);
    Page<Donor> filterByBloodGroup(String bloodGroup, int page, int size, String sort, String dir);
    Page<Donor> filterByBloodGroupAndAvailability(String bloodGroup, boolean available, int page, int size, String sort, String dir);
    Page<Donor> filterByAvailability(boolean available, int page, int size, String sort, String dir);
    Page<Donor> searchByKeyword(String keyword, int page, int size, String sort, String dir);

}
