package com.pierceecom.blog.repository;

import com.pierceecom.blog.dto.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {

    Optional<Post> findById(final String id);
} 
