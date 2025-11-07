package br.com.erudio.controller;

import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/* Exemplo de Circuit Break*/
@RestController
@RequestMapping("book-service")
public class FooBarController {

    private Logger logger = LoggerFactory.getLogger((FooBarController.class));

    @GetMapping("/foo-bar")
//    @Retry(name = "default") //3 tentativas
    @Retry(name = "foo-bar") //tentativas personalizadas
    public String fooBar() {
        logger.info("Request to foobar is received");
        var response = new RestTemplate()
                .getForEntity("http://localhost:8080/foo-bar", String.class);
//        return "Foo Bar!!";
        return response.getBody();
    }


}
