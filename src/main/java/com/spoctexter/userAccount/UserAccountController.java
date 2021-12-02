package com.spoctexter.userAccount;

import com.spoctexter.userProfile.UserProfile;
import com.spoctexter.userProfile.UserProfileService;
import com.spoctexter.exception.NamingConflictException;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/spoc/account")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final UserProfileService userProfileService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService, UserProfileService userProfileService) {
        this.userAccountService = userAccountService;
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public void addNewUserAccount (
            @NotNull @Valid @RequestBody UserAccount userAccount,
            @NotNull @RequestParam(name = "email") String email) {

        UserProfile userProfile = userProfileService.getUserProfileByEmail(email);

        if (userProfile.getUserAccount() != null) {
            throw new NamingConflictException("This user profile already has an existing username and password.");
        }

        userAccount.setUserProfile(userProfile);
        userAccount.setCreatedAt(OffsetDateTime.now());
        userAccountService.addNewUserAccount(userAccount);
    }

    @GetMapping (path = "id = {id}")
    public UserAccount getUserAccountByID (
            @NotNull @PathVariable("id") UUID id
    ) {
        return userProfileService.getUserProfileByID(id).getUserAccount();
    }

    @GetMapping (path = "email={email}")
    public UserAccount getUserAccountByEmail (
            @NotNull @PathVariable("email") String email
    ) {
        return userProfileService.getUserProfileByEmail(email).getUserAccount();
    }

    @GetMapping (path = "phone={phoneNumber}")
    public UserAccount getUserAccountByPhoneNumber (
            @NotNull @PathVariable("phoneNumber") String phoneNumber
    ) {
        return userProfileService.getUserProfileByPhoneNumber(phoneNumber).getUserAccount();
    }

    @GetMapping (path = "username={userName}")
    public UserAccount getUserAccountByUserName (
            @NotNull @PathVariable("userName") String userName
    ) {
        return userAccountService.getUserAccountByUserName(userName);
    }

    @GetMapping(path = "pwusername={userName}")
    public Boolean validatePassword (
            @NotNull @RequestParam (name = "password") String password,
            @NotNull @PathVariable("userName") String userName
    ) {
        return userAccountService.validatePassword(userName, password);
    }

    @PutMapping(path = "unchange={userName}")
    public void changeUserName (
            @NotNull @RequestParam String newUserName,
            @NotNull @PathVariable String userName
    ) {
        userAccountService.updateUserName(userName, newUserName);
    }


    @PutMapping(path = "pwchange={userName}")
    public void changePassword (
            @NotNull @RequestParam(name = "currentPassword") String currentPassword,
            @NotNull @RequestParam(name = "newPassword") String newPassword,
            @NotNull @PathVariable(name = "userName") String userName
    ) {
        if (userAccountService.validatePassword(userName,currentPassword)){
            userAccountService.updatePassword(userName,newPassword);
        }
    }

}
