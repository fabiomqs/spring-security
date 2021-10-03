package br.com.alura.alurapic.exception.domain;

public class CommentNotAllowedException extends Exception {
    public CommentNotAllowedException(String message) {
        super(message);
    }
}
