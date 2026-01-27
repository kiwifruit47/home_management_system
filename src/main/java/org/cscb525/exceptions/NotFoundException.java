package org.cscb525.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Class<?> entityClass, Long id) {
        super(entityClass.getSimpleName() + " with id " + id + " not found");
    }

    public NotFoundException(Class<?> entityClass, Long id, Throwable cause) {
        super(entityClass.getSimpleName() + " with id " + id + " not found", cause);
    }

    public NotFoundException(Class<?> entityClass, Throwable cause) {
        super(entityClass.getSimpleName() + " not found", cause);
    }
}
