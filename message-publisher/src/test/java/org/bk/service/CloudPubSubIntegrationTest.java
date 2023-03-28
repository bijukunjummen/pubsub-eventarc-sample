package org.bk.service;

import org.bk.model.Message;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PubSubEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

@Testcontainers
@SpringBootTest
@Disabled
@ContextConfiguration(initializers = CloudPubSubIntegrationTest.PropertiesInitializer.class)
class CloudPubSubIntegrationTest {
    @Container
    private static final PubSubEmulatorContainer emulator =
            new PubSubEmulatorContainer(
                    DockerImageName.parse("gcr.io/google.com/cloudsdktool/cloud-sdk:316.0.0-emulators"));

    @Autowired
    private CloudPubSubService cloudPubSubService;

    @MockBean
    private MessageProcessor messageProcessor;

    @Test
    void testPubSubBasicWiring() {
        Message message = new Message("id", "payload");
        StepVerifier.create(cloudPubSubService.publish(message))
                .expectComplete();

        StepVerifier.create(cloudPubSubService.retrieve())
                .expectNext(message)
                .thenCancel()
                .verify();
    }

    static class PropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "pubsub.topic=sampletopic",
                    "pubsub.subscriber-id=subid",
                    "pubsub.project=sampleproj",
                    "spring.cloud.gcp.pubsub.emulator-host=" + emulator.getEmulatorEndpoint()
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
