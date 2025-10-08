package com.project.TalentFindr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Beans {
        @Bean
        public S3Client s3Client() {
            return S3Client.builder().region(Region.US_EAST_1).build();
        }

        @Bean
        public String bucketName() {
            return "talentfindr-bucket";
        }
}
