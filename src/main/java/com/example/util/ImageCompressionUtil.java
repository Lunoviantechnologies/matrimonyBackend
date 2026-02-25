package com.example.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageCompressionUtil {

    public static String compressImage(
            MultipartFile file,
            String uploadDir,
            String fileName
    ) throws IOException {

        Path uploadPath = Path.of(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        File outputFile = uploadPath.resolve(fileName).toFile();

        // STEP 1: Resize big images
        Thumbnails.of(file.getInputStream())
                .size(800, 800)          // resize large images
                .outputFormat("jpg")     // convert to jpg
                .outputQuality(0.8f)
                .toFile(outputFile);

        // STEP 2: compress until <200KB
        long size = outputFile.length();
        float quality = 0.8f;

        while (size > 200 * 1024) { // 200KB
            quality -= 0.05f;

            if (quality <= 0.1f) break;

            Thumbnails.of(outputFile)
                    .scale(1)
                    .outputQuality(quality)
                    .toFile(outputFile);

            size = outputFile.length();
        }

        return fileName;
    }
}