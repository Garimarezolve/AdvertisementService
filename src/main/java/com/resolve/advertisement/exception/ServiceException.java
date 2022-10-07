package com.resolve.advertisement.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ServiceException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private String errorCode;
    private HttpStatus httpStatus = HttpStatus.OK;
    private Exception ex;

}
