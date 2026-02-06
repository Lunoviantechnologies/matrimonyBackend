package com.example.matrimony.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.matrimony.entity.Country;
import com.example.matrimony.entity.State;
import com.example.matrimony.repository.CountryRepository;
import com.example.matrimony.repository.StateRepository;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class LocationDataLoader implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;

    public LocationDataLoader(
            CountryRepository countryRepository,
            StateRepository stateRepository) {

        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
    }

    @Override
    public void run(String... args) {

        // ========= INDIA =========
    	Country india = insertCountry("India");

    	// ------------------- 28 STATES -------------------
    	insertState(india, "Andhra Pradesh");
    	insertState(india, "Arunachal Pradesh");
    	insertState(india, "Assam");
    	insertState(india, "Bihar");
    	insertState(india, "Chhattisgarh");
    	insertState(india, "Goa");
    	insertState(india, "Gujarat");
    	insertState(india, "Haryana");
    	insertState(india, "Himachal Pradesh");
    	insertState(india, "Jharkhand");
    	insertState(india, "Karnataka");
    	insertState(india, "Kerala");
    	insertState(india, "Madhya Pradesh");
    	insertState(india, "Maharashtra");
    	insertState(india, "Manipur");
    	insertState(india, "Meghalaya");
    	insertState(india, "Mizoram");
    	insertState(india, "Nagaland");
    	insertState(india, "Odisha");
    	insertState(india, "Punjab");
    	insertState(india, "Rajasthan");
    	insertState(india, "Sikkim");
    	insertState(india, "Tamil Nadu");
    	insertState(india, "Telangana");
    	insertState(india, "Tripura");
    	insertState(india, "Uttar Pradesh");
    	insertState(india, "Uttarakhand");
    	insertState(india, "West Bengal");

    	// ------------------- 8 UNION TERRITORIES -------------------
    	insertState(india, "Andaman and Nicobar Islands");
    	insertState(india, "Chandigarh");
    	insertState(india, "Dadra and Nagar Haveli and Daman and Diu");
    	insertState(india, "Delhi");
    	insertState(india, "Jammu and Kashmir");
    	insertState(india, "Ladakh");
    	insertState(india, "Lakshadweep");
    	insertState(india, "Puducherry");


    	// ========= USA =========
    	Country usa = insertCountry("United States");

    	// ----------- 50 STATES -----------
    	insertState(usa, "Alabama");
    	insertState(usa, "Alaska");
    	insertState(usa, "Arizona");
    	insertState(usa, "Arkansas");
    	insertState(usa, "California");
    	insertState(usa, "Colorado");
    	insertState(usa, "Connecticut");
    	insertState(usa, "Delaware");
    	insertState(usa, "Florida");
    	insertState(usa, "Georgia");

    	insertState(usa, "Hawaii");
    	insertState(usa, "Idaho");
    	insertState(usa, "Illinois");
    	insertState(usa, "Indiana");
    	insertState(usa, "Iowa");
    	insertState(usa, "Kansas");
    	insertState(usa, "Kentucky");
    	insertState(usa, "Louisiana");
    	insertState(usa, "Maine");
    	insertState(usa, "Maryland");

    	insertState(usa, "Massachusetts");
    	insertState(usa, "Michigan");
    	insertState(usa, "Minnesota");
    	insertState(usa, "Mississippi");
    	insertState(usa, "Missouri");
    	insertState(usa, "Montana");
    	insertState(usa, "Nebraska");
    	insertState(usa, "Nevada");
    	insertState(usa, "New Hampshire");
    	insertState(usa, "New Jersey");

    	insertState(usa, "New Mexico");
    	insertState(usa, "New York");
    	insertState(usa, "North Carolina");
    	insertState(usa, "North Dakota");
    	insertState(usa, "Ohio");
    	insertState(usa, "Oklahoma");
    	insertState(usa, "Oregon");
    	insertState(usa, "Pennsylvania");
    	insertState(usa, "Rhode Island");
    	insertState(usa, "South Carolina");

    	insertState(usa, "South Dakota");
    	insertState(usa, "Tennessee");
    	insertState(usa, "Texas");
    	insertState(usa, "Utah");
    	insertState(usa, "Vermont");
    	insertState(usa, "Virginia");
    	insertState(usa, "Washington");
    	insertState(usa, "West Virginia");
    	insertState(usa, "Wisconsin");
    	insertState(usa, "Wyoming");

    	// ----------- FEDERAL DISTRICT -----------
    	insertState(usa, "District of Columbia");

    	// ----------- US TERRITORIES -----------
    	insertState(usa, "Puerto Rico");
    	insertState(usa, "Guam");
    	insertState(usa, "American Samoa");
    	insertState(usa, "U.S. Virgin Islands");
    	insertState(usa, "Northern Mariana Islands");

    	// ========= CANADA =========
    	Country canada = insertCountry("Canada");

    	// -------- Provinces (10) --------
    	insertState(canada, "Ontario");
    	insertState(canada, "Quebec");
    	insertState(canada, "British Columbia");
    	insertState(canada, "Alberta");
    	insertState(canada, "Manitoba");
    	insertState(canada, "Saskatchewan");
    	insertState(canada, "Nova Scotia");
    	insertState(canada, "New Brunswick");
    	insertState(canada, "Newfoundland and Labrador");
    	insertState(canada, "Prince Edward Island");

    	// -------- Territories (3) --------
    	insertState(canada, "Yukon");
    	insertState(canada, "Northwest Territories");
    	insertState(canada, "Nunavut");



//     // ========= UNITED KINGDOM =========
//        Country uk = insertCountry("United Kingdom");
//
//        insertState(uk, "England");
//        insertState(uk, "Scotland");
//        insertState(uk, "Wales");
//        insertState(uk, "Northern Ireland");
    	// ========= UNITED KINGDOM =========
    	Country uk = insertCountry("United Kingdom");

    	// -------- Countries (4) --------
    	insertState(uk, "England");
    	insertState(uk, "Scotland");
    	insertState(uk, "Wales");
    	insertState(uk, "Northern Ireland");

    	// -------- Crown Dependencies --------
    	insertState(uk, "Isle of Man");
    	insertState(uk, "Jersey");
    	insertState(uk, "Guernsey");

    	// -------- British Overseas Territories --------
    	insertState(uk, "Anguilla");
    	insertState(uk, "Bermuda");
    	insertState(uk, "British Antarctic Territory");
    	insertState(uk, "British Indian Ocean Territory");
    	insertState(uk, "British Virgin Islands");
    	insertState(uk, "Cayman Islands");
    	insertState(uk, "Falkland Islands");
    	insertState(uk, "Gibraltar");
    	insertState(uk, "Montserrat");
    	insertState(uk, "Pitcairn Islands");
    	insertState(uk, "Saint Helena, Ascension and Tristan da Cunha");
    	insertState(uk, "South Georgia and the South Sandwich Islands");
    	insertState(uk, "Sovereign Base Areas of Akrotiri and Dhekelia");
    	insertState(uk, "Turks and Caicos Islands");


    	// ========= AUSTRALIA =========
    	Country australia = insertCountry("Australia");

    	// -------- States (6) --------
    	insertState(australia, "New South Wales");
    	insertState(australia, "Victoria");
    	insertState(australia, "Queensland");
    	insertState(australia, "Western Australia");
    	insertState(australia, "South Australia");
    	insertState(australia, "Tasmania");

    	// -------- Territories (2) --------
    	insertState(australia, "Australian Capital Territory");
    	insertState(australia, "Northern Territory");


    }

    // ================= HELPERS =================

    private Country insertCountry(String name) {
        return countryRepository.findByName(name)
                .orElseGet(() -> {
                    Country c = new Country();
                    c.setName(name);
                    return countryRepository.save(c);
                });
    }

    private void insertState(Country country, String stateName) {
        stateRepository.findByNameAndCountry(stateName, country)
                .orElseGet(() -> {
                    State s = new State();
                    s.setName(stateName);
                    s.setCountry(country);
                    return stateRepository.save(s);
                });
    }
}






