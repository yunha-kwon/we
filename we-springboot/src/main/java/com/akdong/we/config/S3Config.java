package com.akdong.we.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.accesskey1}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretkey1}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region = Regions.AP_NORTHEAST_2.getName();

    public S3Config(
        @Value("${cloud.aws.credentials.accessKey1}") String accessKey,
        @Value("${cloud.aws.credentials.secretKey1}") String secretKey
    ){
        log.info("cloud.aws.credentials.accessKey1 = {}", accessKey);
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }
}


