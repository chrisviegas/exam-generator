package dev.viegas.examgenerator.security.filter;

import io.jsonwebtoken.Jwts;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.KeyPair;
import java.util.concurrent.TimeUnit;

public class Constants {
    public static final KeyPair keyPair = Jwts.SIG.ES512.keyPair().build();
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 86400000L; //1day

    public static void main(String[] args) {
        System.out.println(TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS));
        System.out.println(new BCryptPasswordEncoder().encode("viegas"));
    }

}
