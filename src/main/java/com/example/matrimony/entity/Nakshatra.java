package com.example.matrimony.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "nakshatras")
public class Nakshatra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private int sequence;        // 1 – 27
    private String gana;         // Deva / Manushya / Rakshasa
    private String nadi;         // Aadi / Madhya / Antya
    private String yoni;         // Horse, Elephant, etc.
    private String rashi;        // Aries, Taurus...
    private String lord;         // Mars, Venus...
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getGana() {
		return gana;
	}
	public void setGana(String gana) {
		this.gana = gana;
	}
	public String getNadi() {
		return nadi;
	}
	public void setNadi(String nadi) {
		this.nadi = nadi;
	}
	public String getYoni() {
		return yoni;
	}
	public void setYoni(String yoni) {
		this.yoni = yoni;
	}
	public String getRashi() {
		return rashi;
	}
	public void setRashi(String rashi) {
		this.rashi = rashi;
	}
	public String getLord() {
		return lord;
	}
	public void setLord(String lord) {
		this.lord = lord;
	}

    
}
