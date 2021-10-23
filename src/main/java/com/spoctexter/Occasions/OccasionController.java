package com.spoctexter.Occasions;

import com.spoctexter.friends.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/spoc/account/friend/occasion")
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
            @NotNull @RequestParam(name = "friendId") UUID friendId,
            @NotNull @RequestBody @Valid Occasion occasion
    ) {
        occasionService.addOccasion(friendId, occasion);
    }

    @GetMapping
    public Occasion getOccasionByOccasionId(
            @NotNull @RequestParam(name = "occasionId") Long occasionId
    ) {
        return occasionService.getOccasionByOccasionId(occasionId);
    }

    @GetMapping (path = "all")
    public List<Occasion> getOccasionsByFriendId(
            @NotNull @RequestParam(name = "friendId") UUID friendId
    ) {
        return occasionService.getOccasionsByFriendId(friendId);
    }

    @DeleteMapping
    public void removeOccasion(
            @NotNull @RequestParam (name = "occasionId") Long occasionId
    ) {
        occasionService.removeOccasion(occasionId);
    }

    @PutMapping
    public void updateOccasion(
            @NotNull @RequestParam (name = "occasionId") Long occasionId,
            @RequestParam (name = "occasionName") String occasionName,
            @RequestParam (name = "occasionDate") String occasionDate
    ) {
        occasionService.updateOccasion(occasionId, occasionName, occasionDate);
    }

    //Need to add putMapping for editing an occasion (need to add the occasion date field as well)
}
