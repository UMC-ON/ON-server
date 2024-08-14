package com.on.server.global.aws.s3.uuidFile.application;

import com.on.server.global.aws.s3.AmazonS3Manager;
import com.on.server.global.aws.s3.uuidFile.domain.FilePath;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import com.on.server.global.aws.s3.uuidFile.domain.repository.UuidFileRepository;
import com.on.server.global.common.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.on.server.global.common.ResponseCode.DATA_NOT_EXIEST;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UuidFileService {

    private final UuidFileRepository uuidFileRepository;

    private final AmazonS3Manager amazonS3Manager;

    public UuidFile findUuidFileById(Long id) {
        return uuidFileRepository.findById(id).orElseThrow(() -> new InternalServerException(DATA_NOT_EXIEST, "해당 파일을 찾을 수 없습니다."));
    }

    @Transactional
    public UuidFile saveFile(MultipartFile file, FilePath filePath) {
        String uuid = UUID.randomUUID().toString();

        String fileS3Url = amazonS3Manager.uploadFile(generatePathKey(filePath, uuid), file);

        UuidFile uuidFile = UuidFile.builder()
                .uuid(uuid)
                .filePath(filePath)
                .fileUrl(fileS3Url)
                .build();

        uuidFileRepository.save(uuidFile);

        return uuidFile;
    }

    @Transactional
    public void deleteFile(UuidFile uuidFile) {
        amazonS3Manager.deleteFile(generatePathKey(uuidFile.getFilePath(), uuidFile.getUuid()));
        uuidFileRepository.delete(uuidFile);
    }

    private String generatePathKey(FilePath filePath, String uuid) {
        return filePath.getPath() + '/' + uuid;
    }

}