//package com.example.matrimony.config;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import com.example.matrimony.entity.City;
//import com.example.matrimony.entity.Country;
//import com.example.matrimony.entity.District;
//import com.example.matrimony.entity.State;
//import com.example.matrimony.repository.CityRepository;
//import com.example.matrimony.repository.CountryRepository;
//import com.example.matrimony.repository.DistrictRepository;
//import com.example.matrimony.repository.StateRepository;
//
//import jakarta.transaction.Transactional;
//
//@Component
//@Transactional
//public class LocationDataLoader implements CommandLineRunner {
//
//    private final CountryRepository countryRepository;
//    private final StateRepository stateRepository;
//    private final DistrictRepository districtRepository;
//    private final CityRepository cityRepository;
//
//    public LocationDataLoader(
//            CountryRepository countryRepository,
//            StateRepository stateRepository,
//            DistrictRepository districtRepository,
//            CityRepository cityRepository) {
//
//        this.countryRepository = countryRepository;
//        this.stateRepository = stateRepository;
//        this.districtRepository = districtRepository;
//        this.cityRepository = cityRepository;
//    }
//
//    @Override
//    public void run(String... args) {
//
////        // ========= INDIA =========
////        Country india = insertCountry("India");
////
////        insertStateWithDistrictsAndCities(
////                india,
////                "Kerala",
////                Map.of(
////                        "Ernakulam", List.of("Kochi", "Aluva"),
////                        "Kozhikode", List.of("Calicut", "Vadakara")
////                )
////        );
////
////        insertStateWithDistrictsAndCities(
////                india,
////                "Maharashtra",
////                Map.of(
////                        "Pune", List.of("Pune City", "Hinjewadi"),
////                        "Mumbai Suburban", List.of("Andheri", "Borivali")
////                )
////        );
//    	// ========= INDIA =========
//    	Country india = insertCountry("India");
//
//    	/* --------- STATES WITH DISTRICTS & CITIES --------- */
//
//    	// Kerala
//    	insertStateWithDistrictsAndCities(
//    	        india,
//    	        "Kerala",
//    	        Map.of(
//    	                "Ernakulam", List.of("Kochi", "Aluva"),
//    	                "Kozhikode", List.of("Calicut", "Vadakara")
//    	        )
//    	);
//
//    	// Maharashtra
//    	insertStateWithDistrictsAndCities(
//    	        india,
//    	        "Maharashtra",
//    	        Map.of(
//    	                "Pune", List.of("Pune City", "Hinjewadi"),
//    	                "Mumbai Suburban", List.of("Andheri", "Borivali")
//    	        )
//    	);
//
//    	/* --------- REMAINING STATES (STATE ONLY) --------- */
//
//    	insertStateOnly(india, "Andhra Pradesh");
//    	insertStateOnly(india, "Arunachal Pradesh");
//    	insertStateOnly(india, "Assam");
//    	insertStateOnly(india, "Bihar");
//    	insertStateOnly(india, "Chhattisgarh");
//    	insertStateOnly(india, "Goa");
//    	insertStateOnly(india, "Gujarat");
//    	insertStateOnly(india, "Haryana");
//    	insertStateOnly(india, "Himachal Pradesh");
//    	insertStateOnly(india, "Jharkhand");
//    	insertStateOnly(india, "Karnataka");
//    	insertStateOnly(india, "Madhya Pradesh");
//    	insertStateOnly(india, "Manipur");
//    	insertStateOnly(india, "Meghalaya");
//    	insertStateOnly(india, "Mizoram");
//    	insertStateOnly(india, "Nagaland");
//    	insertStateOnly(india, "Odisha");
//    	insertStateOnly(india, "Punjab");
//    	insertStateOnly(india, "Rajasthan");
//    	insertStateOnly(india, "Sikkim");
//    	insertStateOnly(india, "Tamil Nadu");
//    	insertStateOnly(india, "Telangana");
//    	insertStateOnly(india, "Tripura");
//    	insertStateOnly(india, "Uttar Pradesh");
//    	insertStateOnly(india, "Uttarakhand");
//    	insertStateOnly(india, "West Bengal");
//
//
//        // ========= USA =========
//        Country usa = insertCountry("United States");
//
//        insertStateWithDistrictsAndCities(
//                usa,
//                "California",
//                Map.of(
//                        "Los Angeles County", List.of("Los Angeles", "Santa Monica"),
//                        "San Francisco County", List.of("San Francisco", "Daly City")
//                )
//        );
//
//        insertStateWithDistrictsAndCities(
//                usa,
//                "Texas",
//                Map.of(
//                        "Harris County", List.of("Houston", "Pasadena"),
//                        "Dallas County", List.of("Dallas", "Irving")
//                )
//        );
//
//        // ========= CANADA =========
//        Country canada = insertCountry("Canada");
//
//        insertStateWithDistrictsAndCities(
//                canada,
//                "Ontario",
//                Map.of(
//                        "Toronto Division", List.of("Toronto", "North York"),
//                        "Ottawa Division", List.of("Ottawa", "Kanata")
//                )
//        );
//
//        insertStateWithDistrictsAndCities(
//                canada,
//                "British Columbia",
//                Map.of(
//                        "Greater Vancouver", List.of("Vancouver", "Richmond"),
//                        "Vancouver Island", List.of("Victoria", "Nanaimo")
//                )
//        );
//
//        // ========= UNITED KINGDOM =========
//        Country uk = insertCountry("United Kingdom");
//
//        insertStateWithDistrictsAndCities(
//                uk,
//                "England",
//                Map.of(
//                        "Greater London", List.of("London", "Croydon"),
//                        "West Midlands", List.of("Birmingham", "Coventry")
//                )
//        );
//
//        insertStateWithDistrictsAndCities(
//                uk,
//                "Scotland",
//                Map.of(
//                        "Glasgow City", List.of("Glasgow", "Partick"),
//                        "Edinburgh", List.of("Edinburgh", "Leith")
//                )
//        );
//
//        // ========= AUSTRALIA =========
//        Country australia = insertCountry("Australia");
//
//        insertStateWithDistrictsAndCities(
//                australia,
//                "New South Wales",
//                Map.of(
//                        "Sydney Region", List.of("Sydney", "Parramatta"),
//                        "Hunter Region", List.of("Newcastle", "Maitland")
//                )
//        );
//
//        insertStateWithDistrictsAndCities(
//                australia,
//                "Victoria",
//                Map.of(
//                        "Melbourne Metro", List.of("Melbourne", "St Kilda"),
//                        "Geelong Region", List.of("Geelong", "Torquay")
//                )
//        );
//    }
//
//    // ================= HELPERS =================
//
//    private Country insertCountry(String name) {
//        return countryRepository.findByName(name)
//                .orElseGet(() -> {
//                    Country c = new Country();
//                    c.setName(name);
//                    return countryRepository.save(c);
//                });
//    }
//
//    private void insertStateWithDistrictsAndCities(
//            Country country,
//            String stateName,
//            Map<String, List<String>> districtCityMap) {
//
//        State state = stateRepository.findByNameAndCountry(stateName, country)
//                .orElseGet(() -> {
//                    State s = new State();
//                    s.setName(stateName);
//                    s.setCountry(country);
//                    return stateRepository.save(s);
//                });
//
//        districtCityMap.forEach((districtName, cities) -> {
//
//            District district = districtRepository.findByNameAndState(districtName, state)
//                    .orElseGet(() -> {
//                        District d = new District();
//                        d.setName(districtName);
//                        d.setState(state);
//                        return districtRepository.save(d);
//                    });
//
//            for (String cityName : cities) {
//                if (!cityRepository.existsByNameAndDistrict(cityName, district)) {
//                    City city = new City();
//                    city.setName(cityName);
//                    city.setState(state);          // ✅ VERY IMPORTANT
//
//                    city.setDistrict(district);
//                    cityRepository.save(city);
//                }
//            }
//        });
//    }
//    private void insertStateOnly(Country country, String stateName) {
//        stateRepository.findByNameAndCountry(stateName, country)
//                .orElseGet(() -> {
//                    State s = new State();
//                    s.setName(stateName);
//                    s.setCountry(country);
//                    return stateRepository.save(s);
//                });
//    }
//
//}