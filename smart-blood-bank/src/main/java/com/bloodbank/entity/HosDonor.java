package com.bloodbank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class HosDonor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Min(value = 18, message = "Minimum age is 18")
    @Max(value = 65, message = "Maximum age is 65")
    private int age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @Email(message = "Invalid email")
    private String email;

    private String address;
    private String city;
    private String state;
    private String pincode;

    @NotBlank(message = "Blood group is required")
    private String bloodGroup;

    private LocalDate lastDonationDate;

    private boolean hasChronicDiseases;
    private boolean hasRecentSurgery;
    private boolean hasAllergies;
    private String otherMedicalConditions;

    private boolean eligible;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    // Getter and Setter


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Full name is required") String getFullName() {
        return fullName;
    }

    public void setFullName(@NotBlank(message = "Full name is required") String fullName) {
        this.fullName = fullName;
    }

    @Min(value = 18, message = "Minimum age is 18")
    @Max(value = 65, message = "Maximum age is 65")
    public int getAge() {
        return age;
    }

    public void setAge(@Min(value = 18, message = "Minimum age is 18") @Max(value = 65, message = "Maximum age is 65") int age) {
        this.age = age;
    }

    public @NotBlank(message = "Gender is required") String getGender() {
        return gender;
    }

    public void setGender(@NotBlank(message = "Gender is required") String gender) {
        this.gender = gender;
    }

    public @NotBlank(message = "Contact number is required") String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(@NotBlank(message = "Contact number is required") String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public @Email(message = "Invalid email") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Invalid email") String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public @NotBlank(message = "Blood group is required") String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(@NotBlank(message = "Blood group is required") String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public LocalDate getLastDonationDate() {
        return lastDonationDate;
    }

    public void setLastDonationDate(LocalDate lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }

    public boolean isHasChronicDiseases() {
        return hasChronicDiseases;
    }

    public void setHasChronicDiseases(boolean hasChronicDiseases) {
        this.hasChronicDiseases = hasChronicDiseases;
    }

    public boolean isHasRecentSurgery() {
        return hasRecentSurgery;
    }

    public void setHasRecentSurgery(boolean hasRecentSurgery) {
        this.hasRecentSurgery = hasRecentSurgery;
    }

    public boolean isHasAllergies() {
        return hasAllergies;
    }

    public void setHasAllergies(boolean hasAllergies) {
        this.hasAllergies = hasAllergies;
    }

    public String getOtherMedicalConditions() {
        return otherMedicalConditions;
    }

    public void setOtherMedicalConditions(String otherMedicalConditions) {
        this.otherMedicalConditions = otherMedicalConditions;
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
