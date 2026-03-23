package com.finalproject.safepickup.Service;

import com.finalproject.safepickup.Api.ApiException;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class TwilioVerifyService {

    @Value("${TWILIO_ACCOUNT_SID}")
    private String accountSid;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String authToken;

    @Value("${TWILIO_SERVICE_SID}")
    private String serviceSid;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendCode(String phoneNumber) {
        try {
            Verification.creator(serviceSid, phoneNumber, "sms").create();
        } catch (Exception e) {
            if (e.getMessage().contains("unverified ")) {
                throw new ApiException("this is an unverified number (you are using trail Twilio account");
            }
            else if (e.getMessage().contains("invalid")) {
                throw new ApiException("this is an invalid number");
            }
            else {
                throw new ApiException(e.getMessage());
            }
        }
    }

    public boolean verifyCode(String phoneNumber, String code) {
        VerificationCheck check = VerificationCheck.creator(serviceSid)
                .setTo(phoneNumber)
                .setCode(code)
                .create();
        return "approved".equals(check.getStatus());
    }
}