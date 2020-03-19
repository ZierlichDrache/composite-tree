package pl.solejnik.compositetree.config;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.solejnik.compositetree.exception.CannotRemoveRootException;
import pl.solejnik.compositetree.exception.ComponentNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CannotRemoveRootException.class)
    public ResponseEntity<String> handleCannotRemoveRootException(@NonNull final Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ComponentNotFoundException.class)
    public ResponseEntity<String> handleComponentNotFoundException(@NonNull final Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
