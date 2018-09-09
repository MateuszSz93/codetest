package com.pierceecom.blog.service;

import com.pierceecom.blog.dto.Post;
import com.pierceecom.blog.exception.EntityExistsException;
import com.pierceecom.blog.exception.NotFoundException;
import com.pierceecom.blog.repository.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceTest {

    private static final String FIRST_ID = "1";
    private static final String SECOND_ID = "2";
    private static final String CONTENT = "Content";
    private static final String TITLE = "Title";
    private static final UUID RANDOM_UUID = UUID.randomUUID();

    @InjectMocks
    PostService service;

    @Mock
    PostRepository repository;

    @Test
    public void save_post_should_execute_correctly() {

        //given
        when(repository.save(any())).thenReturn(createPost(FIRST_ID, CONTENT, TITLE));
        when(repository.existsById(any())).thenReturn(false);

        //when
        final Post post = service.savePost(createPost(FIRST_ID, CONTENT, TITLE));

        //then
        assertEquals(post.getId(), FIRST_ID);
        assertEquals(post.getContent(), CONTENT);
        assertEquals(post.getTitle(), TITLE);

        verify(repository, times(1)).save(any());
        verify(repository, times(1)).existsById(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void save_post_without_id_should_generate_id() {

        //given
        when(repository.save(any())).thenReturn(createPost(RANDOM_UUID.toString(), CONTENT, TITLE));

        //when
        final Post post = service.savePost(createPost(CONTENT, TITLE));

        //then
        assertNotNull(post.getId());
        assertEquals(post.getId(), RANDOM_UUID.toString());
        assertEquals(post.getContent(), CONTENT);
        assertEquals(post.getTitle(), TITLE);

        verify(repository, times(1)).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = EntityExistsException.class)
    public void save_post_with_already_existing_id_should_throw_exception() {

        //given
        when(repository.existsById(any())).thenReturn(true);

        //when
        service.savePost(createPost(FIRST_ID, CONTENT, TITLE));

        //then
        //Throw exception.
    }

    @Test
    public void find_post_by_id_should_return_single_post() {

        //given
        when(repository.findById(any())).thenReturn(Optional.of(createPost(FIRST_ID, CONTENT, TITLE)));

        //when
        final Optional<Post> post = service.findPostById(FIRST_ID);

        //then
        assertNotNull(post);
        assertTrue(post.isPresent());
        assertEquals(post.get().getId(), FIRST_ID);
        assertEquals(post.get().getContent(), CONTENT);
        assertEquals(post.get().getTitle(), TITLE);

        verify(repository, times(1)).findById(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void find_post_by_id_should_return_empty_optional() {

        //given
        when(repository.findById(any())).thenReturn(Optional.empty());

        //when
        final Optional<Post> post = service.findPostById(FIRST_ID);

        //then
        assertNotNull(post);
        assertFalse(post.isPresent());

        verify(repository, times(1)).findById(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void find_all_posts_should_return_list_with_two_posts() {

        //given
        when(repository.findAll()).thenReturn(Arrays.asList(createPost(FIRST_ID, CONTENT, TITLE), createPost(SECOND_ID, CONTENT, TITLE)));

        //when
        final List<Post> postList = service.findAllPosts();

        //then
        assertNotNull(postList);
        assertFalse(postList.isEmpty());
        assertThat(postList, hasSize(2));

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void find_all_posts_should_return_empty_list() {

        //given
        when(repository.findAll()).thenReturn(new ArrayList<>());

        //when
        final List<Post> postList = service.findAllPosts();

        //then
        assertNotNull(postList);
        assertTrue(postList.isEmpty());

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void update_post_should_execute_correctly() {

        //given
        when(repository.save(any())).thenReturn(createPost(FIRST_ID, CONTENT, TITLE));
        when(repository.existsById(any())).thenReturn(true);

        //when
        final Post post = service.updatePost(createPost(FIRST_ID, CONTENT, TITLE));

        //then
        assertEquals(post.getId(), FIRST_ID);
        assertEquals(post.getContent(), CONTENT);
        assertEquals(post.getTitle(), TITLE);

        verify(repository, times(1)).save(any());
        verify(repository, times(1)).existsById(any());
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = NotFoundException.class)
    public void update_post_without_existing_id_should_throw_exception() {

        //given
        when(repository.existsById(any())).thenReturn(false);

        //when
        service.updatePost(createPost(FIRST_ID, CONTENT, TITLE));

        //then
        //Throw exception.
    }

    @Test
    public void delete_post_should_execute_correctly() {

        //given
        when(repository.existsById(any())).thenReturn(true);
        doNothing().when(repository).deleteById(any());

        //when
        service.deletePost(FIRST_ID);

        //then
        verify(repository, times(1)).deleteById(any());
        verify(repository, times(1)).existsById(any());
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = NotFoundException.class)
    public void delete_post_should_throw_exception() {

        //given
        when(repository.existsById(any())).thenReturn(false);

        //when
        service.deletePost(FIRST_ID);

        //then
        verify(repository, times(1)).existsById(any());
        verifyNoMoreInteractions(repository);
    }

    private Post createPost(final String id, final String content, final String title) {

        final Post post = createPost(content, title);
        post.setId(id);

        return post;
    }

    private Post createPost(final String content, final String title) {

        final Post post = new Post();
        post.setContent(content);
        post.setTitle(title);

        return post;
    }
} 
