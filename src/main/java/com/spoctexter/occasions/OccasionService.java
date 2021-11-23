package com.spoctexter.occasions;

import com.spoctexter.exception.BadInputException;
import com.spoctexter.exception.NotFoundException;
import com.spoctexter.friends.Friend;
import com.spoctexter.friends.FriendRepository;
import com.spoctexter.inputvalidation.InputValidation;
import com.spoctexter.twilio.*;
import com.twilio.twiml.voice.Sms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OccasionService {

    private final OccasionRepository occasionRepository;
    private final FriendRepository friendRepository;
    private final InputValidation inputValidator = new InputValidation();
    private final TwilioSender twilioSender;
    private final SchedulerController schedulerController;
    private final SmsSchedulerRepository smsSchedulerRepository;

    @Autowired
    public OccasionService(OccasionRepository occasionRepository,
                           FriendRepository friendRepository,
                           TwilioSender twilioSender,
                           SchedulerController schedulerController,
                           SmsSchedulerRepository smsSchedulerRepository) {
        this.occasionRepository = occasionRepository;
        this.friendRepository = friendRepository;
        this.twilioSender = twilioSender;
        this.schedulerController = schedulerController;
        this.smsSchedulerRepository = smsSchedulerRepository;
    }

    public void addOccasion(UUID friendID, Occasion occasion) {

        Friend friend = inputValidator.checkFriend(friendID,friendRepository);
        occasion.setFriend(friend);
        occasionRepository.save(occasion);

        String phoneNumber = occasion.getFriend().getUserAccount().getUserProfile().getPhoneNumber();

        SmsScheduler smsScheduler = new SmsScheduler(this.twilioSender);
        SmsRequest smsRequest = new SmsRequest(phoneNumber, occasion.getOccasionName()+ ": sent at " + LocalDateTime.now().toString());
        smsScheduler.setOccasion(occasion);
        smsScheduler.setSmsRequest(smsRequest);

        smsSchedulerRepository.save(smsScheduler);
        SmsScheduler smsScheduler1 = smsSchedulerRepository.findById(occasion.getId()).get();
        System.out.println(smsScheduler1.getOccasion().getId());

        schedulerController.startSchedule(smsSchedulerRepository.findById(occasion.getId()).get());
    }

    public void removeOccasion(Long occasionID) {
        schedulerController.stopSchedule(smsSchedulerRepository.findById(occasionID).get());
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
