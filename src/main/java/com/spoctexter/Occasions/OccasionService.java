package com.spoctexter.Occasions;

import com.spoctexter.friends.Friend;
import com.spoctexter.friends.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OccasionService {

    private final OccasionRepository occasionRepository;
    private final FriendRepository friendRepository;

    @Autowired
    public OccasionService(OccasionRepository occasionRepository, FriendRepository friendRepository) {
        this.occasionRepository = occasionRepository;
        this.friendRepository = friendRepository;
    }

    public void addOccasion(UUID friendID, Occasion occasion) {
        Optional<Friend> optionalFriend = friendRepository.findById(friendID);
        if(optionalFriend.get() != null) {
            Friend friend = optionalFriend.get();
            occasion.setFriend(friend);
        }
    }

}
