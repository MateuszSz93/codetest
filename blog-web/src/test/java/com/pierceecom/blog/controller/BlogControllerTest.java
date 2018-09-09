package com.pierceecom.blog.controller;

import com.pierceecom.blog.dto.Post;
import com.pierceecom.blog.service.PostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BlogControllerTest {

    private static final String ID = "1";
    private static final String CONTENT = "Content";
    private static final String TITLE = "Title";

    @InjectMocks
    BlogController controller;

    @Mock
    PostService service;

    @Test
    public void get_all_posts_should_return_empty_list() {

        //given
        when(service.findAllPosts()).thenReturn(new ArrayList<>());

        //when
        final List<Post> postList = controller.getAllPosts();

        //then
        assertNotNull(postList);
        assertTrue(postList.isEmpty());

        verify(service, times(1)).findAllPosts();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void save_post_should_return_post() {

        //given
        when(service.savePost(any())).thenReturn(createPost());

        //when
        final Post post = controller.addPost(createPost());

        //then
        assertEquals(post.getId(), ID);
        assertEquals(post.getContent(), CONTENT);
        assertEquals(post.getTitle(), TITLE);

        verify(service, times(1)).savePost(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void update_post_should_return_post() {

        //given
        when(service.updatePost(any())).thenReturn(createPost());

        //when
        final Post post = controller.updatePost(createPost());

        //then
        assertEquals(post.getId(), ID);
        assertEquals(post.getContent(), CONTENT);
        assertEquals(post.getTitle(), TITLE);

        verify(service, times(1)).updatePost(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void get_post_by_id_should_return_post() {

        //given
        when(service.findPostById(any())).thenReturn(Optional.of(createPost()));

        //when
        final ResponseEntity<Post> post = controller.getPostById(ID);

        //then
        assertNotNull(post.getBody());
        assertEquals(post.getStatusCode(), HttpStatus.OK);
        assertEquals(post.getBody().getId(), ID);
        assertEquals(post.getBody().getContent(), CONTENT);
        assertEquals(post.getBody().getTitle(), TITLE);

        verify(service, times(1)).findPostById(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void get_post_by_id_should_return_http_status_no_content() {

        //given
        when(service.findPostById(any())).thenReturn(Optional.empty());

        //when
        final ResponseEntity<Post> post = controller.getPostById(ID);

        //then
        assertNull(post.getBody());
        assertEquals(post.getStatusCode(), HttpStatus.NO_CONTENT);

        verify(service, times(1)).findPostById(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void delete_post_should_execute_correctly() {
        //given
        doNothing().when(service).deletePost(any());

        //when
        controller.deletePost(ID);

        //then
        verify(service, times(1)).deletePost(any());
        verifyNoMoreInteractions(service);
    }

    private Post createPost() {

        final Post post = new Post();
        post.setId(ID);
        post.setContent(CONTENT);
        post.setTitle(TITLE);

        return post;
    }
}
