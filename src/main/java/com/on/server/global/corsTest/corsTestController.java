package com.on.server.global.corsTest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cors")
public class corsTestController {

    @GetMapping("/hello")
    public String hello() {
        return "CORS test 성공";
    }

    @PostMapping("/hello/post")
    public String helloPost() {
        return "Post CORS test 성공";
    }

    @DeleteMapping("/hello/delete")
    public String helloDelete() {
        return "Delete CORS test 성공";
    }

}