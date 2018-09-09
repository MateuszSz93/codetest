package com.pierceecom.blog;

import com.pierceecom.blog.dto.Post;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlogTestIntegr {

    private static final String POSTS_URI = "http://localhost:8080/blog-web/posts/";
    private static final String FIRST_ID = "1";
    private static final String FIRST_CONTENT = "First content";
    private static final String FIRST_TITLE = "First title";
    private static final String NEW_CONTENT = "New content";
    private static final String SECOND_ID = "2";
    private static final String SECOND_CONTENT = "Second tile";
    private static final String SECOND_TITLE = "Second content";

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void test_1_BlogWithoutPosts() {

        final ResponseEntity<List<Post>> response = getAllPosts();

        assertThat(response.getBody(), IsEmptyCollection.empty());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void test_2_AddPosts() {

        final ResponseEntity<Post> firstPost = createPost(FIRST_ID, FIRST_CONTENT, FIRST_TITLE);
        final ResponseEntity<Post> secondPost = createPost(SECOND_ID, SECOND_CONTENT, SECOND_TITLE);

        assertEquals(firstPost.getBody().getId(), FIRST_ID);
        assertEquals(firstPost.getBody().getContent(), FIRST_CONTENT);
        assertEquals(firstPost.getBody().getTitle(), FIRST_TITLE);
        assertEquals(firstPost.getStatusCode(), HttpStatus.CREATED);
        assertEquals(secondPost.getBody().getId(), SECOND_ID);
        assertEquals(secondPost.getBody().getContent(), SECOND_CONTENT);
        assertEquals(secondPost.getBody().getTitle(), SECOND_TITLE);
        assertEquals(secondPost.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void test_3_GetPost() {

        final ResponseEntity<Post> firstPost = getSinglePost(FIRST_ID);
        final ResponseEntity<Post> secondPost = getSinglePost(SECOND_ID);

        assertEquals(firstPost.getBody().getId(), FIRST_ID);
        assertEquals(firstPost.getBody().getContent(), FIRST_CONTENT);
        assertEquals(firstPost.getBody().getTitle(), FIRST_TITLE);
        assertEquals(secondPost.getBody().getId(), SECOND_ID);
        assertEquals(secondPost.getBody().getContent(), SECOND_CONTENT);
        assertEquals(secondPost.getBody().getTitle(), SECOND_TITLE);
    }

    @Test
    public void test_4_GetAllPosts() {
        final ResponseEntity<List<Post>> response = getAllPosts();

        assertThat(response.getBody(), hasSize(2));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void test_5_UpdatePost() {

        final ResponseEntity<Post> post = restTemplate.exchange(getUriComponentsBuilder().toUriString(), HttpMethod.PUT,
                new HttpEntity<>(preparePost(FIRST_ID, NEW_CONTENT, FIRST_TITLE), createHeaders()), Post.class);

        assertEquals(post.getBody().getId(), FIRST_ID);
        assertEquals(post.getBody().getContent(), NEW_CONTENT);
        assertEquals(post.getBody().getTitle(), FIRST_TITLE);
    }

    @Test
    public void test_6_DeletePosts() {

        final ResponseEntity deleteFirstPost = removePost(FIRST_ID);
        final ResponseEntity<Post> getFirstPost = getSinglePost(FIRST_ID);
        final ResponseEntity deleteSecondPost = removePost(SECOND_ID);
        final ResponseEntity<Post> getSecondPost = getSinglePost(SECOND_ID);

        assertEquals(deleteFirstPost.getStatusCode(), HttpStatus.OK);
        assertEquals(getFirstPost.getStatusCode(), HttpStatus.NO_CONTENT);
        assertNull(getFirstPost.getBody());
        assertEquals(deleteSecondPost.getStatusCode(), HttpStatus.OK);
        assertEquals(getSecondPost.getStatusCode(), HttpStatus.NO_CONTENT);
        assertNull(getSecondPost.getBody());
    }

    @Test
    public void test_7_GetAllPostsShouldNowBeEmpty() {

        final ResponseEntity<List<Post>> response = getAllPosts();

        assertThat(response.getBody(), IsEmptyCollection.empty());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
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

    private Post preparePost(final String id, final String content, final String title) {

        final Post post = new Post();
        post.setId(id);
        post.setContent(content);
        post.setTitle(title);

        return post;
    }

    private ResponseEntity<List<Post>> getAllPosts() {
        return restTemplate.exchange(getUriComponentsBuilder().toUriString(), HttpMethod.GET, new HttpEntity<>(createHeaders()),
                new ParameterizedTypeReference<List<Post>>() {

                });
    }

    private ResponseEntity<Post> getSinglePost(final String id) {
        return restTemplate.exchange(getUriComponentsBuilder(id).toUriString(), HttpMethod.GET, new HttpEntity<>(createHeaders()), Post.class);
    }

    private ResponseEntity removePost(final String id) {
        return restTemplate.exchange(getUriComponentsBuilder(id).toUriString(), HttpMethod.DELETE, new HttpEntity<>(createHeaders()), ResponseEntity.class);
    }

    private ResponseEntity<Post> createPost(final String id, final String content, final String tile) {
        return restTemplate
                .exchange(getUriComponentsBuilder().toUriString(), HttpMethod.POST, new HttpEntity<>(preparePost(id, content, tile), createHeaders()),
                        Post.class);
    }
}
