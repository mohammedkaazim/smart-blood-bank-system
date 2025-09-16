package com.bloodbank.config;

import com.bloodbank.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // In-memory admin + hospital accounts (for testing)
    @Bean
    public UserDetailsService inMemoryUsers() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin123"))
                        .roles("ADMIN")
                        .build(),
                User.withUsername("hosp1")
                        .password(passwordEncoder().encode("pass123"))
                        .roles("HOSPITAL")
                        .build()
        );
    }

    // Combines DB users (donors, etc.) + in-memory users (admin, hospital)
    @Bean
    public UserDetailsService userDetailsService(UserDetailsService inMemoryUsers) {
        return username -> {
            try {
                return customUserDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                return inMemoryUsers.loadUserByUsername(username);
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                         PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return (request, response, authentication) -> {
            var authorities = authentication.getAuthorities();
            String redirectUrl = "/";

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                redirectUrl = "/admin/dashboard";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_HOSPITAL"))) {
                redirectUrl = "/hospitals/dashboard";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_DONOR"))) {
                redirectUrl = "/donors/profile";
            }

            response.sendRedirect(redirectUrl);
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Admin + Hospital security rules
    @Bean
    @Order(1)
    public SecurityFilterChain adminHospitalSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**", "/hospitals/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/hospitals/**").hasAnyRole("HOSPITAL", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(roleBasedSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    // Donor security rules
    @Bean
    @Order(2)
    public SecurityFilterChain donorSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/donors/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/donors/register", "/donors/login", "/donors/authenticate").permitAll()
                        .requestMatchers("/donors/profile/**").hasRole("DONOR")
                        .requestMatchers("/donors/**").hasAnyRole("DONOR", "ADMIN")
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/donors/login")
                        .loginProcessingUrl("/donors/authenticate")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/donors/profile", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/donors/logout")
                        .logoutSuccessUrl("/donors/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    // Default security fallback
    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurity(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
