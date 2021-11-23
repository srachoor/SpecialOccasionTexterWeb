package com.spoctexter.userAccount;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spoctexter.friends.Friend;
import com.spoctexter.userProfile.UserProfile;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity (name = "UserAccount")  //this Entity name gets referenced in our JPQL queries
@Table
public class UserAccount {

    @Id
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonManagedReference
    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id")
    @MapsId
    private UserProfile userProfile;

    @Column(unique = true, nullable = false)
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String userPassword;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(
            mappedBy = "userAccount",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<Friend> friends = new ArrayList<>();

    public UserAccount(String userName, String userPassword, OffsetDateTime createdAt, UserProfile userProfile) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.createdAt = createdAt;
        this.userProfile = userProfile;
    }

    public UserAccount(String userName, String userPassword, OffsetDateTime createdAt) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.createdAt = createdAt;
    }

    public UserAccount(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public UserAccount() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void addFriend(Friend friend) {
        if (!this.friends.contains(friend)) {
            this.friends.add(friend);
            friend.setUserAccount(this);
        }
    }

    public void removeFriend(Friend friend) {
        if (this.friends.contains(friend)) {
            this.friends.remove(friend);
            friend.setUserAccount(null);
        }
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", createdAt=" + createdAt +
                ", userProfile=" + userProfile +
                '}';
    }

}
