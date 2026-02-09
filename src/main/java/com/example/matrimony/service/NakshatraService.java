package com.example.matrimony.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.matrimony.cache.NakshatraCache;
import com.example.matrimony.entity.Nakshatra;

@Service
public class NakshatraService {

    @Autowired
    private NakshatraCache nakshatraCache;

    public Nakshatra calculateNakshatra(LocalDate dob) {
    	
    	 if (dob == null) {
    	        throw new RuntimeException("DOB is null, cannot calculate Nakshatra");
    	    }

        int day = dob.getDayOfMonth(); // 1 to 31
        int month = dob.getMonthValue(); // 1 to 12

        int index = (day + month) % 27;

        // IMPORTANT: Ensure index between 1 to 27
        if (index == 0) {
            index = 27;
        }

        return nakshatraCache.getBySequence(index);
    }
}
