package com.spoctexter.occasions;

import com.spoctexter.exception.BadInputException;
import com.spoctexter.exception.NotFoundException;
import com.spoctexter.friends.Friend;
import com.spoctexter.friends.FriendRepository;
import com.spoctexter.inputvalidation.InputValidation;
import com.spoctexter.twilio.SmsRequest;
import com.spoctexter.twilio.SmsScheduler;
import com.spoctexter.twilio.TwilioSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class OccasionService {

    private final OccasionRepository occasionRepository;
    private final FriendRepository friendRepository;
    private final InputValidation inputValidator = new InputValidation();
    private final TwilioSender twilioSender;
    private final SmsScheduler smsScheduler;

    @Autowired
    public OccasionService(OccasionRepository occasionRepository,
                           FriendRepository friendRepository,
                           TwilioSender twilioSender,
                           SmsScheduler smsScheduler) {
        this.occasionRepository = occasionRepository;
        this.friendRepository = friendRepository;
        this.twilioSender = twilioSender;
        this.smsScheduler = smsScheduler;
    }

    public void addOccasion(UUID friendID, Occasion occasion) {

        Friend friend = inputValidator.checkFriend(friendID,friendRepository);
        occasion.setFriend(friend);
        occasionRepository.save(occasion);

        String phoneNumber = occasion.getFriend().getUserAccount().getUserProfile().getPhoneNumber();

        SmsRequest smsRequest = new SmsRequest(phoneNumber, occasion.getOccasionName()+ ": sent at " + LocalDate.now().toString());
        smsScheduler.setOccasion(occasion);
        smsScheduler.setSmsRequest(smsRequest);
        smsScheduler.sendSms();

    }

    public void removeOccasion(Long occasionID) {
        occasionRepository.delete(getOccasionByOccasionId(occasionID));
    }

    public Occasion getOccasionByOccasionId(Long occasionId) {
        return occasionRepository
                .findById(occasionId)
                .orElseThrow(
                        () -> {
                            NotFoundException notFoundException = new NotFoundException(
                                    "Occasion not found.");
                            return notFoundException;
                        }
                );
    }

    public List<Occasion> getOccasionsByFriendId(UUID friendId) {
        Friend friend = inputValidator.checkFriend(friendId, friendRepository);
        return friend.getOccasions();
    }

    @Transactional
    public void updateOccasion(Long occasionId, String occasionName, String occasionDate) {
        Occasion occasion = getOccasionByOccasionId(occasionId);
        if(!occasionName.equals("")) {
            occasion.setOccasionName(occasionName);
        }
        if(!occasionDate.equals("")){
            occasion.setOccasionDate(LocalDate.parse(occasionDate));
        }
        else {
            throw new BadInputException("No inputs for changing the occasion were provided");
        }
    }
}
