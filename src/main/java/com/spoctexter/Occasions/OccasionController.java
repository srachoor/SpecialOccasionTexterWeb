package com.spoctexter.Occasions;

import com.spoctexter.UserAccountLayer.UserAccountService;
import com.spoctexter.friends.FriendController;
import com.spoctexter.friends.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/spoc/account/friend")
public class OccasionController {

    private final FriendService friendService;
    private final OccasionService occasionService;

    @Autowired
    public OccasionController(FriendService friendService, OccasionService occasionService) {
        this.friendService = friendService;
        this.occasionService = occasionService;
    }

    @PostMapping
    public void addOccasion(
            @NotNull @RequestParam UUID friendId,
            @NotNull @RequestBody @Valid Occasion occasion
    ) {
        occasionService.addOccasion(friendId, occasion);
    }

}