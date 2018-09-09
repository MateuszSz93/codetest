package com.pierceecom.blog.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundException extends RuntimeException {

    public NotFoundException(final String message) {
        super(message);
    }
} 
