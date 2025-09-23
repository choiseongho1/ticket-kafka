package com.study.ticket.domain.user.domain.repository;

import com.study.ticket.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자 리포지토리 인터페이스
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일로 사용자를 조회합니다.
     * @param email 이메일
     * @return 사용자 (Optional)
     */
    Optional<User> findByEmail(String email);

    /**
     * 이메일 존재 여부를 확인합니다.
     * @param email 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(String email);
}
