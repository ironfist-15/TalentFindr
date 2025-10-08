package com.project.TalentFindr.util;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

@Component
public class FileUploadUtil {

    private final S3Client s3Client;
    private final String bucketName;

    // Inject via constructor
    public FileUploadUtil(S3Client s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public void saveFile(String keyName, MultipartFile multipartFile) throws Exception {
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(keyName)
                            .build(),
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize())
            );
        } catch (Exception e) {
            throw new Exception("Could not upload file: " + keyName, e);
        }
    }
}
