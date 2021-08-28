package com.spoctexter.UserAccountLayer;

import com.spoctexter.Friends.Friend;
import com.spoctexter.UserProfileLayer.UserProfile;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity (name = "UserAccount")  //this Entity name gets referenced in our JPQL queries
@Table
public class UserAccount {

    @Id
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id")
    @MapsId
    private UserProfile userProfile;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userPassword;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

//    @OneToMany(
//            mappedBy = "userAccount",
//            orphanRemoval = true,
//            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
//    )
//    @JoinColumn(
//            name = "user_account_id",
//            referencedColumnName = "user_profile_id"
//    )
//    private List<Friend> friends = new ArrayList<>();

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

//    public void addFriend(Friend friend) {
//        if (!this.friends.contains(friend)) {
//            this.friends.add(friend);
//            friend.setUserAccount(this);
//        }
//    }
//
//    public void removeFriend(Friend friend) {
//        if (this.friends.contains(friend)) {
//            this.friends.remove(friend);
//            friend.setUserAccount(null);
//        }

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
//    }

}
