package com.on.server.domain.companyParticipant.domain.repository;

import com.on.server.domain.companyParticipant.domain.CompanyParticipant;
import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyParticipantRepository extends JpaRepository<CompanyParticipant, Long> {

    CompanyParticipant findByUserAndCompanyPostId(User userParticipant, Long companyPostId);
}
