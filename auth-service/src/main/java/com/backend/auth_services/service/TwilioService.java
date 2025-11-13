package com.backend.auth_services.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwilioService {

    @Value("${twilio.account-sid}")
    private String sid;

    @Value("${twilio.auth-token}")
    private String token;

    @Value("${twilio.from-number}")
    private String from;

    public void sendSms(String to, String body) {
        Twilio.init(sid, token);
        Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(from),
                body
        ).create();
    }
}
