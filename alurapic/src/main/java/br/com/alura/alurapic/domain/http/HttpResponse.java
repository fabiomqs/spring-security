package br.com.alura.alurapic.domain.http;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpResponse {

    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
    private Date timestamp = new Date();
}
