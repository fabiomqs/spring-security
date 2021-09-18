package com.supportportal.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportportal.domain.HttpResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static com.supportportal.constant.SecurityConstant.*;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException arg2) throws IOException {
        log.debug("Pre-authenticated entry point called. Rejecting access");
        HttpResponse httpResponse = HttpResponse.builder()
                .httpStatusCode(FORBIDDEN.value())
                .httpStatus(FORBIDDEN)
                .reason(FORBIDDEN.getReasonPhrase().toUpperCase())
                .message(FORBIDDEN_MESSAGE)
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }
}
