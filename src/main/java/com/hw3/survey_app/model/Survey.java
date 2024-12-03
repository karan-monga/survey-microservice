package com.hw3.survey_app.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity // Marks this class as a JPA entity mapped to a database table
public class Survey {
    @Id // Specifies the primary key of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates unique IDs for each survey
    private Long id;

    // Basic details about the respondent
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String telephone;
    private String email;

    @Temporal(TemporalType.DATE) // Maps this field to a DATE column in the database
    private Date dateOfSurvey;

    // Fields capturing survey-specific preferences
    private String likedMost; // Options: students, location, campus, etc.
    private String interestedBy; // Options: friends, television, etc.
    private String recommendationLikelihood; // Options: Very Likely, Likely, Unlikely

    // Getters and Setters for encapsulation and access to private fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfSurvey() {
        return dateOfSurvey;
    }

    public void setDateOfSurvey(Date dateOfSurvey) {
        this.dateOfSurvey = dateOfSurvey;
    }

    public String getLikedMost() {
        return likedMost;
    }

    public void setLikedMost(String likedMost) {
        this.likedMost = likedMost;
    }

    public String getInterestedBy() {
        return interestedBy;
    }

    public void setInterestedBy(String interestedBy) {
        this.interestedBy = interestedBy;
    }

    public String getRecommendationLikelihood() {
        return recommendationLikelihood;
    }

    public void setRecommendationLikelihood(String recommendationLikelihood) {
        this.recommendationLikelihood = recommendationLikelihood;
    }
}
