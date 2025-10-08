package com.project.TalentFindr.util;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.InputStream;

@Component
public class FileDownloadUtil {

    private final S3Client s3Client;
    private final String bucketName;

    // Inject bucket name via constructor or @Value
    public  FileDownloadUtil(S3Client s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public Resource getFileAsResource(String keyName) throws Exception {
        try {
            InputStream inputStream = s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(keyName)
                            .build()
            );
            return new InputStreamResource(inputStream);
        } catch (NoSuchKeyException e) {
            // file not found in S3
            return null;
        }
    }
}
