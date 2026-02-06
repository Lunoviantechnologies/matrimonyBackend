package com.example.matrimony.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.entity.AstrologyMatch;
import com.example.matrimony.service.AstrologyMatchService;

@RestController
@RequestMapping("/api/astrology")
public class AstrologyController {

	 @Autowired
	    private AstrologyMatchService service;

	    @PostMapping("/match/{id1}/{id2}")
	    public AstrologyMatch match(
	            @PathVariable Long id1,
	            @PathVariable Long id2) {
	        return service.match(id1, id2);
	    }
  
}
