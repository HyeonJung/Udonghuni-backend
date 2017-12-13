package com.hj.udonghuni.web.exception;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOException;
import javax.persistence.EntityNotFoundException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hj.udonghuni.web.support.ApiErrorResponseBody;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public CustomResponseEntityExceptionHandler() {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            body = new ApiErrorResponseBody(status, ex.getLocalizedMessage());
        }
        log.info("### {} {}, {}, {}", status, status.name(), ex.getClass().getName(), ex.getLocalizedMessage());
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    // 400

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleBadRequest(final ConstraintViolationException ex,
            final WebRequest request) {
        ApiErrorResponseBody apiError = new ApiErrorResponseBody(HttpStatus.BAD_REQUEST,
                ex.getConstraintName() + ": 제약 조건 위반");
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(),
                request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleBadRequest(final DataIntegrityViolationException ex,
            final WebRequest request) {
        ApiErrorResponseBody apiError =
                new ApiErrorResponseBody(HttpStatus.BAD_REQUEST, "데이터 무결성 위반");
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException ex, final HttpHeaders headers,
            final HttpStatus status, final WebRequest request) {
        ApiErrorResponseBody apiError = new ApiErrorResponseBody(status, "요청 본문을 읽을 수 없습니다.");
        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex, final HttpHeaders headers,
            final HttpStatus status, final WebRequest request) {
        List<String> errors = new ArrayList<String>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiErrorResponseBody apiError =
                new ApiErrorResponseBody(status, "요청 본문의 파라미터가 유효하지 않습니다.", errors);
        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<String>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiErrorResponseBody apiError =
                new ApiErrorResponseBody(status, "요청 본문의 파라미터가 유효하지 않습니다.", errors);
        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    // 403

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(final Exception ex,
            final WebRequest request) {
        ApiErrorResponseBody apiError =
                new ApiErrorResponseBody(HttpStatus.FORBIDDEN, "리소스 접근에 대한 권한이 없습니다.");
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(),
                request);
    }

    // 404

    @ExceptionHandler(
            value = {EntityNotFoundException.class, CustomResourceNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(final RuntimeException ex,
            final WebRequest request) {
        ApiErrorResponseBody apiError =
                new ApiErrorResponseBody(HttpStatus.NOT_FOUND, "리소스가 없습니다.");
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(),
                request);
    }
    
    // 415
    
//    @ExceptionHandler(
//    		value = {MultipartException.class})
//    public ResponseEntity<Object> handleMultipart(final RuntimeException ex,
//    		final WebRequest request) {
//    	ApiErrorResponseBody apiError = new ApiErrorResponseBody(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
//    			ex.getLocalizedMessage());
//    	
//    	return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(),
//                request); 
//    }

    // 500

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class,
            IllegalStateException.class, RuntimeException.class, IOException.class, IIOException.class, MultipartException.class})
    public ResponseEntity<Object> handleInternal(final RuntimeException ex,
            final WebRequest request) {
        ApiErrorResponseBody apiError = new ApiErrorResponseBody(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getLocalizedMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(),
                request);
    }
    

}
