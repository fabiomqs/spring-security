package br.com.alura.logserver.service;

public interface LogService {
    void log(String type, String message, String stack, String url, String username);
}
