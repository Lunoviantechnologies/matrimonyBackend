package com.example.matrimony.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.matrimony.repository.NakshatraRepository;
import com.example.util.NakshatraMasterData;

@Component
public class NakshatraDataInitializer implements CommandLineRunner {

    @Autowired
    private NakshatraRepository repository;

    @Override
    public void run(String... args) {

        if (repository.count() == 27) {
            return; // already loaded
        }

        repository.deleteAll();
        repository.saveAll(NakshatraMasterData.getAll());

        System.out.println("✅ Nakshatra master data loaded into DB");
    }
}
