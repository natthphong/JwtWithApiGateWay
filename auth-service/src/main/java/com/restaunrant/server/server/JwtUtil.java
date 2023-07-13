package com.restaunrant.server.server;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaunrant.server.server.model.Credential;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

@Component
public class JwtUtil {


    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    public String getToken(Credential credential)  {
        String body = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            body = objectMapper.writeValueAsString(credential);
            body  = Jwts.builder()
                    .setSubject(body)
                    //.setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getJwtExpirationTime() * 24))
                    .signWith(SignatureAlgorithm.HS256, SECRET)
                    .compact();

//            System.out.println("body:  " + body);

        }catch (Exception ex){
            ex.printStackTrace();
        }
       return body;
    }


    public Credential parseToken(String token) {
        if (token != null) {
            // parse the token.
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            if (claims != null) // we managed to retrieve a user
            {
                System.out.println(claims.getSubject());
                Credential auth = stringToAuth(claims.getSubject());
                return auth;
            }
        }

        return new Credential(null , null );
    }
    public Credential stringToAuth(String xx) {
        ObjectMapper mapper = new ObjectMapper();
        Credential auth = null;
        try {
            auth = mapper.readValue(xx, Credential.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return auth;
    }



    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}