package com.example.matrimony.referral;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.service.ProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/referrals")
@CrossOrigin("*")
public class ReferralController {

    private final ReferralService referralService;
    private final ProfileService profileService;

    @Value("${app.client.base-url:https://yourappdomain.com}")
    private String clientBaseUrl;

    public ReferralController(ReferralService referralService,
                              ProfileService profileService) {
        this.referralService = referralService;
        this.profileService = profileService;
    }

    public static class UseCodeRequest {
        private String referralCode;

        public String getReferralCode() {
            return referralCode;
        }

        public void setReferralCode(String referralCode) {
            this.referralCode = referralCode;
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ReferralSummaryDto> getMyReferralSummary(Authentication authentication) {
        String email = authentication.getName();
        Profile profile = profileService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        ReferralSummaryDto dto = referralService.getSummary(profile, clientBaseUrl);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/use-code")
    public ResponseEntity<ReferralSummaryDto> useReferralCode(@RequestBody UseCodeRequest request,
                                             Authentication authentication) {
        String email = authentication.getName();
        Profile referredUser = profileService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        referralService.applyReferralCode(referredUser, request.getReferralCode());
        return ResponseEntity.ok().build();
    }
    
}

