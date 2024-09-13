package com.on.server.domain.alarm.domain.repository;

import com.on.server.domain.alarm.domain.Alert;
import com.on.server.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    Page<Alert> findByUser(User user, Pageable pageable);

    long countByUserAndIsReadFalse(User user);
}
