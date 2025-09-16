package com.bloodbank.security;

import com.bloodbank.entity.Donor;
import com.bloodbank.repository.DonorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final DonorRepository donorRepository;

    public CustomUserDetailsService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Donor donor = donorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No donor found with email: " + email));

        // Print debug info
        System.out.println("Loaded user: " + donor.getEmail() + " | role=" + donor.getRole());

        String role = donor.getRole();
        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        return User.builder()
                .username(donor.getEmail())
                .password(donor.getPassword())
                .roles(role)
                .build();
    }
}
