package com.example.matrimony.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.matrimony.dto.CountryDto;
import com.example.matrimony.dto.StateDto;
import com.example.matrimony.repository.CountryRepository;
import com.example.matrimony.repository.StateRepository;
@RestController
@RequestMapping("/api/locations")
@CrossOrigin("*")
public class LocationController {

    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;

    public LocationController(CountryRepository countryRepository,
                              StateRepository stateRepository) {
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
    }

    @GetMapping("/countries")
    public List<CountryDto> getCountries() {
        return countryRepository.findAll()
                .stream()
                .map(c -> new CountryDto(c.getId(), c.getName()))
                .toList();
    }

    @GetMapping("/states/{countryId}")
    public List<StateDto> getStates(@PathVariable Long countryId) {
        return stateRepository.findByCountryId(countryId)
                .stream()
                .map(s -> new StateDto(s.getId(), s.getName()))
                .toList();
    }
}