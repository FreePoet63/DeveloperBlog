package com.razlivinskiyproject.developerBlog.repository;

import com.razlivinskiyproject.developerBlog.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface WritePostRepository extends CrudRepository<Post, Long> {
}
