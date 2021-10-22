package com.spoctexter.friends;

import com.spoctexter.UserAccountLayer.UserAccount;
import com.spoctexter.UserAccountLayer.UserAccountRepository;
import com.spoctexter.exception.NamingConflictException;
import com.spoctexter.inputvalidation.InputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserAccountRepository userAccountRepository;
    private final InputValidation inputValidator = new InputValidation();

    @Autowired
    public FriendService(FriendRepository friendRepository, UserAccountRepository userAccountRepository) {
        this.friendRepository = friendRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public void addNewFriend(UserAccount userAccount, Friend friend) {
        UUID uuid = UUID.randomUUID();
        friend.setId(uuid);

        Friend conflictFriend = inputValidator.checkFriendConflict(userAccount,friend,this.friendRepository);

        if(conflictFriend == null) {
            friend.setUserAccount(userAccount);
            friendRepository.save(friend);
        } else {
            throw new NamingConflictException("This phone number is already associated with another friend.");
        }
    }

    public void deleteFriendById(UUID friendID) {
        friendRepository.deleteById(inputValidator.checkFriend(friendID,this.friendRepository).getId());
    }

    public void updateFriend(
            String newFriendPhoneNumber,
            String newFriendFirstName,
            String newFriendLastName,
            UUID friendId
            ) {

        if(newFriendPhoneNumber != "") {
            this.updateFriendPhoneNumber(newFriendPhoneNumber, friendId);
        }
        if(newFriendFirstName != "") {
            this.updateFriendFirstName(newFriendFirstName, friendId);
        }
        if(newFriendLastName != "") {
            this.updateFriendLastName(newFriendLastName, friendId);
        }

    }

    public void updateFriendPhoneNumber(String newPhoneNumber, UUID friendId) {
        Friend friend = inputValidator.checkFriend(friendId, this.friendRepository);
        friend.setFriendPhoneNumber(newPhoneNumber);
        friendRepository.save(friend);
    }

    public void updateFriendFirstName(String newFirstName, UUID friendId) {
        Friend friend = inputValidator.checkFriend(friendId, this.friendRepository);
        friend.setFriendFirstName(newFirstName);
        friendRepository.save(friend);
    }

    public void updateFriendLastName(String newLastName, UUID friendId) {
        Friend friend = inputValidator.checkFriend(friendId, this.friendRepository);
        friend.setFriendLastName(newLastName);
        friendRepository.save(friend);
    }

}
