package com.spoctexter.twilio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spoctexter.occasions.Occasion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;

@Entity (name = "SmsScheduler")
@Table
@EnableScheduling
public class SmsScheduler {

    @Id
    @Column(unique = true, updatable = false, nullable = false)
    private long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonManagedReference
    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id")
    @MapsId
    private Occasion occasion;

    @Transient
    private SmsRequest smsRequest;

    @Transient
    @JsonIgnore
    private TwilioSender twilioSender;

    public SmsScheduler(TwilioSender twilioSender) {
        this.twilioSender = twilioSender;
    }

    public SmsScheduler() {
    }

    @Scheduled(cron= "00 23 13 23 11 ?")
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

    @Override
    public String toString() {
        return "SmsScheduler{" +
                "id=" + id +
                ", occasion=" + occasion +
                ", smsRequest=" + smsRequest +
                '}';
    }
}
