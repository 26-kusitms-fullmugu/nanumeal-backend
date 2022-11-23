package com.fullmugu.nanumeal.api.controller.s3;

import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class S3UploadController {

    private final S3UploadService s3UploadService;

    @PostMapping("/document")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile, @AuthenticationPrincipal User user) throws IOException {
        return ResponseEntity.ok(
                s3UploadService.upload(multipartFile, user)
        );
    }
}
