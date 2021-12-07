package com.spoctexter.userProfile;

import com.spoctexter.exception.ForbiddenAccessException;
import com.spoctexter.jwt.JwtConfig;
import com.sun.istack.NotNull;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/spoc/profile")
public class UserProfileController {

    private final UserProfileService userService;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Autowired
    public UserProfileController(UserProfileService userService,
                                 JwtConfig jwtConfig,
                                 SecretKey secretKey
    ) {
        this.userService = userService;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
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
    public UserProfile getUserProfileByID(@NotNull @PathVariable("id") UUID id, Principal principal) {
        if(principal.getName().equals(userService.getUserProfileByID(id).getUserAccount().getUsername())) {
            return userService.getUserProfileByID(id);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @GetMapping(path = "email={email}")
    public UserProfile getUserProfileByEmail(@NotNull @PathVariable("email") String email, Principal principal) {
        if(principal.getName().equals(userService.getUserProfileByEmail(email).getUserAccount().getUsername())) {
            return userService.getUserProfileByEmail(email);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource hahaha.");
        }

    }

    @GetMapping(path = "phoneNumber={phoneNumber}")
    public UserProfile getUserProfileByPhoneNumber(@NotNull @PathVariable("phoneNumber") String phoneNumber, Principal principal) {
        if (principal.getName().equals(userService.getUserProfileByPhoneNumber(phoneNumber).getUserAccount().getUsername())) {
            return userService.getUserProfileByPhoneNumber(phoneNumber);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @DeleteMapping(path = "id={id}")
    public void deleteUserProfileById(@NotNull @PathVariable("id") UUID id, Principal principal) {
        if(principal.getName().equals(userService.getUserProfileByID(id).getUserAccount().getUsername())) {
            userService.deleteUserProfileByID(id);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @DeleteMapping(path = "email={email}")
    public void deleteUserProfileByEmail(@NotNull @PathVariable("email") String email, Principal principal) {
        if(principal.getName().equals(userService.getUserProfileByEmail(email).getUserAccount().getUsername())) {
            userService.deleteUserProfileByEmail(email);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @DeleteMapping(path = "phone={phoneNumber}")
    public void deleteUserProfileByPhoneNumber(@NotNull @PathVariable("phoneNumber") String phoneNumber, Principal principal) {
        if (principal.getName().equals(userService.getUserProfileByPhoneNumber(phoneNumber).getUserAccount().getUsername())) {
            userService.deleteUserProfileByPhoneNumber(phoneNumber);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @PutMapping(path = "update")
    public void updateUserProfile(
            @RequestParam (name = "newEmail") String newEmail,
            @RequestParam (name = "newPhoneNumber") String newPhoneNumber,
            @RequestParam (name = "newFirstName") String newFirstName,
            @RequestParam (name = "newLastName") String newLastName,
            @RequestParam (name = "newUsername") String newUsername,
            @RequestParam (name = "currentEmail") String currentEmail,
            Authentication authentication,
            Principal principal,
            HttpServletResponse response
    ) {
        if( userService.getUserProfileByEmail(currentEmail).getUserAccount().getUsername().equals(principal.getName()) ){
            userService.updateUserProfile(newEmail, newPhoneNumber, newFirstName, newLastName, newUsername, currentEmail);

            String subject;

            if (newUsername.equals("")) {
                subject = principal.getName();
            } else {
                subject = newUsername;
            }

            String token = Jwts.builder()
                    .setSubject(subject)
                    .claim("authorities", authentication.getAuthorities())
                    .setIssuedAt(new Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                    .signWith(secretKey)
                    .compact();

            response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);

        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }
}
