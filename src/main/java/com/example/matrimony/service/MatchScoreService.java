package com.example.matrimony.service;

import org.springframework.stereotype.Service;

@Service
public class MatchScoreService {

    public int calculateScore(String n1, String n2) {

        if (n1.equals(n2)) {
            return 10;
        }

        if (isFriendly(n1, n2)) {
            return 7;
        }

        return 5;
    }

    private boolean isFriendly(String n1, String n2) {
        return (
            (n1.equals("Ashwini") && n2.equals("Bharani")) ||
            (n1.equals("Rohini") && n2.equals("Mrigashirsha")) ||
            (n1.equals("Pushya") && n2.equals("Ashlesha"))
        );
    }
}
