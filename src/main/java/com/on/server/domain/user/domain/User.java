package com.on.server.domain.user.domain;

import com.on.server.domain.dispatchCertify.domain.DispatchCertify;
import com.on.server.domain.dispatchCertify.domain.PermitStatus;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.common.exceptions.InternalServerException;
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

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "name", nullable = false)
    private String name;

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
    private DispatchType dispatchType;

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

    // 사용자 디바이스 토큰
    @Column(name = "device_token")
    private String deviceToken;

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
        return this.loginId;
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

    /**
     * User 엔티티의 속성을 업데이트하는 메소드
     */

    public void setStartDate(LocalDate startDate) {
        if (startDate == null)
            throw new InternalServerException(ResponseCode.INTERNAL_SERVER, "필수 입력 값인 시작 일자 값 검사를 하지 않았습니다. 관리자에게 문의 바랍니다.");
        this.startDate = startDate;
    }

    public void updateRole(UserStatus oldRole, UserStatus newRole) {
        if (!this.getRoles().contains(oldRole))
            throw new BadRequestException(ResponseCode.INVALID_PARAMETER, "유저에게 해당 기존 권한이 존재하지 않습니다.");
        this.removeRole(oldRole);
        this.addRole(newRole);
    }

    public void changeRole(UserStatus role) {
        if (role == null)
            throw new InternalServerException(ResponseCode.INTERNAL_SERVER, "필수 입력 값인 권한 값 검사를 하지 않았습니다. 관리자에게 문의 바랍니다.");
        this.roles.clear();
        this.roles.add(role);
        this.setIsDispatchConfirmed(!role.equals(UserStatus.TEMPORARY));
    }

    public void changeRoleByDispatchCertify(DispatchCertify dispatchCertify) {
        if (dispatchCertify.getPermitStatus().equals(PermitStatus.AWAIT)) {
            this.changeRole(UserStatus.AWAIT);
        } else if (dispatchCertify.getPermitStatus().equals(PermitStatus.ACTIVE)) {
            this.changeRole(UserStatus.ACTIVE);
        } else if (dispatchCertify.getPermitStatus().equals(PermitStatus.DENIED)) {
            this.changeRole(UserStatus.DENIED);
        }
        this.dispatchType = dispatchCertify.getDispatchType();
        this.dispatchedUniversity = dispatchCertify.getDispatchedUniversity();
        this.country = dispatchCertify.getCountry();
    }

    private void setIsDispatchConfirmed(Boolean isDispatchConfirmed) {
        if (isDispatchConfirmed == null)
            throw new InternalServerException(ResponseCode.INTERNAL_SERVER, "필수 입력 값인 교환/방문 여부 값 검사를 하지 않았습니다. 관리자에게 문의 바랍니다.");
        if (isDispatchConfirmed && this.getRoles().contains(UserStatus.TEMPORARY))
            throw new InternalServerException(ResponseCode.INTERNAL_SERVER, "임시 회원은 교환/방문 여부 참으로 설정 불가합니다. 관리자에게 문의 바랍니다.");
        this.isDispatchConfirmed = isDispatchConfirmed;
    }

    private void addRole(UserStatus role) {
        this.roles.add(role);
    }

    private void removeRole(UserStatus role) {
        this.roles.remove(role);
    }

    public void setNickname(String nickname) {
        if (nickname == null || nickname.isEmpty())
            throw new InternalServerException(ResponseCode.INTERNAL_SERVER, "필수 입력 값인 닉네임 값 검사를 하지 않았습니다. 관리자에게 문의 바랍니다.");
        this.nickname = nickname;
    }

    public void setUniversityUrl(String universityUrl) {
        if (universityUrl == null || universityUrl.isEmpty())
            throw new InternalServerException(ResponseCode.INTERNAL_SERVER, "필수 입력 값인 교환/방문교 홈페이지 링크 값 검사를 하지 않았습니다. 관리자에게 문의 바랍니다.");
        this.universityUrl = universityUrl;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public User deleteUser() {
        this.loginId = "deleted_" + this.getId();
        this.password = "deleted" + this.getId();
        this.nickname = "탈퇴사용자" + this.getId();
        this.name = "탈퇴사용자" + this.getId();
        this.phone = "탈퇴사용자" + this.getId();
        this.roles.clear();
        this.roles.add(UserStatus.TEMPORARY);
        return this;
    }

}
