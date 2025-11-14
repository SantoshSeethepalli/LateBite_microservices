package com.backend.auth_services.service.sms;

import com.backend.auth_services.service.sms.providers.SmsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsProvider smsProvider;

    public void sendSms(String to, String body) {
        smsProvider.sendSms(to, body);
    }
}
