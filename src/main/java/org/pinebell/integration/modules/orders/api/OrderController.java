package org.pinebell.integration.modules.orders.api;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    
    @GetMapping("/test")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String test() {
        return "123";
    }
}
