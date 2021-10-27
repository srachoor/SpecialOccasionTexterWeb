package com.spoctexter.twilioAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v1/twilio")
public class TwilioController {

    public final TwilioSender twilioSender;

    @Autowired
    public TwilioController(TwilioSender twilioSender){
        this.twilioSender = twilioSender;
    }

    @PostMapping
    public void sendSMS(@NotNull @RequestBody @Valid SmsRequest smsRequest) {
        twilioSender.sendSms(smsRequest);
    }


}
