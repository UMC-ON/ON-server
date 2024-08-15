package com.on.server.domain.companyPost.domain.repository;

import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompanyPostRepository extends JpaRepository<CompanyPost, Long> {

    List<CompanyPost> findByUser(User user);

    // 필터링을 위한 쿼리
    @Query("SELECT cp FROM CompanyPost cp WHERE (:startDate IS NULL OR cp.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR cp.endDate <= :endDate) " +
            "AND (:gender IS NULL OR cp.user.gender = :gender) " +
            "AND (:country IS NULL OR :country MEMBER OF cp.travelArea)")
    List<CompanyPost> findFilteredCompanyPosts(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("gender") Gender gender,
            @Param("country") String country);
}
