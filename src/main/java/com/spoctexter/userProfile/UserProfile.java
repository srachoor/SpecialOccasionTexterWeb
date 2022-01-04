package com.spoctexter.userProfile;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spoctexter.userAccount.UserAccount;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity (name = "UserProfile")  //this Entity name gets referenced in our JPQL queries
@Table
public class UserProfile {

    @Id
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @JsonBackReference
    @OneToOne (
            mappedBy = "userProfile",
            orphanRemoval = true
    )
    private UserAccount userAccount;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = true)
    private OffsetDateTime createdAt;

    public UserProfile(UUID id,
                       String firstName,
                       String lastName,
                       String email,
                       String phoneNumber,
                       OffsetDateTime createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email.toLowerCase();
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    public UserProfile(UUID id, String firstName, String lastName, String email, String phoneNumber, OffsetDateTime createdAt, UserAccount userAccount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email.toLowerCase();
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.userAccount = userAccount;
    }

    public UserProfile(UserAccount userAccount, String firstName, String lastName, String email, String phoneNumber) {
        this.userAccount = userAccount;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email.toLowerCase();
        this.phoneNumber = phoneNumber;
    }

    public UserProfile() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createdAt=" + createdAt +
                ", userAccount=" + userAccount +
                '}';
    }
}
