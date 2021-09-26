package com.spoctexter.UserAccountLayer;

import com.spoctexter.UserProfileLayer.UserProfile;
import com.spoctexter.UserProfileLayer.UserProfileService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.OffsetDateTime;

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

        UserProfile userProfile = userProfileService.getUserProfileByEmail(email).orElse(null);

        userAccount.setUserProfile(userProfile);
        userAccount.setCreatedAt(OffsetDateTime.now());
        userAccountService.addNewUserAccount(userAccount);
    }

    @GetMapping (path = "email={email}")
    public UserAccount getUserAccountByEmail (
            @NotNull @PathVariable("email") String email
    ) {
        return userAccountService.getUserAccountByEmail(email);
    }

    @GetMapping (path = "phone={phoneNumber}")
    public UserAccount getUserAccountByPhoneNumber (
            @NotNull @PathVariable("phoneNumber") String phoneNumber
    ) {
        return userAccountService.getUserAccountByPhoneNumber(phoneNumber);
    }

    @GetMapping (path = "username={userName}")
    public UserAccount getUserAccountByUserName (
            @NotNull @PathVariable("userName") String userName
    ) {
        return userAccountService.getUserAccountByUserName(userName);
    }

    @GetMapping(path = "pwusername={userName}")
    public Boolean validatePassword (
            @NotNull @RequestParam String password,
            @NotNull @PathVariable("userName") String userName
    ) {
        return userAccountService.validatePassword(userName, password);
    }

    //Add putmapping for password change method here and in Service (update)
    //Add getmapping for userAccount by ID
}
