package com.pierceecom.blog.connector;

import com.pierceecom.blog.dto.Post;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static java.lang.System.out;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BlogConnector {

    private static final String POSTS_URI = "http://localhost:8080/blog-web/posts/";

    private final RestTemplate restTemplate;

    public BlogConnector() {
        this.restTemplate = new RestTemplate();
    }

    public void getAllPosts() {

        final ResponseEntity<List<Post>> responseEntity = restTemplate
                .exchange(getUriComponentsBuilder().toUriString(), HttpMethod.GET, new HttpEntity<>(createHeaders()),
                        new ParameterizedTypeReference<List<Post>>() {

                        });

        if (nonNull(responseEntity.getBody())) {
            responseEntity.getBody().forEach(p -> System.out.println(p.toString()));
        }
    }

    public void getPost(final String id) {

        final ResponseEntity<Post> post = restTemplate.getForEntity(getUriComponentsBuilder(id).toUriString(), Post.class);

        displayPost(post);
    }

    public void createPost(final String id, final String content, final String title) {

        final Post post = new Post();
        if (isNotBlank(id)) {
            post.setId(id);
        }
        post.setContent(content);
        post.setTitle(title);

        try {
            final ResponseEntity<Post> response = restTemplate.postForEntity(getUriComponentsBuilder().toUriString(), post, Post.class);

            displayPost(response);
        } catch (HttpClientErrorException ex) {
            out.println(ex.getResponseBodyAsString());
        }
    }

    public void deletePost(final String id) {

        try {
            restTemplate.delete(getUriComponentsBuilder(id).toUriString());
        } catch (HttpClientErrorException ex) {
            out.println(ex.getResponseBodyAsString());
        }
    }

    private void displayPost(final ResponseEntity<Post> post) {
        if (nonNull(post.getBody())) {
            System.out.println(post.getBody().toString());
        }
    }

    private HttpHeaders createHeaders() {

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }

    private UriComponentsBuilder getUriComponentsBuilder() {
        return UriComponentsBuilder.fromHttpUrl(POSTS_URI);
    }

    private UriComponentsBuilder getUriComponentsBuilder(final String path) {
        return getUriComponentsBuilder().path(path);
    }
} 
