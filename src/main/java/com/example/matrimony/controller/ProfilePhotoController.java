package com.example.matrimony.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.matrimony.entity.Profilepicture;
import com.example.matrimony.service.ProfilePhotoService;

@RestController
@RequestMapping("/api/profile-photos")
@CrossOrigin("*")
public class ProfilePhotoController {

    private final ProfilePhotoService profilePhotoService;

    public ProfilePhotoController(ProfilePhotoService profilePhotoService) {
        this.profilePhotoService = profilePhotoService;
    }
    
 // ================= Upload Main Photo (index 0) =================
    @PutMapping("/updatePhoto/{profileId}")
    public ResponseEntity<?> uploadMainPhoto(
            @PathVariable Long profileId,
            @RequestParam("file") MultipartFile file) {

        try {

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is required");
            }

            long maxSize = 1 * 1024 * 1024;
            if (file.getSize() > maxSize) {
                return ResponseEntity
                        .status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File too large! Max allowed is 1MB");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity
                        .badRequest()
                        .body("Only image files are allowed");
            }

            Profilepicture saved = profilePhotoService.uploadPhoto(profileId, 0, file);

            return ResponseEntity.ok(Map.of(
                    "message", "Main photo uploaded successfully",
                    "fileName", saved.getFileName(),
                    "photoUrl", "/profile-photos/" + saved.getFileName()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to upload photo");
        }
    }

    // ================= Upload Photo 1 =================
    @PutMapping("/updatePhoto1/{profileId}")
    public ResponseEntity<?> uploadPhoto1(
            @PathVariable Long profileId,
            @RequestParam("file") MultipartFile file) {

        try {
            // ================= FILE VALIDATION =================

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is required");
            }

            //  1MB limit
            long maxSize = 1 * 1024 * 1024; // 1MB
            if (file.getSize() > maxSize) {
                return ResponseEntity
                        .status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File too large! Max allowed is 1MB");
            }

            //  Only images
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity
                        .badRequest()
                        .body("Only image files are allowed");
            }

            // ================= SERVICE CALL =================
            Profilepicture saved = profilePhotoService.uploadPhoto(profileId, 1, file);

            return ResponseEntity.ok(Map.of(
                    "message", "Photo1 uploaded successfully",
                    "fileName", saved.getFileName(),
                    "photoUrl", "/profile-photos/" + saved.getFileName()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to upload photo");
        }
    }
    // ================= Upload Photo 2 =================
    @PutMapping("/updatePhoto2/{profileId}")
    public ResponseEntity<?> uploadPhoto2(
            @PathVariable Long profileId,
            @RequestParam("file") MultipartFile file) {

        try {
            // ================= FILE VALIDATION =================

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is required");
            }

            //  1MB limit
            long maxSize = 1 * 1024 * 1024; 
            if (file.getSize() > maxSize) {
                return ResponseEntity
                        .status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File too large! Max allowed is 1MB");
            }

            //  Only images
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity
                        .badRequest()
                        .body("Only image files are allowed");
            }

            // ================= SERVICE CALL =================
            Profilepicture saved = profilePhotoService.uploadPhoto(profileId, 2, file);

            return ResponseEntity.ok(Map.of(
                    "message", "Photo2 uploaded successfully",
                    "fileName", saved.getFileName(),
                    "photoUrl", "/profile-photos/" + saved.getFileName()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to upload photo");
        }
    }

    // ================= Upload Photo 3 =================
    @PutMapping("/updatePhoto3/{profileId}")
    public ResponseEntity<?> uploadPhoto3(
            @PathVariable Long profileId,
            @RequestParam("file") MultipartFile file) {

        try {
            // ================= FILE VALIDATION =================

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is required");
            }

            // ✅ 1MB limit
            long maxSize = 1 * 1024 * 1024; // 1MB
            if (file.getSize() > maxSize) {
                return ResponseEntity
                        .status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File too large! Max allowed is 1MB");
            }

            // ✅ Only images
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity
                        .badRequest()
                        .body("Only image files are allowed");
            }

            // ================= SERVICE CALL =================
            Profilepicture saved = profilePhotoService.uploadPhoto(profileId, 3, file);

            return ResponseEntity.ok(Map.of(
                    "message", "Photo3 uploaded successfully",
                    "fileName", saved.getFileName(),
                    "photoUrl", "/profile-photos/" + saved.getFileName()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to upload photo");
        }
    }

    // ================= Upload Photo 4 =================
    @PutMapping("/updatePhoto4/{profileId}")
    public ResponseEntity<?> uploadPhoto4(
            @PathVariable Long profileId,
            @RequestParam("file") MultipartFile file) {

        try {
            // ================= FILE VALIDATION =================

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is required");
            }

            // ✅ 1MB limit
            long maxSize = 1 * 1024 * 1024; // 1MB
            if (file.getSize() > maxSize) {
                return ResponseEntity
                        .status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File too large! Max allowed is 1MB");
            }

            // ✅ Only images
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity
                        .badRequest()
                        .body("Only image files are allowed");
            }

            // ================= SERVICE CALL =================
            Profilepicture saved = profilePhotoService.uploadPhoto(profileId, 4, file);

            return ResponseEntity.ok(Map.of(
                    "message", "Photo4 uploaded successfully",
                    "fileName", saved.getFileName(),
                    "photoUrl", "/profile-photos/" + saved.getFileName()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to upload photo");
        }
    }
    // ================= Delete Photo 1 =================
    @DeleteMapping("/updatePhoto1/{profileId}")
    public ResponseEntity<?> deletePhoto1(@PathVariable Long profileId) throws IOException {
        profilePhotoService.deletePhoto(profileId, 1);
        return ResponseEntity.ok(Map.of("message", "Photo1 deleted successfully"));
    }

    // ================= Delete Photo 2 =================
    @DeleteMapping("/updatePhoto2/{profileId}")
    public ResponseEntity<?> deletePhoto2(@PathVariable Long profileId) throws IOException {
        profilePhotoService.deletePhoto(profileId, 2);
        return ResponseEntity.ok(Map.of("message", "Photo2 deleted successfully"));
    }

    // ================= Delete Photo 3 =================
    @DeleteMapping("/updatePhoto3/{profileId}")
    public ResponseEntity<?> deletePhoto3(@PathVariable Long profileId) throws IOException {
        profilePhotoService.deletePhoto(profileId, 3);
        return ResponseEntity.ok(Map.of("message", "Photo3 deleted successfully"));
    }

    // ================= Delete Photo 4 =================
    @DeleteMapping("/updatePhoto4/{profileId}")
    public ResponseEntity<?> deletePhoto4(@PathVariable Long profileId) throws IOException {
        profilePhotoService.deletePhoto(profileId, 4);
        return ResponseEntity.ok(Map.of("message", "Photo4 deleted successfully"));
    }

    // ================= Get All Photos =================
    @GetMapping("/{profileId}")
    public ResponseEntity<?> getAllPhotos(@PathVariable Long profileId) {

        List<Profilepicture> photos = profilePhotoService.getPhotos(profileId);

        return ResponseEntity.ok(photos);
    }
    @DeleteMapping("/updatePhoto/{profileId}")
    public ResponseEntity<?> deleteMainPhoto(@PathVariable Long profileId) throws IOException {
        profilePhotoService.deletePhoto(profileId, 0);
        return ResponseEntity.ok(Map.of("message", "Main photo deleted successfully"));
    }
    
}
