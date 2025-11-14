package com.backend.auth_services.service.sms.providers;

public interface SmsProvider {

    void sendSms(String to, String body);
}
