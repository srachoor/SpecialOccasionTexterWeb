package com.spoctexter.UserAccountLayer;

import com.spoctexter.UserProfileLayer.UserProfile;
import com.spoctexter.UserProfileLayer.UserProfileService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.OffsetDateTime;
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

        UserProfile userProfile = userProfileService.getUserProfileByEmail(email).orElse(null);

        userAccount.setUserProfile(userProfile);
        userAccount.setCreatedAt(OffsetDateTime.now());
        userAccountService.addNewUserAccount(userAccount);
    }


}
