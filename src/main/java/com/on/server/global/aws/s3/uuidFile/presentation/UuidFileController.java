package com.on.server.global.aws.s3.uuidFile.presentation;

import com.on.server.global.aws.s3.uuidFile.application.UuidFileService;
import com.on.server.global.aws.s3.uuidFile.domain.FilePath;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/uuid-file")
@Tag(name = "UuidFile", description = "UuidFile API")
public class UuidFileController {

    private final UuidFileService uuidFileService;

    @Operation(summary = "UuidFile 상세 조회, test 용도")
    @GetMapping("/{id}")
    public ResponseEntity<UuidFile> getUuidFile(
            @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity.ok(uuidFileService.findUuidFileById(id));
    }

    @Operation(summary = "UuidFile 업로드, test 용도")
    @PostMapping(value = "/upload/{filePath}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UuidFile> uploadFile(
            @PathVariable(name = "filePath") FilePath filePath,
            @RequestPart(name = "file") MultipartFile file
    ) {
        return ResponseEntity.ok(uuidFileService.saveFile(file, filePath));
    }

    @Operation(summary = "UuidFile 삭제, test 용도")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable(name = "id") Long id
    ) {
        uuidFileService.deleteFile(uuidFileService.findUuidFileById(id));

        return ResponseEntity.ok().build();
    }
}
