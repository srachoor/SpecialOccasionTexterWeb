package com.spoctexter.twilio;

import com.spoctexter.occasions.Occasion;
import com.spoctexter.occasions.OccasionRepository;
import com.spoctexter.texts.Text;
import com.spoctexter.texts.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class SmsScheduler {

    private final OccasionRepository occasionRepository;
    private final TwilioSender twilioSender;
    private final TextService textService;

    @Autowired
    public SmsScheduler(OccasionRepository occasionRepository,
                        TwilioSender twilioSender,
                        TextService textService) {
        this.occasionRepository = occasionRepository;
        this.twilioSender = twilioSender;
        this.textService = textService;
    }

    @Scheduled(cron= "0 0/30 8-11 * * *")
    public void checkOccasions() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        System.out.println("month: " + month);
        System.out.println("day: "+  day);
        List<Occasion> todayOccasions = occasionRepository.findOccasionsByDate(month, day);

        for (Occasion occasion : todayOccasions) {

            Boolean wasSent = false;
            List<Text> sentTexts = occasion.getTexts();

            if(sentTexts.isEmpty()) {
                System.out.println("SentTexts is empty.");
            } else {
                innerLoop:
                for (Text sentText : sentTexts) {
                    if (sentText.getSentTime().getDayOfMonth() == day &&
                            sentText.getSentTime().getMonthValue() == month) {
                        wasSent = true;
                        break innerLoop;
                    }
                }
            }

            if(wasSent) {
                continue;
            } else {

                String message = "Reminder: Today is " + occasion
                        .getFriend().getFriendFirstName() + " " + occasion
                        .getFriend().getFriendLastName() + "'s " + occasion.getOccasionName() +
                        ". Make sure you text them at " + occasion.getFriend().getFriendPhoneNumber() + ".";

                String phoneNumber = occasion
                        .getFriend()
                        .getUserAccount()
                        .getUserProfile()
                        .getPhoneNumber();

                Text newText = new Text(LocalDateTime.now(),message,phoneNumber);

                SmsRequest smsRequest = new SmsRequest(phoneNumber, message);
                twilioSender.sendSms(smsRequest);

                textService.addText(occasion.getId(),newText);
            }
        }
    }
}
