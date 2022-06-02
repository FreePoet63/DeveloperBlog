package com.razlivinskiyproject.developerBlog.repository;

import com.razlivinskiyproject.developerBlog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
