package com.example.matrimony.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.matrimony.entity.Nakshatra;
import com.example.util.NakshatraMasterData;

import jakarta.annotation.PostConstruct;

@Component
public class NakshatraCache {

    private final Map<Integer, Nakshatra> cache = new HashMap<>();

    @PostConstruct
    public void load() {
        for (Nakshatra n : NakshatraMasterData.getAll()) {
            cache.put(n.getSequence(), n);
        }
    }

    public Nakshatra getBySequence(int seq) {
        Nakshatra n = cache.get(seq);
        if (n == null) {
            throw new RuntimeException("Nakshatra not found in cache: " + seq);
        }
        return n;
    }
}
