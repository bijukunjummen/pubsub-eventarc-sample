package org.bk.config;

import org.bk.model.PubSubProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(PubSubProperties.class)
@TestPropertySource(properties = {"pubsub.topic=sampletopic", "pubsub.subscriberId=test"})
class PubSubPropertiesTest {

    @Autowired
    private PubSubProperties pubSubProperties;

    @Test
    void testLoadingOfProps() {
        assertThat(pubSubProperties.topic()).isEqualTo("sampletopic");
        assertThat(pubSubProperties.subscriberId()).isEqualTo("test");
    }
}
