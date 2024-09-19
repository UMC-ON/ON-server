package com.on.server.global.common;

import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.common.exceptions.ServiceUnavailableException;
import com.on.server.global.common.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;

@Component
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return ResponseEntity
                .status(
                        exception.getResponseCode().getCode()
                )
                .body(
                        ErrorResponseDto.of(
                                exception.getResponseCode(),
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return ResponseEntity
                .status(
                        ResponseCode.INVALID_PARAMETER.getCode()
                )
                .body(
                        ErrorResponseDto.of(
                                ResponseCode.INVALID_PARAMETER
                        )
                );
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return ResponseEntity
                .status(
                        ResponseCode.INVALID_PARAMETER.getCode()
                )
                .body(
                        ErrorResponseDto.of(
                                ResponseCode.INVALID_PARAMETER
                        )
                );
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleBadHttpRequestMethodException(HttpRequestMethodNotSupportedException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return ResponseEntity
                .status(
                        ResponseCode.INVALID_HTTP_METHOD.getCode()
                )
                .body(
                        ErrorResponseDto.of(
                                ResponseCode.INVALID_HTTP_METHOD,
                                "Invalid request http method (GET, POST, PUT, DELETE)")
                );
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedException(UnauthorizedException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return ResponseEntity
                .status(
                        exception.getResponseCode().getCode()
                )
                .body(
                        ErrorResponseDto.of(
                                exception.getResponseCode(),
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(value = {ServiceUnavailableException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleServiceUnavailableException(ServiceUnavailableException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return ResponseEntity
                .status(
                        exception.getResponseCode().getCode()
                )
                .body(
                        ErrorResponseDto.of(
                                exception.getResponseCode(),
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return ResponseEntity
                .status(
                        ResponseCode.API_NOT_ALLOWED.getCode()
                )
                .body(
                        ErrorResponseDto.of(
                                ResponseCode.API_NOT_ALLOWED
                        )
                );
    }

    @ExceptionHandler(value = {AuthorizationDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponseDto> handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return ResponseEntity
                .status(
                        ResponseCode.API_NOT_ALLOWED.getCode()
                )
                .body(
                        ErrorResponseDto.of(
                                ResponseCode.API_NOT_ALLOWED
                        )
                );
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDto> unknownException(Exception exception) {
        GlobalExceptionHandler.log.error("[Error message]", exception);
        return ResponseEntity
                .status(
                        ResponseCode.INTERNAL_SERVER.getCode()
                )
                .body(
                        ErrorResponseDto.of(
                                ResponseCode.INTERNAL_SERVER
                        )
                );
    }

}