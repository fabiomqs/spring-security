package com.supportportal.resources;

import com.supportportal.domain.User;
import com.supportportal.domain.UserPrincipal;
import com.supportportal.exception.ErrorMappingHandling;
import com.supportportal.security.util.JwtTokenProvider;
import com.supportportal.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.supportportal.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("")
public class HomeResource extends ErrorMappingHandling {

    //@GetMapping
    //public String home() {
    //    return "works!";
    //}

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public HomeResource(UserService userService, JwtTokenProvider jwtTokenProvider) {
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
