package com.example.matrimony.controller;

import com.example.matrimony.dto.ProfileFilterRequest;
import com.example.matrimony.dto.ProfileMatchResponse;
import com.example.matrimony.service.MatchService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class MatchController {

    private final MatchService matchService;
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/matches")
    public Page<ProfileMatchResponse> matches(
            @RequestBody ProfileFilterRequest request
    ){
        return matchService.getMatches(request);
    }
}