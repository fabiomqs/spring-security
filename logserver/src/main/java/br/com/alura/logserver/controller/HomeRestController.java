package br.com.alura.logserver.controller;

import br.com.alura.logserver.service.LogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class HomeRestController {

    private final LogService logService;

    public HomeRestController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping("/log")
    public void log( @RequestParam("type")  String type,
                     @RequestParam("message") String message,
                     @RequestParam("stack") String stack,
                     @RequestParam("url") String url,
                     @RequestParam("username") String username)  {

        logService.log(type, message, stack, url, username);

    }
}
