package com.on.server.global.aws.s3.uuidFile.domain.repository;

import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UuidFileRepository extends JpaRepository<UuidFile, Long> {
}
