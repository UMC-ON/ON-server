package com.on.server.domain.user.domain.repository;

import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);

    Boolean existsByLoginId(String loginId);

    Boolean existsByNickname(String nickname);

    List<User> findByName(String name);

}
