package com.example.demo.Controller;

import java.net.URI;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("")
public class ApiController {

    private final RedisTemplate<String, Object> redisTemplate;

    public ApiController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("")
    public ResponseEntity<?> HomePage() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("docs/index.html"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    // 테스트용
    @PostMapping("redis")
    public ResponseEntity<?> redisSave() {
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        vop.set("key", "value");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("redis")
    public ResponseEntity<?> redisGet() {
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        String value = Objects.requireNonNull(vop.get("key")).toString();

        return new ResponseEntity<>(value, HttpStatus.OK);
    }

    @DeleteMapping("redis")
    public ResponseEntity<?> redisDelete() {
        redisTemplate.delete("key");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
