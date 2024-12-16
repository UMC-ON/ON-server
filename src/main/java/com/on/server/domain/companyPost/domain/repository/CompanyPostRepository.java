package com.on.server.domain.companyPost.domain.repository;

import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import org.springframework.data.domain.Page;
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

    // 필터링을 위한 쿼리
    @Query("SELECT cp FROM CompanyPost cp WHERE (:startDate IS NULL OR cp.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR cp.endDate <= :endDate) " +
            "AND (:gender IS NULL OR cp.user.gender = :gender) " +
            "ORDER BY cp.createdAt DESC")
    Page<CompanyPost> findFilteredCompanyPostsWithoutCountry(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("gender") Gender gender,
            Pageable pageable);

    // 최신 4개의 글을 최신순으로 가져오기
    default List<CompanyPost> findTop4ByOrderByCreatedAtDesc() {
        Pageable pageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createdAt"));
        return findAll(pageable).getContent();
    }

    /**
    * 사용자가 작성하지 않은 동행글 중에서,
    * 특정 국가명을 포함하고 모집이 완료되지 않은 글
    * 최신순으로 정렬하여 최대 5개 가져오기
    * @param country: 검색할 국가명
    * @param user: 현재 로그인한 사용자 (사용자가 작성한 글은 제외)
    */
    @Query("SELECT c FROM CompanyPost c JOIN c.travelArea t WHERE t LIKE CONCAT(:country, ' %') AND c.isRecruitCompleted = false AND c.user <> :user ORDER BY c.createdAt DESC")
    List<CompanyPost> findTop5ByTravelArea(@Param("country") String country, @Param("user") User user, Pageable pageable);

    // 동행글 상세보기의 동행글 추천에서 같은 글은 조회에서 제외
    @Query("SELECT c FROM CompanyPost c JOIN c.travelArea t WHERE t LIKE CONCAT(:country, ' %') AND c.isRecruitCompleted = false AND c.id <> :companyPostId AND c.user <> :user ORDER BY c.createdAt DESC")
    List<CompanyPost> findTop5NearbyCompanyPosts(@Param("country") String country, @Param("companyPostId") Long companyPostId, @Param("user") User user, Pageable pageable);

    // 최신순 정렬
    Page<CompanyPost> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<CompanyPost> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
