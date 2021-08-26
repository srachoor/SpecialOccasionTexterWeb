package com.spoctexter.UserAccountLayer;

import com.spoctexter.Friends.Friend;
import com.spoctexter.UserProfileLayer.User_profile;
import org.hibernate.annotations.GenerationTime;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity (name = "User_account")  //this Entity name gets referenced in our JPQL queries
@Table (
        name = "user_account",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_name_unique",columnNames = "user_name")}
)
public class UserAccount {

    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "account_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name =  "user_name", nullable = false, columnDefinition = "TEXT")
    private String userName;

    @Column(name = "user_password", nullable = false, columnDefinition = "TEXT")
    private String userPassword;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "user_profile_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_profile_id_fk")
    )
    private User_profile userProfile;

    @OneToMany(
            mappedBy = "userAccount",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
//    @JoinColumn(
//            name = "user_account_id",
//            referencedColumnName = "user_profile_id"
//    )
    private List<Friend> friends = new ArrayList<>();

    public UserAccount(String userName, String userPassword, OffsetDateTime createdAt, User_profile userProfile) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.createdAt = createdAt;
        this.userProfile = userProfile;
    }

    public UserAccount(Long id, String userName, String userPassword, OffsetDateTime createdAt, User_profile userProfile) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User_profile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(User_profile userProfile) {
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
