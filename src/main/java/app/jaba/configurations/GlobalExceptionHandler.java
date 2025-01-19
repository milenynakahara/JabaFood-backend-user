package app.jaba.configurations;

import app.jaba.dtos.ErrorResponseDto;
import app.jaba.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(Exception ex, WebRequest request, HttpStatus status) {
        HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
        String requestPath = httpRequest.getRequestURI();
        String httpMethod = httpRequest.getMethod();

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                ex.getMessage(),
                httpMethod,
                requestPath,
                LocalDateTime.now().toString(),
                status.value()
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler({EmailAlreadyInUseException.class, LoginAlreadyInUseException.class})
    public ResponseEntity<ErrorResponseDto> handleAlreadyInUseException(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            AddressNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDto> handleEntityNotFound(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            AddressMandatoryFieldException.class,
            EmailFormatException.class,
            InvalidPageValueException.class,
            InvalidPasswordException.class,
            InvalidSizeValueException.class,
            MissingPasswordException.class,
            PasswordNotMatchException.class,
            UserMandatoryFieldException.class,
            InvalidPasswordException.class,
            UserMandatoryFieldException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            SaveUserException.class,
            SaveAddressException.class,
            UpdatePasswordException.class
    })
    public ResponseEntity<ErrorResponseDto> handleSaveException(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}