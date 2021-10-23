package com.spoctexter.friends;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spoctexter.Occasions.Occasion;
import com.spoctexter.UserAccountLayer.UserAccount;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity (name = "Friend") //this Entity name gets referenced in our JPQL queries
@Table
public class Friend {

    @Id
    @Column(unique = true, updatable = false, nullable = false)
    private UUID friendId;

    @Column(nullable = false)
    private String friendFirstName;

    @Column(nullable = true)
    private String friendLastName;

    @Column(nullable = true)
    private String friendPhoneNumber;

    @Column(nullable = true)
    private LocalDate friendDOB;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            mappedBy = "friend",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<Occasion> occasions  = new ArrayList<>();

    public Friend(UUID id, String friendFirstName, String friendLastName, String friendPhoneNumber, LocalDate friendDOB) {
        this.friendId = id;
        this.friendFirstName = friendFirstName;
        this.friendLastName = friendLastName;
        this.friendPhoneNumber = friendPhoneNumber;
        this.friendDOB = friendDOB;
    }

    public Friend(String friendFirstName, String friendLastName, String friendPhoneNumber, LocalDate friendDOB, UserAccount userAccount) {
        this.friendFirstName = friendFirstName;
        this.friendLastName = friendLastName;
        this.friendPhoneNumber = friendPhoneNumber;
        this.friendDOB = friendDOB;
        this.userAccount = userAccount;
    }

    public Friend() {
    }

    public UUID getFriendId() {
        return friendId;
    }

    public void setFriendId(UUID friendId) {
        this.friendId = friendId;
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

    public LocalDate getFriendDOB() {
        return friendDOB;
    }

    public void setFriendDOB(LocalDate friendDOB) {
        this.friendDOB = friendDOB;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public void addOccasion(Occasion occasion) {
        if (!this.occasions.contains(occasion)) {
            this.occasions.add(occasion);
            occasion.setFriend(this);
        }
    }

    public void removeOccasion(Occasion occasion) {
        if (this.occasions.contains(occasion)) {
            this.occasions.remove(occasion);
            occasion.setFriend(null);
        }
    }

    public List<Occasion> getOccasions() {
        return occasions;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + friendId +
                ", friendFirstName='" + friendFirstName + '\'' +
                ", friendLastName='" + friendLastName + '\'' +
                ", friendPhoneNumber='" + friendPhoneNumber + '\'' +
                ", friendDOB=" + friendDOB +
                ", userAccount=" + userAccount +
                '}';
    }
}

