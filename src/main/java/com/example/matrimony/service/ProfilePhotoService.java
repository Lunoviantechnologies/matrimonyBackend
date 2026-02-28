package com.example.matrimony.service;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import com.example.util.ImageCompressionUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.Profilepicture;
import com.example.matrimony.repository.ProfilePhotoRepository;
import com.example.matrimony.repository.ProfileRepository;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class ProfilePhotoService {

    private final ProfilePhotoRepository photoRepository;
    private final ProfileRepository profileRepository;

    @Value("${app.upload.profile-photos-path}")
    private String profilePhotoDir;

    public ProfilePhotoService(ProfilePhotoRepository photoRepository, ProfileRepository profileRepository) {
        this.photoRepository = photoRepository;
        this.profileRepository = profileRepository;
    }

    // ================= Upload Photo =================

    public Profilepicture uploadPhoto(Long profileId, int photoIndex, MultipartFile file) throws IOException {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Path uploadPath = Paths.get(profilePhotoDir);
        Files.createDirectories(uploadPath);

        //  Always store as JPG (best compression)
        String fileName = "profile_" + profileId + "_photo" + photoIndex + "_"
                + System.currentTimeMillis() + ".jpg";


        //  COMPRESS IMAGE HERE (MAIN LOGIC)
        ImageCompressionUtil.compressImage(
                file,
                uploadPath.toString(),
                fileName
        );

        Profilepicture picture = photoRepository
                .findByProfile_IdAndPhotoNumber(profileId, photoIndex)
                .orElse(null);

        if (picture != null) {
            // DELETE OLD FILE
            Path oldPath = uploadPath.resolve(picture.getFileName());
            Files.deleteIfExists(oldPath);

            picture.setFileName(fileName);
            picture.setUploadedAt(LocalDateTime.now());
        } else {
            picture = new Profilepicture();
            picture.setProfile(profile);
            picture.setPhotoNumber(photoIndex);
            picture.setFileName(fileName);
            picture.setUploadedAt(LocalDateTime.now());
        }

        return photoRepository.save(picture);
    }
    // ================= Delete Photo =================
    public void deletePhoto(Long profileId, Integer photoNumber) throws IOException {

        Profilepicture photo = photoRepository.findByProfile_IdAndPhotoNumber(profileId, photoNumber)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        Path uploadPath = Paths.get(profilePhotoDir);
        Path filePath = uploadPath.resolve(photo.getFileName());
        Files.deleteIfExists(filePath);

        photoRepository.delete(photo);
    }

    // ================= Get Photos =================
    public List<Profilepicture> getPhotos(Long profileId) {
        return photoRepository.findByProfile_Id(profileId);
    }

 // ================= Get Main Photo =================
    public String getMainPhoto(Long profileId) {

        return photoRepository
                .findByProfile_IdAndPhotoNumber(profileId, 0)
                .map(photo -> "/profile-photos/" + photo.getFileName())
                .orElse(null);
    }
  

    public Map<Long, Map<Integer, String>> getPhotosForProfiles(List<Long> profileIds) {

        if (profileIds == null || profileIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        List<Profilepicture> photos =
                photoRepository.findByProfile_IdIn(profileIds);

        return photos.stream().collect(
                Collectors.groupingBy(
                        p -> p.getProfile().getId(),
                        Collectors.toMap(
                                Profilepicture::getPhotoNumber,
                                p -> "/profile-photos/" + p.getFileName(),
                                (existing, replacement) -> existing
                        )
                )
        );
    }
   
}
