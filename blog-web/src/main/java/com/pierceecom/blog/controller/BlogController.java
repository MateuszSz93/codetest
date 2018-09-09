package com.pierceecom.blog.controller;

import com.pierceecom.blog.dto.Post;
import com.pierceecom.blog.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "post")
@RequestMapping("/posts")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogController {

    private final PostService service;

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(value = "Get all posts", notes = "Return all posts", nickname = "getAllPosts")
    @ApiResponses({ //
            @ApiResponse(code = 200, message = "successful operation", response = Post.class) //
    })
    public List<Post> getAllPosts() {
        return service.findAllPosts();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, //
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(value = "Add a new post", nickname = "addPost")
    @ApiResponses({ //
            @ApiResponse(code = 201, message = "OK of post", response = Post.class), //
            @ApiResponse(code = 405, message = "Invalid input") //
    })
    @ApiImplicitParam(name = "body", value = "Post object that needs to be added", required = true, dataType = "Body")
    public Post addPost(@RequestBody @Valid final Post body) {
        return service.savePost(body);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, //
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(value = "Update a post", nickname = "updatePost")
    @ApiResponses({ //
            @ApiResponse(code = 201, message = "OK of post", response = Post.class), //
            @ApiResponse(code = 404, message = "Post not found"), //
            @ApiResponse(code = 405, message = "Invalid input") //
    })
    @ApiImplicitParam(name = "body", value = "Post object that needs to be updated", required = true, dataType = "Body")
    public Post updatePost(@RequestBody @Valid final Post body) {
        return service.updatePost(body);
    }

    @GetMapping(value = "/{postId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(value = "Find post by ID", notes = "Returns a single post", nickname = "getPostById")
    @ApiResponses({ //
            @ApiResponse(code = 200, message = "successful operation", response = Post.class), //
            @ApiResponse(code = 204, message = "No content") //
    })
    @ApiImplicitParam(name = "postId", value = "ID of post to return", required = true, dataType = "String")
    public ResponseEntity<Post> getPostById(@PathVariable("postId") final String postId) {

        final Optional<Post> post = service.findPostById(postId);

        if (post.isPresent()) {
            return new ResponseEntity<>(post.get(), new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{postId}")
    @ApiOperation(value = "Deletes a post", response = Post.class, nickname = "deletePost")
    @ApiResponses({ //
            @ApiResponse(code = 200, message = "successful operation"), //
            @ApiResponse(code = 404, message = "Post not found") //
    })
    @ApiImplicitParam(name = "postId", value = "Post id to delete", required = true, dataType = "String")
    public void deletePost(@PathVariable("postId") final String postId) {
        service.deletePost(postId);
    }
}
