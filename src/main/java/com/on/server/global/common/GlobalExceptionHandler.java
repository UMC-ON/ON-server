package com.on.server.global.common;

import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.common.exceptions.InternalServerException;
import com.on.server.global.common.exceptions.ServiceUnavailableException;
import com.on.server.global.common.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
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
    public CommonResponse<Void> handleBadRequestException(BadRequestException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return CommonResponse.error(exception);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleConstraintViolationException(ConstraintViolationException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return CommonResponse.error(new BadRequestException(ResponseCode.INVALID_PARAMETER));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleIllegalArgumentException(IllegalArgumentException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return CommonResponse.error(new BadRequestException(ResponseCode.INVALID_PARAMETER));
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleBadHttpRequestMethodException(HttpRequestMethodNotSupportedException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return CommonResponse.error(new BadRequestException(ResponseCode.INVALID_HTTP_METHOD, "Invalid request http method (GET, POST, PUT, DELETE)"));
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse<Void> handleUnauthorizedException(UnauthorizedException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return CommonResponse.error(exception);
    }

    @ExceptionHandler(value = {ServiceUnavailableException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public CommonResponse<Void> handleServiceUnavailableException(ServiceUnavailableException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return CommonResponse.error(exception);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse<Void> handleAccessDeniedException(AccessDeniedException exception) {
        GlobalExceptionHandler.log.debug("[Error message]", exception);
        return CommonResponse.error(new UnauthorizedException(ResponseCode.API_NOT_ALLOWED));
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<Void> unknownException(Exception exception) {
        GlobalExceptionHandler.log.error("[Error message]", exception);
        return CommonResponse.error(new InternalServerException(ResponseCode.INTERNAL_SERVER, exception.getMessage()));
    }

}