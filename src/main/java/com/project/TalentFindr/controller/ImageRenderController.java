package com.project.TalentFindr.controller;


import java.io.InputStream;

import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@Controller
public class ImageRenderController {

    @Autowired
    public UsersRepository usersRepository;
    private final S3Client s3Client;
    private final String bucketName;

    public ImageRenderController(S3Client s3Client, @Value("${aws.s3.bucket-name}")String bucketName, UsersRepository usersRepository) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.usersRepository = usersRepository;
    }


    @GetMapping("/profile/photo")
    public ResponseEntity<Resource> getProfilePhoto(@RequestParam("key") String imageName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find user"));

        String keyName = "";
        if ("Recruiter".equals(user.getUserTypeId().getUserTypeName())) {
            keyName = user.getUserId() + "_RecruiterImage_" + imageName;
        } else {
            keyName = user.getUserId() + "_CandidateImage_" + imageName;
        }

        try {
            // Get the InputStream from S3
            InputStream inputStream = s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(keyName)
                            .build()
            );

            // Wrap it as a Resource
            Resource resource = new InputStreamResource(inputStream);

            String lowerCaseName = imageName.toLowerCase();
            MediaType contentType;
            if (lowerCaseName.endsWith(".png")) {
                contentType = MediaType.IMAGE_PNG;
            } else if (lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".jpeg")) {
                contentType = MediaType.IMAGE_JPEG;
            } else {
                contentType = MediaType.APPLICATION_OCTET_STREAM; // fallback
            }

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(resource);

        } catch (NoSuchKeyException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


}
