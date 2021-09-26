package br.com.alura.alurapic.controller.api.v1;

import br.com.alura.alurapic.domain.User;
import br.com.alura.alurapic.domain.security.UserPrincipal;
import br.com.alura.alurapic.exception.ErrorMappingHandling;
import br.com.alura.alurapic.security.util.JwtTokenProvider;
import br.com.alura.alurapic.service.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.alura.alurapic.util.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("")
public class HomeRestController extends ErrorMappingHandling {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public HomeRestController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user)  {
        UserPrincipal userPrincipal = userService.login(user);
        HttpHeaders jwtHeader = getJwtHeaders(userPrincipal);
        return new ResponseEntity<>(userPrincipal.getUser(), jwtHeader, OK);
    }

    private HttpHeaders getJwtHeaders(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }
}
