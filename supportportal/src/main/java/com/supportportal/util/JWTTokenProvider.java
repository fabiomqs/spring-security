package com.supportportal.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.supportportal.domain.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.supportportal.constant.SecurityConstant.*;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.util.Arrays.stream;

@Slf4j
@Component
public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secret;
    
    public String generateJWTToken(UserPrincipal userPrincipal) {
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(GET_ARRAYS_LLC)
                .withAudience(GET_ARRAYS_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(secret.getBytes()));

    }

    public List<GrantedAuthority> getAuthorities(String token) {
        //String[] claims = getClaimsFromToken(token);
        return stream(getClaimsFromToken(token))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Authentication getAuthentication(String username,
                                            List<GrantedAuthority> authorities,
                                            HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        usernamePasswordAuthenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }

    public boolean isTokenValid(String username, String token) {
        return StringUtils.isNoneEmpty(username) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getJWTVerifier().verify(token).getExpiresAt();
        return expirationDate.before(new Date());
    }

    public String getSubject(String token) {
        return getJWTVerifier().verify(token).getSubject();
    }

    private String[] getClaimsFromToken(String token) {
        return getJWTVerifier()
                .verify(token)
                .getClaim(AUTHORITIES)
                .asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier jwtVerifier;
        try {
            jwtVerifier = JWT.require(HMAC512(secret)).withIssuer(GET_ARRAYS_LLC).build();
        } catch (JWTVerificationException exception) {
            log.error(TOKEN_CANNOT_BE_VERIFIED, exception);
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return jwtVerifier;
    }

    private String[] getClaimsFromUser(UserPrincipal user) {
        //List<String> authorities = new ArrayList<>();
        //for(GrantedAuthority grantedAuthority : user.getAuthorities()) {
        //    authorities.add(grantedAuthority.getAuthority());
        //}
        //return authorities.toArray(new String[0]);
        return user.getAuthorities().stream().map(authority -> {
            return authority.getAuthority();
        }).collect(Collectors.toList()).toArray(new String[0]);
    }
}
