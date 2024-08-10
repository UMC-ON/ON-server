package com.on.server.domain.companyPost.domain.repository;

import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyPostRepository extends JpaRepository<CompanyPost, Long> {

    List<CompanyPost> findByUser(User user);
}
