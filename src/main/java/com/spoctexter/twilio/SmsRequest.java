package com.spoctexter.twilio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SmsRequest {

    @NotBlank
    private final String phoneNumber;

    @NotBlank
    private final String message;

    public SmsRequest(@JsonProperty(value = "phoneNumber") String phoneNumber,
                      @JsonProperty(value = "message") String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

}
