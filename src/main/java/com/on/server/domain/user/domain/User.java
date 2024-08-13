package com.on.server.domain.user.domain;

import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

import java.time.LocalDate;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "age", nullable = false)
    private Integer age;

    /**
     * MALE,
     * FEMALE
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "phone", nullable = false)
    private String phone;

    // 교환/방문 여부
    @Column(name = "is_dispatch_confirmed", nullable = false)
    private Boolean isDispatchConfirmed;

    /**
     * 교환/방문 타입
     * DISPATCHED,
     * EXCHANGED
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "dispatched_type")
    private DispatchedType dispatchedType;

    // 교환/방문교 이름
    @Column(name = "dispatched_university")
    private String dispatchedUniversity;

    // 교환/방문교 홈페이지 링크
    @Column(name = "university_url")
    private String universityUrl;

    // 교환/방문교의 소재 국가
    @Column(name = "country")
    private String country;

    // 나의 교환(일기) 시작 일자)
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * Spring Security 전용 속성
     * UserDetails interface 구현
     * TODO: 따로 분리하는 것도 나쁘지 않을 듯
     */

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<UserStatus> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void updateRole(UserStatus oldRole, UserStatus newRole) {
        if (!this.getRoles().contains(oldRole))
            throw new BadRequestException(ResponseCode.INVALID_PARAMETER, "유저에게 해당 기존 권한이 존재하지 않습니다.");
        this.removeRole(oldRole);
        this.addRole(newRole);
    }

    public void clearAndAddRole(UserStatus role) {
        this.roles.clear();
        this.roles.add(role);
    }

    private void addRole(UserStatus role) {
        this.roles.add(role);
    }

    private void removeRole(UserStatus role) {
        this.roles.remove(role);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUniversityUrl(String universityUrl) {
        this.universityUrl = universityUrl;
    }

}
