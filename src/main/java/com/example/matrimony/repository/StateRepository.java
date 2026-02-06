package com.example.matrimony.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.matrimony.entity.Country;
import com.example.matrimony.entity.State;

public interface StateRepository extends JpaRepository<State, Long> {
	List<State> findByCountryId(Long countryId);
	
	Optional<State> findByNameAndCountry(String name, Country country);

	
}