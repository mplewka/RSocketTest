package com.example.consumer;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.MetadataExtractor;
import org.springframework.messaging.rsocket.MetadataExtractorRegistry;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    RSocket rSocket() {
        return RSocketFactory
                .connect()
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON_VALUE)
                .frameDecoder(PayloadDecoder.ZERO_COPY)
                .transport(TcpClientTransport.create(7000))
                .start()
                .block();
    }

    @Bean
    RSocketRequester requester(RSocketStrategies rSocketStrategies) {
        return RSocketRequester.builder()
                .rsocketFactory(factory -> factory
                    .dataMimeType(MimeTypeUtils.APPLICATION_JSON_VALUE)
                    .frameDecoder(PayloadDecoder.ZERO_COPY))
                .rsocketStrategies(rSocketStrategies)
                .connect(TcpClientTransport.create(7000))
                .retry().block();
//        return RSocketRequester.wrap(this.rSocket(),
//                MimeTypeUtils.APPLICATION_JSON,
//                new MimeType("message", "x.rsocket.composite-metadata.v0"),
//                rSocketStrategies);
    }

}

@RestController
class GreetingsRestController {

    private final RSocketRequester requester;

    GreetingsRestController(RSocketRequester requester) {
        this.requester = requester;
    }

    @GetMapping("/greet/{name}")
    Publisher<GreetingsResponse> greet(@PathVariable String name) {
        return this.requester
                .route("greet")
                .data(new GreetingsRequest(name))
                .retrieveMono(GreetingsResponse.class);
    }

}


class GreetingsRequest {

    private String name;

    public GreetingsRequest(String name) {
        this.name = name;
    }

    public GreetingsRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class GreetingsResponse {

    private String greeting;

    public GreetingsResponse() {}

    GreetingsResponse(String name) {
        this.greeting = "Hello " + name + " @ " + Instant.now();
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
