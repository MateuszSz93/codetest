package com.pierceecom.blog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Document(collection = "post")
public class Post {

    @Id
    @ApiModelProperty(example = "1")
    private String id;

    @NotNull
    @Field(value = "Title")
    @ApiModelProperty(required = true, example = "what I did today")
    private String title;

    @NotNull
    @Field(value = "Content")
    @ApiModelProperty(required = true, example = "wrote a boring post")
    private String content;
} 
