package com.spoctexter.UserProfileLayer;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/spoc")
public class UserController {

    private final UserProfileService userService;

    @Autowired
    public UserController(UserProfileService userService) {
        this.userService = userService;
    }

    @PostMapping
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



}
