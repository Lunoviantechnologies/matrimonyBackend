package com.example.matrimony.specification;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.dto.ProfileFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.*;

public class ProfileSpecification {

    public static Specification<Profile> filter(ProfileFilterRequest req) {

        return (root, query, cb) -> {
        	
        	
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);

            // ================= EXCLUDE MYSELF =================
            if (req.getMyId() != null) {
                predicates.add(cb.notEqual(root.get("id"), req.getMyId()));
            }

            // ================= ONLY APPROVED USERS =================
            predicates.add(cb.equal(root.get("approved"), true));
            predicates.add(cb.equal(root.get("banned"), false));

            predicates.add(
                cb.or(
                    cb.isNull(root.get("deleteRequested")),
                    cb.equal(root.get("deleteRequested"), false)
                )
            );

            // ================= GLOBAL SEARCH =================
            if (req.getSearch() != null && !req.getSearch().isBlank()) {

                String search = req.getSearch().toLowerCase().trim();
                String likeSearch = "%" + search + "%";

                List<Predicate> searchPredicates = new ArrayList<>();

                searchPredicates.add(cb.like(cb.lower(root.get("firstName")), likeSearch));
                searchPredicates.add(cb.like(cb.lower(root.get("lastName")), likeSearch));
                searchPredicates.add(cb.like(cb.lower(root.get("city")), likeSearch));
                searchPredicates.add(cb.like(cb.lower(root.get("state")), likeSearch));
                searchPredicates.add(cb.like(cb.lower(root.get("country")), likeSearch));
                searchPredicates.add(cb.like(cb.lower(root.get("occupation")), likeSearch));
                searchPredicates.add(cb.like(cb.lower(root.get("religion")), likeSearch));

                try {
                    Long idValue = Long.parseLong(search);
                    searchPredicates.add(cb.equal(root.get("id"), idValue));
                } catch (Exception ignored) {}

                predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
            }

            // ================= OPPOSITE GENDER ONLY =================
            if (req.getMyGender() != null && !req.getMyGender().isBlank()) {

                String myGender = req.getMyGender().trim().toLowerCase();

                if (myGender.equals("male")) {
                    predicates.add(cb.equal(cb.lower(root.get("gender")), "female"));
                } 
                else if (myGender.equals("female")) {
                    predicates.add(cb.equal(cb.lower(root.get("gender")), "male"));
                }
            }

            // ================= NEW MATCH (last 4 days) =================
            if (req.getCreatedAfter() != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        req.getCreatedAfter()
                    )
                );
            }


            // ================= NEAR MATCH (city) =================
            if (req.getCity() != null && !req.getCity().isBlank()) {
                predicates.add(
                    cb.equal(
                        cb.lower(root.get("city")),
                        req.getCity().toLowerCase()
                    )
                );
            }

            // ================= HIDE SENT / ACCEPTED / REJECTED =================
            if (req.getHiddenIds() != null && !req.getHiddenIds().isEmpty()) {
                predicates.add(cb.not(root.get("id").in(req.getHiddenIds())));
            }

            // ================= RELIGION =================
            if (req.getReligion() != null && !req.getReligion().isEmpty()) {
                predicates.add(root.get("religion").in(req.getReligion()));
            }

            // ================= COUNTRY =================
            if (req.getCountry() != null && !req.getCountry().isEmpty()) {
                predicates.add(root.get("country").in(req.getCountry()));
            }

            // ================= MARITAL =================
            if (req.getMaritalStatus() != null && !req.getMaritalStatus().isEmpty()) {
                predicates.add(root.get("maritalStatus").in(req.getMaritalStatus()));
            }

            // ================= EDUCATION =================
            if (req.getEducation() != null && !req.getEducation().isEmpty()) {
                predicates.add(root.get("highestEducation").in(req.getEducation()));
            }
         // ================= AGE FILTER =================
            if (req.getAge() != null && !req.getAge().isEmpty()) {

                List<Predicate> agePredicates = new ArrayList<>();

                for (String range : req.getAge()) {

                    if (range.equals("18-25"))
                        agePredicates.add(cb.between(root.get("age"), 18, 25));

                    else if (range.equals("26-30"))
                        agePredicates.add(cb.between(root.get("age"), 26, 30));

                    else if (range.equals("31-35"))
                        agePredicates.add(cb.between(root.get("age"), 31, 35));

                    else if (range.equals("36-40"))
                        agePredicates.add(cb.between(root.get("age"), 36, 40));

                    else if (range.equals("40+"))
                        agePredicates.add(cb.greaterThanOrEqualTo(root.get("age"), 40));
                }

                predicates.add(cb.or(agePredicates.toArray(new Predicate[0])));
            }
         // ================= HABBITS =================
            if (req.getHabbits() != null && !req.getHabbits().isEmpty()) {
                predicates.add(root.get("habbits").in(req.getHabbits()));
            }
         // ================= LIFESTYLE =================
            if (req.getLifestyle() != null && !req.getLifestyle().isEmpty()) {
                predicates.add(root.get("vegiterian").in(req.getLifestyle()));
            }
            
         // ================= PROFILE FOR =================
            if (req.getProfileFor() != null && !req.getProfileFor().isEmpty()) {
                predicates.add(root.get("profileFor").in(req.getProfileFor()));
            }

            // ================= PROFESSION =================
            if (req.getProfession() != null && !req.getProfession().isEmpty()) {
                predicates.add(root.get("occupation").in(req.getProfession()));
            }

         // ================= CASTE DROPDOWN =================
            if (req.getOtherValues() != null) {

                String caste = req.getOtherValues().get("caste");

                if (caste != null && !caste.isBlank()) {
                    predicates.add(cb.equal(root.get("subCaste"), caste));
                }

                if (req.getCustomCaste() != null && !req.getCustomCaste().isBlank()) {
                    predicates.add(cb.like(
                        cb.lower(root.get("subCaste")),
                        "%" + req.getCustomCaste().toLowerCase() + "%"
                    ));
                }
            }
            
         // ================= HEIGHT DROPDOWN =================
            if (req.getOtherValues() != null) {

                String height = req.getOtherValues().get("height");

                if (height != null && !height.isBlank()) {
                    predicates.add(cb.equal(root.get("height"), height));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}