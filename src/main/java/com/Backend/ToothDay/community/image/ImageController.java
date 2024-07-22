package com.Backend.ToothDay.community.image;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private static final String UPLOAD_DIR = "/home/ubuntu/upload"; // EC2 서버의 파일 경로 설정

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            // 파일 경로 설정
            Path filePath = Paths.get(UPLOAD_DIR + File.separator + fileName).toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // 파일 존재 여부 확인
            if (resource.exists()) {
                // 이미지 파일을 바이트 배열로 읽어옴
                byte[] imageBytes = Files.readAllBytes(filePath);

                // Content-Type 설정
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(imageBytes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
