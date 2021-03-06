package com.wowls.userservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HelloController {

    private final Environment env;

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
    public String check(HttpServletRequest request)
    {
        log.info("port:: {} {}", env.getProperty("local.server.port"), request.getServerPort());
        return "custom filter check";
    }
}
