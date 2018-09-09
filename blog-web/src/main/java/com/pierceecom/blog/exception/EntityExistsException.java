package com.pierceecom.blog.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityExistsException extends RuntimeException {

    public EntityExistsException(final String message) {
        super(message);
    }
} 
