package br.com.alura.alurapic.exception.domain;

public class EmailExistException extends Exception {
    public EmailExistException(String message) {
        super(message);
    }
}
