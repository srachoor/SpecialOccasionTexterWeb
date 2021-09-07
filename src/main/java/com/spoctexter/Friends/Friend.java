package com.spoctexter.Friends;

import com.spoctexter.UserAccountLayer.UserAccount;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Entity (name = "Friend") //this Entity name gets referenced in our JPQL queries
@Table
public class Friend {

    @Id
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String friendFirstName;

    @Column(nullable = false)
    private String friendLastName;

    @Column(unique = true, nullable = false)
    private String friendPhoneNumber;

    @Column(nullable = true)
    private OffsetDateTime friendDOB;

    @ManyToOne
    @JoinColumn(
            name = "user_profile_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey (
                    name = "user_profile_id_fk"
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

