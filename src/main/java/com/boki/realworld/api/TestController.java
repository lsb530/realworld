package com.boki.realworld.api;

import com.boki.realworld.resolver.ClientIP;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Operation(summary = "test hello", description = "hello api example")
    @GetMapping("/header")
    public ResponseEntity<String> test() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "boki");
        return new ResponseEntity<>("Custom header set", headers, HttpStatus.OK);
    }

    @GetMapping("/customHeader")
    ResponseEntity<String> customHeader() {
        return ResponseEntity.ok()
            .header("Custom-Header", "foo")
            .body("Custom header set");
    }

    @GetMapping("/customCookie")
    ResponseEntity<String> customCookie() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "platform=mobile; Max-Age=604800; Path=/; Secure; HttpOnly");
        return ResponseEntity
            .status(HttpStatus.OK)
            .headers(headers)
            .build();
    }

    @GetMapping("/create")
    ResponseEntity<String> kkt() {
        return ResponseEntity.created(URI.create("/key"))
            .build();
    }

    @GetMapping("/age")
    ResponseEntity<String> age(
        @RequestParam("yearOfBirth") int yearOfBirth) {
        if (isInFuture(yearOfBirth)) {
            return new ResponseEntity<>(
                "Year of birth cannot be in the future",
                HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
            "Your age is " + calculateAge(yearOfBirth),
            HttpStatus.OK);
    }

    private String calculateAge(int yearOfBirth) {
        return Integer.toString(2021 - yearOfBirth + 1);
    }

    private boolean isInFuture(int yearOfBirth) {
        return yearOfBirth >= 2021;
    }


    @GetMapping("/redirect/origin")
    public ResponseEntity<Object> redirect() throws URISyntaxException {
        URI redirectUri = new URI(
            "http://localhost:8080/api/test/redirect/target?code=" + UUID.randomUUID());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
    }

    @GetMapping("/redirect/target")
    public String target(@RequestParam("code") long code) {
        System.out.println(">>> " + code);
        return "redirect param :" + code;
    }

    @GetMapping("/forward/origin")
    public String forward() throws URISyntaxException {
        URI forwardUri = new URI("https://www.naver.com");
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(forwardUri, String.class);
    }

    @GetMapping("/ip")
    private ResponseEntity<String> test1(@ClientIP String clientIp) {
        System.out.println("clientIP = " + clientIp);
        return ResponseEntity.ok(clientIp);
    }

}