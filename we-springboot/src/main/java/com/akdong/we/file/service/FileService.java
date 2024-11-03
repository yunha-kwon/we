package com.akdong.we.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class FileService {
    private final AmazonS3 amazonS3;
    private final String bucket;

    public FileService(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    public String upload(MultipartFile file,String dirName) throws IOException {
        // 파일 이름에서 공백을 제거한 새로운 파일 이름 생성
        String originalFileName = file.getOriginalFilename();

        // UUID를 파일명에 추가
        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName.replaceAll("\\s", "_");
        String fileName = dirName + "/" + uniqueFileName;

        // S3 클라이언트 객체 사용
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(bucket, fileName, inputStream, metadata);
        } catch (IOException e) {
            log.error("S3 업로드 중 오류 발생: {}", e.getMessage());
            throw e;
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
