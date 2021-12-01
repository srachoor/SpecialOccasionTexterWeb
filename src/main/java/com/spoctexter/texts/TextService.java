package com.spoctexter.texts;

import com.spoctexter.friends.Friend;
import com.spoctexter.friends.FriendService;
import com.spoctexter.occasions.Occasion;
import com.spoctexter.occasions.OccasionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TextService {

    private final TextRepository textRepository;
    private final FriendService friendService;
    private final OccasionService occasionService;

    @Autowired
    public TextService(TextRepository textRepository,
                       FriendService friendService,
                       OccasionService occasionService) {
        this.textRepository = textRepository;
        this.friendService = friendService;
        this.occasionService = occasionService;
    }

    public void addText(Long occasionId, Text text) {

        //Validation completed by occasionService
        Occasion occasion = occasionService.getOccasionByOccasionId(occasionId);
        text.setOccasion(occasion);
        textRepository.save(text);

    }

    public List<Text> getTexts(Long occasionId) {

        //Validation completed by occasionService
        Occasion occasion = occasionService.getOccasionByOccasionId(occasionId);
        return occasion.getTexts();
    }

    public List<Text> getTextsByUser(UUID accountId){

        List<Text> texts = new ArrayList<>();

        //Validation completed by friendService
        List<Friend> friends = friendService.getFriendsByUserId(accountId);

        for(Friend friend : friends) {
            List<Occasion> occasions = occasionService.getOccasionsByFriendId(friend.getFriendId());

            for(Occasion occasion : occasions) {
                List<Text> currentTexts = occasion.getTexts();

                for(Text text : currentTexts) {
                    texts.add(text);
                }
            }
        }

        return texts;
    }

}
