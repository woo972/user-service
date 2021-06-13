package com.wowls.userservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/userservice")
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello user service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("userservice-request") String header) {
      log.info(header);
      return "user service message";
    }

    @GetMapping("/check2")
    public String check2(@RequestHeader("global-header") String header) {
        log.info(header);
        return "global filter test";
    }

    @GetMapping("/check")
    public String check() {
        return "custom filter check";
    }
}
