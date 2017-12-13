package com.hj.udonghuni.web.support;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiErrorResponseBody {

    private HttpStatus status;
    private String message;
    private List<String> errors;
    
    public ApiErrorResponseBody(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
 
    public ApiErrorResponseBody(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        this.errors = Arrays.asList(error);
    }
    
    public ApiErrorResponseBody(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }
 
}
