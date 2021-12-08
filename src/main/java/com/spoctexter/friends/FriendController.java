package com.spoctexter.friends;

import com.spoctexter.exception.ForbiddenAccessException;
import com.spoctexter.userAccount.UserAccount;
import com.spoctexter.userAccount.UserAccountService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/spoc/account/friend")
public class FriendController {

    private final UserAccountService userAccountService;
    private final FriendService friendService;

    @Autowired
    public FriendController(UserAccountService userAccountService, FriendService friendService) {
        this.userAccountService = userAccountService;
        this.friendService = friendService;
    }

    @PostMapping(path="add")
    @PreAuthorize(value = "authentication.principal.equals(#username)")
    public void addNewFriend(
            @NotNull @RequestParam(name = "username") String username,
            @NotNull @Valid @RequestBody Friend friend
    ) {
        UserAccount userAccount = userAccountService.getUserAccountByUserName(username);
        friendService.addNewFriend(userAccount, friend);
    }

    @GetMapping
    public Friend getFriendById(
            @NotNull @RequestParam(name = "friendId") UUID friendId,
            Principal principal
    ) {
        if (friendService.getFriendById(friendId).getUserAccount().getUsername().equals(principal.getName())) {
            return friendService.getFriendById(friendId);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @GetMapping(path = "all")
    @PreAuthorize(value = "authentication.principal.equals(#username)")
    public List <Friend> getFriendsByUser(
            @RequestParam (name = "username") String username
    ){
        return friendService.getFriendsByUser(username);
    }

    @DeleteMapping(path="delete")
    public void deleteFriend(
            @NotNull @RequestParam(name = "friendId") UUID id,
            Principal principal
    ) {
        if (friendService.getFriendById(id).getUserAccount().getUsername().equals(principal.getName())) {
            friendService.deleteFriendById(id);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

    @PutMapping(path="update")
    public void updateFriend(
            @RequestParam (name="newFriendFirstName") String newFriendFirstName,
            @RequestParam (name="newFriendPhoneNumber") String newFriendPhoneNumber,
            @RequestParam (name="newFriendLastName") String newFriendLastName,
            @RequestParam (name="newFriendDOB") String newFriendDOB,
            @NotNull @RequestParam(name = "friendId") UUID friendId,
            Principal principal
            ) {

        if (friendService.getFriendById(friendId).getUserAccount().getUsername().equals(principal.getName())) {
            friendService.updateFriend(newFriendPhoneNumber, newFriendFirstName, newFriendLastName, newFriendDOB, friendId);
        } else {
            throw new ForbiddenAccessException("You do not have access to this resource.");
        }
    }

}
