package com.spoctexter.Friends;

import com.spoctexter.UserAccountLayer.UserAccount;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Entity (name = "Friend")
@Table (
        name = "friend",
        uniqueConstraints = {
                @UniqueConstraint(name = "friend_phone_number_unique", columnNames = "friend_phone_number")
        }
)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",updatable = false, nullable = false)
    private UUID id;

    @Column(name = "friend_first_name", nullable = false, columnDefinition = "TEXT")
    private String friendFirstName;

    @Column(name = "friend_last_name", nullable = false, columnDefinition = "TEXT")
    private String friendLastName;

    @Column(name = "friend_phone_number", nullable = false, columnDefinition = "TEXT")
    private String friendPhoneNumber;

    @Column(name = "friend_DOB", nullable = true, columnDefinition = "DATE")
    private OffsetDateTime friendDOB;

    @ManyToOne
    @JoinColumn(
            name = "user_profile_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey (
                    name = "user_profile_id_fk_in_friend"
            )
    )
    UserAccount userAccount;

    public Friend(UUID id, String friendFirstName, String friendLastName, String friendPhoneNumber, OffsetDateTime friendDOB) {
        this.id = id;
        this.friendFirstName = friendFirstName;
        this.friendLastName = friendLastName;
        this.friendPhoneNumber = friendPhoneNumber;
        this.friendDOB = friendDOB;
    }

    public Friend(String friendFirstName, String friendLastName, String friendPhoneNumber, OffsetDateTime friendDOB) {
        this.friendFirstName = friendFirstName;
        this.friendLastName = friendLastName;
        this.friendPhoneNumber = friendPhoneNumber;
        this.friendDOB = friendDOB;
    }

    public Friend(String friendFirstName, String friendLastName, String friendPhoneNumber, OffsetDateTime friendDOB, UserAccount userAccount) {
        this.friendFirstName = friendFirstName;
        this.friendLastName = friendLastName;
        this.friendPhoneNumber = friendPhoneNumber;
        this.friendDOB = friendDOB;
        this.userAccount = userAccount;
    }

    public Friend() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFriendFirstName() {
        return friendFirstName;
    }

    public void setFriendFirstName(String friendFirstName) {
        this.friendFirstName = friendFirstName;
    }

    public String getFriendLastName() {
        return friendLastName;
    }

    public void setFriendLastName(String friendLastName) {
        this.friendLastName = friendLastName;
    }

    public String getFriendPhoneNumber() {
        return friendPhoneNumber;
    }

    public void setFriendPhoneNumber(String friendPhoneNumber) {
        this.friendPhoneNumber = friendPhoneNumber;
    }

    public OffsetDateTime getFriendDOB() {
        return friendDOB;
    }

    public void setFriendDOB(OffsetDateTime friendDOB) {
        this.friendDOB = friendDOB;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", friendFirstName='" + friendFirstName + '\'' +
                ", friendLastName='" + friendLastName + '\'' +
                ", friendPhoneNumber='" + friendPhoneNumber + '\'' +
                ", friendDOB=" + friendDOB +
                ", userAccount=" + userAccount +
                '}';
    }
}

