package com.project.TalentFindr.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Component
public class FileDownloadUtil {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    // Inject via constructor
    public FileDownloadUtil(S3Client s3Client, S3Presigner s3Presigner,@Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
    }

    /**
     * Get the file from S3 as a Spring Resource (for streaming/download endpoint)
     */


    public Resource getFileAsResource(String keyName) throws Exception {
        try {
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(keyName)
                            .build()
            );

            // Read all bytes from S3 object
            byte[] data = s3Object.readAllBytes();
            s3Object.close(); // close the stream

            // Wrap bytes in a Spring Resource
            return new ByteArrayResource(data) {
                @Override
                public String getFilename() {
                    // Return the key as filename (optional: strip prefixes if needed)
                    return keyName.substring(keyName.indexOf("_CandidateResume_") + "_CandidateResume_".length());
                }
            };
        } catch (NoSuchKeyException e) {
            return null;
        }
    }



    /**
     * Generate a presigned URL for accessing a private S3 object (for frontend images)
     */
    public String generatePresignedUrl(String keyName, Duration expiration) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(expiration)
                .build();

        return s3Presigner.presignGetObject(presignRequest)
                .url()
                .toString();
    }
}
