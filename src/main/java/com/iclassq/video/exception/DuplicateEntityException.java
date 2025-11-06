package com.iclassq.video.exception;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(String entity, String field, Object value) {
        super(String.format("%s con %s '%s' ya existe", entity, field, value));
    }
}
