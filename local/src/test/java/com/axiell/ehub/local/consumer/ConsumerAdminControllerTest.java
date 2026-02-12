package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ConsumerAdminControllerTest {
    private static final Long EHUB_CONSUMER_ID = 1L;
    private IConsumerAdminController underTest;

    @Mock
    private IEhubConsumerRepository ehubConsumerRepository;
    @Mock
    private IContentProviderConsumerRepository contentProviderConsumerRepository;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private EhubConsumer ehubConsumer;
    private ContentProviderConsumer actualContentProviderConsumer;

    @BeforeEach
    public void setUpConsumerAdminController() {
        underTest = new ConsumerAdminController();
        ReflectionTestUtils.setField(underTest, "ehubConsumerRepository", ehubConsumerRepository);
        ReflectionTestUtils.setField(underTest, "contentProviderConsumerRepository", contentProviderConsumerRepository);
    }

    @Test
    public void addContentProviderConsumerToEhubConsumer() {
        givenEhubConsumer();
        givenContentProviderConsumer();
        givenContentProviderConsumers();
        whenAddContentProviderConsumerToEhubConsumer();
        thenActualContentProviderConsumerEqualsExpectedContentProviderConsumer();
    }

    private void givenEhubConsumer() {
        given(ehubConsumerRepository.findById(EHUB_CONSUMER_ID)).willReturn(Optional.ofNullable(ehubConsumer));
        given(underTest.save(ehubConsumer)).willReturn(ehubConsumer);
    }

    private void givenContentProviderConsumer() {
        given(contentProviderConsumerRepository.save(contentProviderConsumer)).willReturn(contentProviderConsumer);
    }

    private void givenContentProviderConsumers() {
        Set<ContentProviderConsumer> contentProviderConsumers = new HashSet<>();
        contentProviderConsumers.add(contentProviderConsumer);
        given(ehubConsumer.getContentProviderConsumers()).willReturn(contentProviderConsumers);
    }

    private void whenAddContentProviderConsumerToEhubConsumer() {
        actualContentProviderConsumer = underTest.add(EHUB_CONSUMER_ID, contentProviderConsumer);
    }

    private void thenActualContentProviderConsumerEqualsExpectedContentProviderConsumer() {
        Assertions.assertNotNull(actualContentProviderConsumer);
        Assertions.assertEquals(contentProviderConsumer, actualContentProviderConsumer);
    }
}
