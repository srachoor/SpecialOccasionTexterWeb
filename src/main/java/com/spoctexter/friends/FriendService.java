package com.spoctexter.friends;

import com.spoctexter.UserAccountLayer.UserAccount;
import com.spoctexter.UserAccountLayer.UserAccountRepository;
import com.spoctexter.exception.NamingConflictException;
import com.spoctexter.exception.NotFoundException;
import com.spoctexter.inputvalidation.InputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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
        friend.setFriendId(uuid);

        Friend conflictFriend = inputValidator.checkFriendConflict(userAccount,friend,this.friendRepository);

        if(conflictFriend == null) {
            friend.setUserAccount(userAccount);
            friendRepository.save(friend);
        } else {
            throw new NamingConflictException("This phone number is already associated with another friend.");
        }
    }

    public void deleteFriendById(UUID friendID) {
        friendRepository.deleteById(inputValidator.checkFriend(friendID,this.friendRepository).getFriendId());
    }

    public void updateFriend(
            String newFriendPhoneNumber,
            String newFriendFirstName,
            String newFriendLastName,
            String newFriendDOB,
            UUID friendId
            ) {

        if(!newFriendPhoneNumber.equals("")) {
            this.updateFriendPhoneNumber(newFriendPhoneNumber, friendId);
        }
        if(!newFriendFirstName.equals("")) {
            this.updateFriendFirstName(newFriendFirstName, friendId);
        }
        if(!newFriendDOB.equals("")) {
            this.updateFriendDOB(LocalDate.parse(newFriendDOB),friendId);
        }
        if(!newFriendLastName.equals("")) {
            this.updateFriendLastName(newFriendLastName, friendId);
        }

    }

    @Transactional
    public void updateFriendPhoneNumber(String newPhoneNumber, UUID friendId) {
        Friend friend = inputValidator.checkFriend(friendId, this.friendRepository);
        friend.setFriendPhoneNumber(newPhoneNumber);
        friendRepository.save(friend);
    }

    @Transactional
    public void updateFriendFirstName(String newFirstName, UUID friendId) {
        Friend friend = inputValidator.checkFriend(friendId, this.friendRepository);
        friend.setFriendFirstName(newFirstName);
        friendRepository.save(friend);
    }

    @Transactional
    public void updateFriendLastName(String newLastName, UUID friendId) {
        Friend friend = inputValidator.checkFriend(friendId, this.friendRepository);
        friend.setFriendLastName(newLastName);
        friendRepository.save(friend);
    }

    @Transactional
    public void updateFriendDOB(LocalDate newFriendDOB, UUID friendId) {
        Friend friend = inputValidator.checkFriend(friendId, this.friendRepository);
        friend.setFriendDOB(newFriendDOB);
        friendRepository.save(friend);
    }

    public Friend getFriendById(UUID friendId) {
        return inputValidator.checkFriend(friendId, this.friendRepository);
    }

    public List<Friend> getFriendsByUser(UUID userProfileId) {
            UserAccount userAccount = this.userAccountRepository
                    .findById(userProfileId)
                    .orElseThrow(
                            () -> {
                                NotFoundException notFoundException = new NotFoundException(
                                        "User with id " + userProfileId + " not found");
                                return notFoundException;
                            }
                    );
            return userAccount.getFriends();
    }
}
