package com.on.server.global.aws.s3.uuidFile.domain;

import jakarta.persistence.*;
import lombok.*;
import com.on.server.global.domain.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "uuid_image")
public class UuidFile extends BaseEntity {

    @Column(name = "uuid", unique = true)
    private String uuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_path")
    private FilePath filePath;

    @Column(name = "file_url")
    private String fileUrl;

}
