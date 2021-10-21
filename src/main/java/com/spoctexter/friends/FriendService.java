package com.spoctexter.friends;

import com.spoctexter.UserAccountLayer.UserAccount;
import com.spoctexter.UserAccountLayer.UserAccountRepository;
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

    @Autowired
    public FriendService(FriendRepository friendRepository, UserAccountRepository userAccountRepository) {
        this.friendRepository = friendRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public void addNewFriend(UserAccount userAccount, Friend friend) {
        UUID uuid = UUID.randomUUID();
        friend.setId(uuid);
        friend.setUserAccount(userAccount);
        friendRepository.save(friend);
    }

    public void deleteFriendById(UUID friendID) {
        Optional<Friend> optionalFriend = friendRepository.findById(friendID);
        if(optionalFriend.get() != null) {
            friendRepository.deleteById(friendID);
        }
    }

}
