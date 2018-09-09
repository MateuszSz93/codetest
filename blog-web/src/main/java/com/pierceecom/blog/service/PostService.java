package com.pierceecom.blog.service;

import com.pierceecom.blog.dto.Post;
import com.pierceecom.blog.exception.EntityExistsException;
import com.pierceecom.blog.exception.NotFoundException;
import com.pierceecom.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostService {

    final PostRepository repository;

    public Post savePost(final Post post) {

        if (nonNull(post.getId()) && repository.existsById(post.getId())) {
            throw new EntityExistsException("Entiy with this ID already exists.");
        } else if (isNull(post.getId())) {
            post.setId(randomUUID().toString());
        }
        return repository.save(post);
    }

    public Optional<Post> findPostById(final String id) {
        return repository.findById(id);
    }

    public List<Post> findAllPosts() {
        return repository.findAll();
    }

    public Post updatePost(final Post post) {

        checkIfPostExist(post.getId());
        return repository.save(post);
    }

    public void deletePost(final String id) {

        checkIfPostExist(id);
        repository.deleteById(id);
    }

    private void checkIfPostExist(final String id) {

        if (isNull(id) || !repository.existsById(id)) {
            throw new NotFoundException("Post with this ID can not be found.");
        }
    }
} 
