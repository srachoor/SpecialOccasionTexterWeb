package com.spoctexter.twilio;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twilio")
@Data
@NoArgsConstructor
public class TwilioConfiguration {
    private String accountSid;
    private String authToken;
    private String trialNumber;
}
