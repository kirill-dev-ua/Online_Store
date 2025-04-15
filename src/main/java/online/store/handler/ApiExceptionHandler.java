package online.store.handler;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import online.store.dto.ErrorResponseDto;
import online.store.exceptions.BigSizeException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private ErrorResponseDto buildErrorDto(String message) {
        String requestId = MDC.get("requestId");
        return new ErrorResponseDto(requestId, message);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlerNotFoundException(EntityNotFoundException ex){
        log.error("Error: {}", ex.getMessage());
        return new ResponseEntity<>(buildErrorDto(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleAlreadyExistsException(EntityExistsException ex) {
        log.error("Already exists: {}", ex.getMessage());
        return new ResponseEntity<>(buildErrorDto(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BigSizeException.class)
    public ResponseEntity<ErrorResponseDto> handleBigSizeException(BigSizeException ex) {
        log.error("Invalid size: {}", ex.getMessage());
        return new ResponseEntity<>(buildErrorDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
