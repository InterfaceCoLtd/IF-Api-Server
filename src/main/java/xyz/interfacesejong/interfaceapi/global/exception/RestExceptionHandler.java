package xyz.interfacesejong.interfaceapi.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.interfacesejong.interfaceapi.global.exception.dto.BaseExceptionResponse;

import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseExceptionResponse> handleEntityNotFoundException(EntityNotFoundException exception){
        log.info("{} : {}",exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<>(new BaseExceptionResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<BaseExceptionResponse> handleEntityExistsException(EntityExistsException exception){
        log.info("{} : {}",exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<>(new BaseExceptionResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseExceptionResponse> handleIllegalArgumentException(IllegalArgumentException exception){
        log.info("{} : {}",exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<>(new BaseExceptionResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<BaseExceptionResponse> handleIllegalStateException(IllegalStateException exception){
        log.info("{} : {}",exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<>(new BaseExceptionResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<BaseExceptionResponse> messagingException(MessagingException exception){
        log.info("{} : {}",exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<>(new BaseExceptionResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<BaseExceptionResponse> authenticationServiceException(AuthenticationServiceException exception){
        log.info("{} : {}",exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<>(new BaseExceptionResponse(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

}
