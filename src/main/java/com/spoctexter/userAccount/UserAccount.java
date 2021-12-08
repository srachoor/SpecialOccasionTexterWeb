package com.spoctexter.userAccount;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spoctexter.friends.Friend;
import com.spoctexter.security.Role;
import com.spoctexter.userProfile.UserProfile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(
            mappedBy = "userAccount",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<Friend> friends = new ArrayList<>();

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public UserAccount(UserProfile userProfile,
                       String username,
                       String password,
                       OffsetDateTime createdAt,
                       List<Friend> friends,
                       boolean isAccountNonExpired,
                       boolean isAccountNonLocked,
                       boolean isCredentialsNonExpired,
                       boolean isEnabled) {
        this.userProfile = userProfile;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.friends = friends;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    public UserAccount(String username,
                       String password,
                       OffsetDateTime createdAt,
                       List<Friend> friends,
                       boolean isAccountNonExpired,
                       boolean isAccountNonLocked,
                       boolean isCredentialsNonExpired,
                       boolean isEnabled) {
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.friends = friends;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    public UserAccount(String username, String password, OffsetDateTime createdAt, UserProfile userProfile) {
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.userProfile = userProfile;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }
//
    public UserAccount(String username, String password, OffsetDateTime createdAt) {
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdAt = OffsetDateTime.now();
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    public UserAccount() {
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
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

    public Role getRole() {
        return role;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", userPassword='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", userProfile=" + userProfile +
                '}';
    }

}
