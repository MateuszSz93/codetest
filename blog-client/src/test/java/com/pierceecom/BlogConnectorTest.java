package com.pierceecom;

import com.pierceecom.blog.connector.BlogConnector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BlogConnector.class)
public class BlogConnectorTest {

    @Mock
    RestTemplate restTemplate;

    @Test
    public void get_all_posts_should_execute_correctly() throws Exception {

        //given
        whenNew(RestTemplate.class).withAnyArguments().thenReturn(restTemplate);
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        //when
        final BlogConnector blogConnector = new BlogConnector();
        blogConnector.getAllPosts();

        //then
        verify(restTemplate, times(1)).exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class));
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void get_post_should_execute_correctly() throws Exception {

        //given
        whenNew(RestTemplate.class).withAnyArguments().thenReturn(restTemplate);
        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        //when
        final BlogConnector blogConnector = new BlogConnector();
        blogConnector.getPost("1");

        //then
        verify(restTemplate, times(1)).getForEntity(anyString(), any(Class.class));
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void create_post_should_execute_correctly() throws Exception {

        //given
        whenNew(RestTemplate.class).withAnyArguments().thenReturn(restTemplate);
        when(restTemplate.postForEntity(anyString(), any(), any(Class.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        //when
        final BlogConnector blogConnector = new BlogConnector();
        blogConnector.createPost("1", "content", "title");

        //then
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), any(Class.class));
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void delete_post_should_execute_correctly() throws Exception {

        //given
        whenNew(RestTemplate.class).withAnyArguments().thenReturn(restTemplate);
        doNothing().when(restTemplate).delete(anyString());

        //when
        final BlogConnector blogConnector = new BlogConnector();
        blogConnector.deletePost("1");

        //then
        verify(restTemplate, times(1)).delete(anyString());
        verifyNoMoreInteractions(restTemplate);
    }
}
