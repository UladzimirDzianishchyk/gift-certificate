package com.epam.esm.exception;

public class EntityNotFoundByIdException extends RuntimeException{
    public EntityNotFoundByIdException(Long id) {

        super(String.format("%d not found",id));
    }
}
