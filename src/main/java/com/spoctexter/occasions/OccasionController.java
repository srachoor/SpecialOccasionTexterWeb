package com.spoctexter.occasions;

import com.spoctexter.exception.ForbiddenAccessException;
import com.spoctexter.friends.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDate;
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
            @NotNull @RequestBody @Valid Occasion occasion,
            Principal principal
    ) {
        if(principal.getName().equals(friendService.getFriendById(friendId).getUserAccount().getUsername())) {
            occasionService.addOccasion(friendId, occasion);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @GetMapping
    public Occasion getOccasionByOccasionId(
            @NotNull @RequestParam(name = "occasionId") Long occasionId,
            Principal principal
    ) {
        if (principal.getName().equals(occasionService.getOccasionByOccasionId(occasionId).getFriend().getUserAccount().getUsername())) {
            return occasionService.getOccasionByOccasionId(occasionId);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @GetMapping (path = "all")
    public List<Occasion> getOccasionsByFriendId(
            @NotNull @RequestParam(name = "friendId") UUID friendId,
            Principal principal
    ) {
        if(principal.getName().equals(friendService.getFriendById(friendId).getUserAccount().getUsername())) {
            return occasionService.getOccasionsByFriendId(friendId);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @DeleteMapping
    public void removeOccasion(
            @NotNull @RequestParam (name = "occasionId") Long occasionId,
            Principal principal
    ) {
        if (principal.getName().equals(occasionService.getOccasionByOccasionId(occasionId).getFriend().getUserAccount().getUsername())) {
            occasionService.removeOccasion(occasionId);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @PutMapping
    public void updateOccasion(
            @NotNull @RequestParam (name = "occasionId") Long occasionId,
            @RequestParam (name = "occasionName") String occasionName,
            @RequestParam (name = "occasionDate") String occasionDate,
            Principal principal
    ) {
        if (principal.getName().equals(occasionService.getOccasionByOccasionId(occasionId).getFriend().getUserAccount().getUsername())) {
            occasionService.updateOccasion(occasionId, occasionName, LocalDate.parse(occasionDate));
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }
}
