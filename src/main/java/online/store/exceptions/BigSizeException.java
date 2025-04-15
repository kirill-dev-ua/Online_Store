package online.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BigSizeException extends RuntimeException {
    public BigSizeException(String message) {
        super(message);
    }
}
