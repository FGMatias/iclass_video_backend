package com.iclassq.video.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String entity, Integer id) {
        super(String.format("%s con ID %d no encontrado", entity, id));
    }

    public ResourceNotFoundException(String entity, String field, Object value) {
        super(String.format("%s con %s '%s' no encontrado", entity, field, value));
    }
}
