package com.spoctexter.UserProfileLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/spoc")
public class UserController {

    private final UserProfileService userService;

    @Autowired
    public UserController(UserProfileService userService) {
        this.userService = userService;
    }



}
