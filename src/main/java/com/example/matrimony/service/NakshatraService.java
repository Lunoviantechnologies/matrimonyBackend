package com.example.matrimony.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.matrimony.entity.Nakshatra;
import com.example.matrimony.repository.NakshatraRepository;

@Service
public class NakshatraService {

    @Autowired
    private NakshatraRepository nakshatraRepository;

    public Nakshatra calculateNakshatra(LocalDate dob) {

        long days = ChronoUnit.DAYS.between(
                LocalDate.of(1900, 1, 1), dob);

        double moonLongitude = (days * 13.176396) % 360;

        int index = (int) (moonLongitude / 13.333) + 1;

        return nakshatraRepository.findBySequence(index)
                .orElseThrow(() ->
                        new RuntimeException("Nakshatra not found for index: " + index));
    }
}
