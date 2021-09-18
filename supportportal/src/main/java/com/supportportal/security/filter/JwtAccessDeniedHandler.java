package com.supportportal.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportportal.domain.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static com.supportportal.security.constant.SecurityConstant.ACCESS_DENIED_MESSAGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
        log.debug("Pre-authenticated entry point called. Rejecting access");
        HttpResponse httpResponse = HttpResponse.builder()
                .httpStatusCode(UNAUTHORIZED.value())
                .httpStatus(UNAUTHORIZED)
                .reason(UNAUTHORIZED.getReasonPhrase().toUpperCase())
                .message(ACCESS_DENIED_MESSAGE)
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }
}