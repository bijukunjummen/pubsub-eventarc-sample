package org.bk.web;

import org.bk.model.Message;
import org.bk.service.PubSubService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@WebFluxTest(controllers = PublishMessageController.class)
class PublishMessageControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PubSubService pubSubService;

    @Test
    void testBasicPublish() {
        when(pubSubService.publish(any(Message.class)))
                .thenReturn(Mono.empty());

        webTestClient.post().uri("/messages")
                .body(fromValue(new Message("1", "one")))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(""" 
                        {
                        "id": "1",
                        "payload": "one"
                        } """);
    }

}
