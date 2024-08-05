package com.on.server.domain.companyPost.domain.repository;

import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyPostRepository extends JpaRepository<CompanyPost, Long> {

    List<CompanyPost> findByUser(User user);
}
