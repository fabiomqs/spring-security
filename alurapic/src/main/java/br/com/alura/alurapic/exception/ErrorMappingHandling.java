package br.com.alura.alurapic.exception;

import br.com.alura.alurapic.domain.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ErrorMappingHandling extends ResponseExceptionHandler implements ErrorController {

    public static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<HttpResponse> notFound404() {
        return createHttpResponse(NOT_FOUND, "There is no mapping for this URL");
    }

    //@Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
