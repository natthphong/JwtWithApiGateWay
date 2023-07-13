package com.restaunrant.server.server.controller.rest;


import com.restaunrant.server.server.JwtUtil;
import com.restaunrant.server.server.model.Credential;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class Controller {


    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Credential credential) {

        if (credential.getUsername().equalsIgnoreCase("tar")
                && credential.getPassword().equalsIgnoreCase("123456")
        ) {

            String token = jwtUtil.getToken(credential);
            return new ResponseEntity<>(Map.of("TOKEN", token), HttpStatus.OK);
        }

        return new ResponseEntity<>(Map.of("ERROR", "USERNAME OR PASSWORD INVALIDATE"), HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/test")
    public Credential test(ServerHttpRequest httpRequest) {

        String authHeader = httpRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7);
        }
        log.info("authHeader {}" , authHeader);
        return jwtUtil.parseToken(authHeader);
    }
}
