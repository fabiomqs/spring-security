package br.com.alura.logserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static br.com.alura.logserver.service.Constant.*;

@Slf4j
@Service
public class LogServiceImpl implements LogService {
    @Override
    public void log(String type, String message, String stack, String url, String username) {
        switch (type) {
            case INFO:
                log.info(INFO_PREFIX + "--------------------------- Log received start ------------------------------");
                log.info(INFO_PREFIX + "Url " + url);
                log.info(INFO_PREFIX + "Username "+ username);
                log.info(INFO_PREFIX + "Message " + message);
                log.info(INFO_PREFIX + stack);
                log.info(INFO_PREFIX + "--------------------------- Log received end ------------------------------");
                break;
            case DEBUG:
                log.debug(DEBUG_PREFIX + "--------------------------- Log received start ------------------------------");
                log.debug(DEBUG_PREFIX + "Url "  + url);
                log.debug(DEBUG_PREFIX + "Username " + username);
                log.debug(DEBUG_PREFIX + "Message " + message);
                log.debug(DEBUG_PREFIX + stack);
                log.debug(DEBUG_PREFIX + "--------------------------- Log received end ------------------------------");
                break;
            case ERROR:
                log.error(ERROR_PREFIX + "--------------------------- Log received start ------------------------------");
                log.error(ERROR_PREFIX + "Url "  + url);
                log.error(ERROR_PREFIX + "Username " + username);
                log.error(ERROR_PREFIX + "Message " + message);
                log.error(ERROR_PREFIX + stack);
                log.error(ERROR_PREFIX + "--------------------------- Log received end ------------------------------");
                break;
            default:
                break;
        }
    }
}
