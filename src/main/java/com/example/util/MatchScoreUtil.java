package com.example.util;

import com.example.matrimony.entity.Profile;

public class MatchScoreUtil {

    public static int calculate(Profile me, Profile other) {

        int score = 0;

        if (equals(me.getReligion(), other.getReligion())) score += 20;
        if (equals(me.getSubCaste(), other.getSubCaste())) score += 15;
        if (equals(me.getHighestEducation(), other.getHighestEducation())) score += 15;
        if (equals(me.getSector(), other.getSector())) score += 15;
        if (equals(me.getCountry(), other.getCountry())) score += 15;
        if (equals(me.getMaritalStatus(), other.getMaritalStatus())) score += 10;
        if (equals(me.getHabbits(), other.getHabbits())) score += 5;

        if (hobbyMatch(me.getHobbies(), other.getHobbies())) score += 5;

        return score;
    }

    private static boolean equals(String a, String b) {
        if (a == null || b == null) return false;
        return a.trim().equalsIgnoreCase(b.trim());
    }

    private static boolean hobbyMatch(String a, String b) {
        if (a == null || b == null) return false;

        String[] arr1 = a.toLowerCase().split(",");
        String[] arr2 = b.toLowerCase().split(",");

        for (String h1 : arr1) {
            for (String h2 : arr2) {
                if (h1.trim().equals(h2.trim())) return true;
            }
        }
        return false;
    }
}