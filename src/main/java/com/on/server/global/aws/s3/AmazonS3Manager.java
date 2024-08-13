package com.on.server.global.aws.s3;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.on.server.global.aws.s3.config.AwsS3Config;
import com.on.server.global.common.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static com.on.server.global.common.ResponseCode.FILE_DELETE_FAIL;
import static com.on.server.global.common.ResponseCode.FILE_UPLOAD_FAIL;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;

    private final AwsS3Config awsS3Config;

    public String uploadFile(String keyName, MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(awsS3Config.getBucket(), keyName, file.getInputStream(), objectMetadata));
        } catch (Exception e) {
            throw new InternalServerException(FILE_UPLOAD_FAIL, "S3에 file upload를 실패했습니다: " + e.getMessage());
        }

        return amazonS3.getUrl(awsS3Config.getBucket(), keyName).toString();
    }

    public void deleteFile(String keyName) {
        try {
            amazonS3.deleteObject(awsS3Config.getBucket(), keyName);
        } catch (Exception e) {
            throw new InternalServerException(FILE_DELETE_FAIL, "S3에 file delete를 실패했습니다: " + e.getMessage());
        }
    }
}
