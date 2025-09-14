package dev.viegas.examgenerator.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.viegas.examgenerator.models.ApplicationUser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static dev.viegas.examgenerator.security.filter.Constants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ApplicationUser user = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
            return authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        ZonedDateTime expTineUTC = ZonedDateTime.now(ZoneOffset.UTC).plus(EXPIRATION_TIME, ChronoUnit.MILLIS);
        String token = Jwts.builder()
                .subject(((ApplicationUser) authResult.getPrincipal()).getUsername())
                .expiration(Date.from(expTineUTC.toInstant()))
                .signWith(keyPair.getPrivate())
                .compact();

        token = TOKEN_PREFIX + token;
        String tokenJson = "{\"token\":" +addQuotes(token) + ",\"exp\":" +addQuotes(expTineUTC.toString()) + "}";
        response.getWriter().write(tokenJson);
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.addHeader(HEADER_STRING, token);
    }

    private String addQuotes(String value) {
        return new StringBuilder(300).append("\"").append(value).append("\"").toString();
    }
}
