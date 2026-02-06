package com.example.matrimony.service;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.Profilepicture;
import com.example.matrimony.repository.ProfilePhotoRepository;
import com.example.matrimony.repository.ProfileRepository;

@Service
public class ProfilePhotoService {

    private final ProfilePhotoRepository photoRepository;
    private final ProfileRepository profileRepository;

    private final Path uploadPath = Paths.get("uploads/profile-photos");

    public ProfilePhotoService(ProfilePhotoRepository photoRepository, ProfileRepository profileRepository) {
        this.photoRepository = photoRepository;
        this.profileRepository = profileRepository;
    }

    // ================= Upload Photo =================
    public Profilepicture uploadPhoto(Long profileId, Integer photoNumber, MultipartFile file) throws IOException {

        if (photoNumber < 1 || photoNumber > 4) {
            throw new RuntimeException("Photo number must be between 1 and 4");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Files.createDirectories(uploadPath);

        String fileName = "profile_" + profileId + "_photo" + photoNumber + "_" + System.currentTimeMillis() + ".jpg";
        Path filePath = uploadPath.resolve(fileName);

        Files.write(filePath, file.getBytes());

        Profilepicture profilePhoto = photoRepository
                .findByProfile_IdAndPhotoNumber(profileId, photoNumber)
                .orElse(new Profilepicture());

        profilePhoto.setProfile(profile);
        profilePhoto.setPhotoNumber(photoNumber);
        profilePhoto.setFileName(fileName);
        profilePhoto.setUploadedAt(LocalDateTime.now());

        return photoRepository.save(profilePhoto);
    }

    // ================= Delete Photo =================
    public void deletePhoto(Long profileId, Integer photoNumber) throws IOException {

        Profilepicture photo = photoRepository.findByProfile_IdAndPhotoNumber(profileId, photoNumber)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        Path filePath = uploadPath.resolve(photo.getFileName());
        Files.deleteIfExists(filePath);

        photoRepository.delete(photo);
    }

    // ================= Get Photos =================
    public List<Profilepicture> getPhotos(Long profileId) {
        return photoRepository.findByProfile_Id(profileId);
    }

    // ================= Get Photo Base64 (NEW METHOD) =================
    public String getPhotoBase64(Long profileId, Integer photoNumber) {

        try {
            Profilepicture photo = photoRepository.findByProfile_IdAndPhotoNumber(profileId, photoNumber)
                    .orElse(null);

            if (photo == null || photo.getFileName() == null || photo.getFileName().isBlank()) {
                return null;
            }

            Path filePath = uploadPath.resolve(photo.getFileName());

            if (!Files.exists(filePath)) {
                return null;
            }

            byte[] imageBytes = Files.readAllBytes(filePath);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            return "data:image/jpeg;base64," + base64Image;

        } catch (Exception e) {
            return null;
        }
    }
}
