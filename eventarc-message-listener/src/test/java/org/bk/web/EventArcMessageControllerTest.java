package org.bk.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@WebFluxTest(controllers = EventArcMessageController.class)
class EventArcMessageControllerTest {
    @Autowired
    private WebTestClient webTestClient;


    @Test
    void testHandlePayload() {
        webTestClient.post().uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue("""
                        {
                          "subscription": "projects/test-project/subscriptions/my-subscription",
                          "message": {
                            "attributes": {
                              "attr1": "attr1-value"
                            },
                            "data": "dGVzdCBtZXNzYWdlIDM=",
                            "messageId": "message-id",
                            "publishTime": "2021-02-05T04:06:14.109Z",
                            "orderingKey": "ordering-key"
                          }
                        }
                        """))
                .exchange()
                .expectStatus().isOk();
    }

}
