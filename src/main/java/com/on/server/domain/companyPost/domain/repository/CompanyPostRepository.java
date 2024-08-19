package com.on.server.domain.companyPost.domain.repository;

import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // 최신 4개의 글을 최신순으로 가져오기
    default List<CompanyPost> findTop4ByOrderByCreatedAtDesc() {
        Pageable pageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createdAt"));
        return findAll(pageable).getContent();
    }

    @Query("SELECT c FROM CompanyPost c JOIN c.travelArea t WHERE t LIKE CONCAT('%', :country, '%') AND c.currentRecruitNumber < c.totalRecruitNumber ORDER BY c.createdAt DESC")
    List<CompanyPost> findTop5ByTravelArea(@Param("country") String country);

}
