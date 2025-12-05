package com.zjgsu.djy.coursecloud.user.repository;

import com.zjgsu.djy.coursecloud.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 用户Repository接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 根据用户类型查找用户
     */
    List<User> findByUserType(String userType);

    /**
     * 根据用户编号查找用户
     */
    Optional<User> findByUserId(String userId);

    /**
     * 根据用户编号和用户类型查找用户
     */
    Optional<User> findByUserIdAndUserType(String userId, String userType);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);
}

