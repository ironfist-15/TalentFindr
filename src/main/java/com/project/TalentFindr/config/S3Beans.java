package com.project.TalentFindr.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
 // New Import


@Configuration
public class S3Beans {

    @Value("talentfindr-bucket")
    private String bucketName;

    @Value("us-east-1")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                 .build();
    }

    @Bean
    public String bucketName() {
        return bucketName;
    }
}
