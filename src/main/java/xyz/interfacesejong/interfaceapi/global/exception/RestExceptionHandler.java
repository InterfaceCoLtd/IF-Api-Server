package xyz.interfacesejong.interfaceapi.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.interfacesejong.interfaceapi.global.exception.dto.BaseExceptionResponse;

import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseExceptionResponse> handleEntityNotFoundException(EntityNotFoundException exception){
        return new ResponseEntity<>(new BaseExceptionResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<BaseExceptionResponse> handleEntityExistsException(EntityExistsException exception){
        return new ResponseEntity<>(new BaseExceptionResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseExceptionResponse> handleIllegalArgumentException(IllegalArgumentException exception){
        return new ResponseEntity<>(new BaseExceptionResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<BaseExceptionResponse> messagingException(MessagingException exception){
        return new ResponseEntity<>(new BaseExceptionResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

}
