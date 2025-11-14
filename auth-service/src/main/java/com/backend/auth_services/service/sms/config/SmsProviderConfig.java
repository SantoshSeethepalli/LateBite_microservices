package com.backend.auth_services.service.sms.config;

import com.backend.auth_services.service.sms.providers.SmsProvider;
import com.backend.auth_services.service.sms.providers.TwilioSmsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SmsProviderConfig {

    private final TwilioSmsProvider twilioSmsProvider;

    @Value("${sms.provider}")
    private String provider;

    @Bean
    public SmsProvider smsProvider() {

        if (provider.equalsIgnoreCase("twilio")) return twilioSmsProvider;

        return twilioSmsProvider;
    }
}
