package com.example.matrimony.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    // ================= Upload Photo 1 =================
    @PutMapping("/updatePhoto1/{profileId}")
    public ResponseEntity<?> uploadPhoto1(@PathVariable Long profileId,
                                         @RequestParam("file") MultipartFile file) throws IOException {

    	Profilepicture saved = profilePhotoService.uploadPhoto(profileId, 1, file);

        return ResponseEntity.ok(Map.of(
                "message", "Photo1 uploaded successfully",
                "fileName", saved.getFileName(),
                "photoUrl", "/profile-photos/" + saved.getFileName()
        ));
    }

    // ================= Upload Photo 2 =================
    @PutMapping("/updatePhoto2/{profileId}")
    public ResponseEntity<?> uploadPhoto2(@PathVariable Long profileId,
                                         @RequestParam("file") MultipartFile file) throws IOException {

    	Profilepicture saved = profilePhotoService.uploadPhoto(profileId, 2, file);

        return ResponseEntity.ok(Map.of(
                "message", "Photo2 uploaded successfully",
                "fileName", saved.getFileName(),
                "photoUrl", "/profile-photos/" + saved.getFileName()
        ));
    }

    // ================= Upload Photo 3 =================
    @PutMapping("/updatePhoto3/{profileId}")
    public ResponseEntity<?> uploadPhoto3(@PathVariable Long profileId,
                                         @RequestParam("file") MultipartFile file) throws IOException {

    	Profilepicture saved = profilePhotoService.uploadPhoto(profileId, 3, file);

        return ResponseEntity.ok(Map.of(
                "message", "Photo3 uploaded successfully",
                "fileName", saved.getFileName(),
                "photoUrl", "/profile-photos/" + saved.getFileName()
        ));
    }

    // ================= Upload Photo 4 =================
    @PutMapping("/updatePhoto4/{profileId}")
    public ResponseEntity<?> uploadPhoto4(@PathVariable Long profileId,
                                         @RequestParam("file") MultipartFile file) throws IOException {

    	Profilepicture saved = profilePhotoService.uploadPhoto(profileId, 4, file);

        return ResponseEntity.ok(Map.of(
                "message", "Photo4 uploaded successfully",
                "fileName", saved.getFileName(),
                "photoUrl", "/profile-photos/" + saved.getFileName()
        ));
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
}
