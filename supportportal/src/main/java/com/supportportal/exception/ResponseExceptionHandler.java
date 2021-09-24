package com.supportportal.exception;

import com.supportportal.domain.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ResponseExceptionHandler {

    protected ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .httpStatusCode(httpStatus.value())
                        .httpStatus(httpStatus)
                        .reason(httpStatus.getReasonPhrase())
                        .message(message)
                        .build(),
                httpStatus);
    }

}
