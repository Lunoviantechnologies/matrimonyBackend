package com.example.matrimony.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "astro_number")

public class AstroNumber {
	

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String astroNumber;
	    @Column(nullable = false)
	    private String name;
	    @Column(nullable = false)
	    private String experience;
	    @Column(nullable = false)
	    private String price;
	    @Column(nullable = false)
	    private String languages;

	    // getters & setters
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getAstroNumber() {
	        return astroNumber;
	    }

	    public void setAstroNumber(String astroNumber) {
	        this.astroNumber = astroNumber;
	    }

		public synchronized String getName() {
			return name;
		}

		public synchronized void setName(String name) {
			this.name = name;
		}

		

		public synchronized String getExperience() {
			return experience;
		}

		public synchronized void setExperience(String experience) {
			this.experience = experience;
		}

		public synchronized String getPrice() {
			return price;
		}

		public synchronized void setPrice(String price) {
			this.price = price;
		}

		public synchronized String getLanguages() {
			return languages;
		}

		public synchronized void setLanguages(String languages) {
			this.languages = languages;
		}
	    
		
	}


