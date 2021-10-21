package com.spoctexter.UserProfileLayer;

import com.sun.istack.NotNull;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/spoc/profile")
public class UserProfileController {

    private final UserProfileService userService;

    @Autowired
    public UserProfileController(UserProfileService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "addUser") //Another way to add a user profile
    public void addUserProfile(
            @RequestParam(name = "firstName") String firstName,
            @RequestParam(name = "lastName") String lastName,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "phoneNumber") String phoneNumber
            ){

        UserProfile newUserProfile = new UserProfile(
                UUID.randomUUID(),
                firstName,
                lastName,
                email,
                phoneNumber,
                OffsetDateTime.now()
        );

        userService.addNewUserProfile(newUserProfile);
    }

    @PostMapping
    public void addNewUserProfile(@NotNull @Valid @RequestBody UserProfile userProfile) {
        UUID uuid = UUID.randomUUID();
        userProfile.setId(uuid);
        userProfile.setCreatedAt(OffsetDateTime.now());
        userService.addNewUserProfile(userProfile);
    }

    @GetMapping(path = "id={id}")
    public UserProfile getUserProfileByID(@NotNull @PathVariable("id") UUID id) {
        return userService.getUserProfileByID(id);
    }

    @GetMapping(path = "email={email}")
    public UserProfile getUserProfileByEmail(@NotNull @PathVariable("email") String email) {
        return userService.getUserProfileByEmail(email);
    }

    @GetMapping(path = "phoneNumber={phoneNumber}")
    public UserProfile getUserProfileByPhoneNumber(@NotNull @PathVariable("phoneNumber") String phoneNumber) {
        return userService.getUserProfileByPhoneNumber(phoneNumber);
    }

    @DeleteMapping(path = "id={id}")
    public void deleteUserProfileById(@NotNull @PathVariable("id") UUID id) {
        userService.deleteUserProfileByID(id);
    }

    @DeleteMapping(path = "email={email}")
    public void deleteUserProfileByEmail(@NotNull @PathVariable("email") String email) {
        userService.deleteUserProfileByEmail(email);
    }

    @DeleteMapping(path = "phone={phoneNumber}")
    public void deleteUserProfileByPhoneNumber(@NotNull @PathVariable("phoneNumber") String phoneNumber) {
        userService.deleteUserProfileByPhoneNumber(phoneNumber);
    }

    @PutMapping(path = "oldEmail={email}/newEmail={newEmail}")
    public void updateUserProfileEmail(@NotNull @PathVariable("email") String email, @NotNull @PathVariable("newEmail") String newEmail) {
        userService.updateUserProfileEmail(email, newEmail);
    }

    @PutMapping(path = "oldPhone={phone}/newPhone={newPhone}")
    public void updateUserProfilePhone(@NotNull @PathVariable("phone") String phoneNumber, @NotNull @PathVariable("newPhone") String newPhoneNumber) {
        userService.updateUserProfilePhoneNumber(phoneNumber, newPhoneNumber);
    }

    @PutMapping(path = "email={email}/newFirstName={newFirstName}")
    public void updateUserProfileFirstName(
            @NotNull @PathVariable("email") String email,
            @NotNull @PathVariable("newFirstName") String newFirstName) {

        userService.updateUserProfileFirstName(email, newFirstName);
    }

    @PutMapping(path = "email={email}/newLastName={newLastName}")
    public void updateUserProfileLastName(
            @NotNull @PathVariable("email") String email,
            @NotNull @PathVariable("newLastName") String newLastName) {

        userService.updateUserProfileLastName(email, newLastName);
    }


}
