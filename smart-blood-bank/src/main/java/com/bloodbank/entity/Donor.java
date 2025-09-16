package com.bloodbank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Donor {

    public interface OnCreate {}
    public interface OnUpdate {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required", groups = {OnCreate.class, OnUpdate.class})
    private String fullName;

    @Min(value = 18, message = "Minimum age is 18", groups = {OnCreate.class, OnUpdate.class})
    @Max(value = 65, message = "Maximum age is 65", groups = {OnCreate.class, OnUpdate.class})
    private int age;

    @NotBlank(message = "Gender is required", groups = {OnCreate.class, OnUpdate.class})
    private String gender;

    @NotBlank(message = "Blood group is required", groups = {OnCreate.class, OnUpdate.class})
    private String bloodGroup;

    @Past(message = "Date of birth must be in the past", groups = {OnCreate.class, OnUpdate.class})
    private java.time.LocalDate dob;

    @NotBlank(message = "Contact number is required", groups = {OnCreate.class, OnUpdate.class})
    private String contactNumber;

    @Email(message = "Invalid email", groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "Email is required", groups = OnCreate.class)
    private String email;

    @NotBlank(message = "Password is required", groups = OnCreate.class)
    private String password; // only validated on register

    @NotBlank(message = "City is required", groups = {OnCreate.class, OnUpdate.class})
    private String city;

    private String state;

    @NotBlank(message = "Pincode is required", groups = {OnCreate.class, OnUpdate.class})
    private String pincode;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate lastDonationDate;

    private boolean hasChronicDiseases;
    private boolean hasRecentSurgery;
    private boolean hasAllergies;
    private String otherMedicalConditions;

    private boolean eligible;

    private boolean available = true;
    private boolean verified = false;

    private String role = "DONOR";

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }

    public boolean isHasChronicDiseases() { return hasChronicDiseases; }
    public void setHasChronicDiseases(boolean hasChronicDiseases) { this.hasChronicDiseases = hasChronicDiseases; }

    public boolean isHasRecentSurgery() { return hasRecentSurgery; }
    public void setHasRecentSurgery(boolean hasRecentSurgery) { this.hasRecentSurgery = hasRecentSurgery; }

    public boolean isHasAllergies() { return hasAllergies; }
    public void setHasAllergies(boolean hasAllergies) { this.hasAllergies = hasAllergies; }

    public String getOtherMedicalConditions() { return otherMedicalConditions; }
    public void setOtherMedicalConditions(String otherMedicalConditions) { this.otherMedicalConditions = otherMedicalConditions; }

    public boolean isEligible() { return eligible; }
    public void setEligible(boolean eligible) { this.eligible = eligible; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
}
