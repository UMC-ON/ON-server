package com.on.server.domain.diary.domain.repository;

import com.on.server.domain.diary.domain.Diary;
import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByUser(User user);

}
