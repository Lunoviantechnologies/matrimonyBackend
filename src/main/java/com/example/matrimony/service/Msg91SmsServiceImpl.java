package com.example.matrimony.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Msg91SmsServiceImpl implements Msg91SmsService {

    private static final Logger log = LoggerFactory.getLogger(Msg91SmsServiceImpl.class);

    @Override
    public void sendSms(String to, String message) {
        // For now, just log. Replace with actual MSG91 API call.
        log.info("SMS sent to {}: {}", to, message);
    }
}
