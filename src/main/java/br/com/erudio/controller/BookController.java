package br.com.erudio.controller;

import br.com.erudio.dto.Exchange;
import br.com.erudio.environment.InstanceInformationService;
import br.com.erudio.model.Book;
import br.com.erudio.proxy.ExchangeProxy;
import br.com.erudio.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;

@Tag(name="Book Endpoint")
@RestController
@RequestMapping("book-service")
public class BookController {

    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private InstanceInformationService info;

    @Autowired
    private BookRepository repository;

    @Autowired
    private ExchangeProxy proxy;

    @Operation(summary = "Find a specific book by your ID")
    @GetMapping(value = "/{id}/{currency}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
/*    public Book findBook(@PathVariable("id") Long id,
                         @PathVariable("currency") String currency) {
        return new Book(
                1L,
                "Nigel Pulton",
                "Docker Deep Dive",
                new Date(),
                15.8,
                "BRL",
                info.retrieveServerPort()
        );
    }*/

    /*public Book findBook(@PathVariable("id") Long id,
                         @PathVariable("currency") String currency) {
        var book = repository.findById(id).orElseThrow();
        book.setEnvironment("PORT " + info.retrieveServerPort());
        book.setCurrency(currency);
        return book;
    }*/

    /*public Book findBook(@PathVariable("id") Long id,
                         @PathVariable("currency") String currency) {

        var book = repository.findById(id).orElseThrow();
        book.setEnvironment("PORT " + info.retrieveServerPort());
        book.setCurrency(currency);

        HashMap<String, String> params = new HashMap<>();
        params.put("amount",book.getPrice().toString());
        params.put("from","USD");
        params.put("to",currency);

        var response = new RestTemplate().getForEntity("http://localhost:8000/exchange-service/" +
                "{amount}/{from}/{to}", Exchange.class, params);

        Exchange exchange = response.getBody();
        book.setPrice(exchange.getConvertedValue());
        return book;
    }
    */

    public Book findBook(@PathVariable("id") Long id,
                         @PathVariable("currency") String currency) {
        logger.info("findBook is called with -> {}, {}",id, currency);
        String port = info.retrieveServerPort();
        var book = repository.findById(id).orElseThrow();
        Exchange exchange = proxy.getExchange(book.getPrice(), "USD", currency);
        book.setPrice(exchange.getConvertedValue());
        book.setEnvironment("Book Port: " + port + " Exchange Port: " + exchange.getEnvironment());
        book.setCurrency(currency);
        return book;
    }
}
