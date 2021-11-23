package com.spoctexter.twilio;

import com.spoctexter.occasions.Occasion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SmsScheduler {

    private Occasion occasion;
    private SmsRequest smsRequest;
    private TwilioSender twilioSender;

    @Autowired
    public void SmsScheduler(TwilioSender twilioSender) {
        this.twilioSender = twilioSender;
    }


    @Scheduled(fixedDelay = 30000L)
    public void sendSms() {
        twilioSender.sendSms(smsRequest);
    }

    public Occasion getOccasion() {
        return occasion;
    }

    public void setOccasion(Occasion occasion) {
        this.occasion = occasion;
    }

    public SmsRequest getSmsRequest() {
        return smsRequest;
    }

    public void setSmsRequest(SmsRequest smsRequest) {
        this.smsRequest = smsRequest;
    }
}
