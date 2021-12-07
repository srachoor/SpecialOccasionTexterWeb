package com.spoctexter.userAccount;

import com.spoctexter.exception.BadInputException;
import com.spoctexter.exception.ForbiddenAccessException;
import com.spoctexter.userProfile.UserProfile;
import com.spoctexter.userProfile.UserProfileService;
import com.spoctexter.exception.NamingConflictException;
import com.sun.istack.NotNull;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/spoc/account")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAccountController(UserAccountService userAccountService, UserProfileService userProfileService, PasswordEncoder passwordEncoder) {
        this.userAccountService = userAccountService;
        this.userProfileService = userProfileService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping (path = "signup")
    public void addNewUserAccount (
            @NotNull @Valid @RequestBody UserAccount userAccount,
            @NotNull @RequestParam(name = "email") String email) {

        UserProfile userProfile = userProfileService.getUserProfileByEmail(email);

        if (userProfile.getUserAccount() != null) {
            throw new NamingConflictException("This user profile already has an existing username and password.");
        }

        UserAccount newUserAccount = new UserAccount(userAccount.getUsername(), passwordEncoder.encode(userAccount.getPassword()));

        newUserAccount.setUserProfile(userProfile);
        newUserAccount.setCreatedAt(OffsetDateTime.now());
        userAccountService.addNewUserAccount(newUserAccount);
    }

    @GetMapping (path = "id={id}")
    public UserAccount getUserAccountByID (
            @NotNull @PathVariable("id") UUID id, Principal principal
    ) {
        if (userAccountService.getUserAccountByUserName(principal.getName()).getId().equals(id)) {
            return userProfileService.getUserProfileByID(id).getUserAccount();
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @GetMapping (path = "email={email}")
    public UserAccount getUserAccountByEmail (
            @NotNull @PathVariable("email") String email, Principal principal
    ) {
        if (userAccountService.getUserAccountByUserName(principal.getName()).getUserProfile().getEmail().equals(email)) {
            return userProfileService.getUserProfileByEmail(email).getUserAccount();
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }

    }

    @GetMapping (path = "phone={phoneNumber}")
    public UserAccount getUserAccountByPhoneNumber (
            @NotNull @PathVariable("phoneNumber") String phoneNumber, Principal principal
    ) {

        if (userAccountService.getUserAccountByUserName(principal.getName()).getUserProfile().getPhoneNumber().equals(phoneNumber)) {
            return userProfileService.getUserProfileByPhoneNumber(phoneNumber).getUserAccount();
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }

    }

    @GetMapping (path = "username={userName}")
    @PreAuthorize(value = "authentication.principal.equals(#userName)")
    public UserAccount getUserAccountByUserName (
            @NotNull @PathVariable("userName") String userName
    ) {
        return userAccountService.getUserAccountByUserName(userName);
    }

    @GetMapping (path = "{userName}")
    @PreAuthorize(value = "authentication.principal.equals(#userName)")
    public UserProfile getUserProfileByUserName (
            @NotNull @PathVariable("userName") String userName
    ) {
        return userAccountService.getUserAccountByUserName(userName).getUserProfile();
    }

    @GetMapping(path = "pwusername={userName}")
    @PreAuthorize(value = "authentication.principal.equals(#userName)")
    public Boolean validatePassword (
            @NotNull @RequestParam (name = "password") String password,
            @NotNull @PathVariable("userName") String userName
    ) {
        System.out.println(password);
        return userAccountService.validatePassword(userName, password);
    }

    @PutMapping(path = "unchange={userName}")
    @PreAuthorize(value = "authentication.principal.equals(#userName)")
    public void changeUserName (
            @NotNull @RequestParam String newUserName,
            @NotNull @PathVariable String userName
    ) {
        userAccountService.updateUserName(userName, newUserName);
    }

    @PutMapping(path = "pwchange={userName}")
    @PreAuthorize(value = "authentication.principal.equals(#userName)")
    public void changePassword (
            @NotNull @RequestParam(name = "currentPassword") String currentPassword,
            @NotNull @RequestParam(name = "newPassword") String newPassword,
            @NotNull @PathVariable(name = "userName") String userName
    ) {
        if (userAccountService.validatePassword(userName,currentPassword)){
            userAccountService.updatePassword(userName,passwordEncoder.encode(newPassword));
        }
    }
}
