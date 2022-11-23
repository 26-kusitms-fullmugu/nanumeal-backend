package com.fullmugu.nanumeal.api.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByNickName(String nickName);

    Optional<User> findByKakaoIdAndEmail(Long kakaoId, String email);

    void deleteUserById(Long id);
}
