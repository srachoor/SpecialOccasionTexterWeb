package com.spoctexter.friends;

import com.spoctexter.userAccount.UserAccount;
import com.spoctexter.userAccount.UserAccountService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
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
    public void addNewFriend(
            @NotNull @RequestParam(name = "username") String userName,
            @NotNull @Valid @RequestBody Friend friend
    ) {
        UserAccount userAccount = userAccountService.getUserAccountByUserName(userName);
        friendService.addNewFriend(userAccount, friend);
    }

    @GetMapping
    public Friend getFriendById(
            @NotNull @RequestParam(name = "friendId") UUID friendId
    ) {
        return friendService.getFriendById(friendId);
    }

    @GetMapping(path = "all")
    public List <Friend> getFriendsByUser(
            @RequestParam (name = "userName") String userName
    ){
        return friendService.getFriendsByUser(userName);
    }

    @DeleteMapping(path="delete")
    public void deleteFriend(
            @NotNull @RequestParam(name = "friendId") UUID id
    ) {
        friendService.deleteFriendById(id);
    }

    @PutMapping(path="update")
    public void updateFriend(
            @RequestParam (name="newFriendFirstName") String newFriendFirstName,
            @RequestParam (name="newFriendPhoneNumber") String newFriendPhoneNumber,
            @RequestParam (name="newFriendLastName") String newFriendLastName,
            @RequestParam (name="newFriendDOB") String newFriendDOB,
            @NotNull @RequestParam(name = "friendId") UUID friendId
            ) {

        friendService.updateFriend(newFriendPhoneNumber, newFriendFirstName, newFriendLastName, newFriendDOB, friendId);
    }

}
