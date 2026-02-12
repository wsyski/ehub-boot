package com.axiell.ehub.local.it.consumer;

import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.local.EhubApplication;
import com.axiell.ehub.local.consumer.IConsumerAdminController;
import com.axiell.ehub.local.consumer.IEhubConsumerRepository;
import com.axiell.ehub.local.it.AbstractEhubRepositoryIT;
import com.axiell.ehub.local.it.DevelopmentData;
import com.axiell.ehub.local.language.ILanguageAdminController;
import com.axiell.ehub.local.provider.IContentProviderAdminController;
import com.axiell.ehub.local.provider.record.format.IFormatAdminController;
import com.axiell.ehub.local.provider.record.platform.IPlatformAdminController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = {EhubApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class EhubConsumerRepositoryIT extends AbstractEhubRepositoryIT<DevelopmentData> {

    @Autowired
    private IContentProviderAdminController contentProviderAdminController;
    @Autowired
    private IFormatAdminController formatAdminController;
    @Autowired
    private IConsumerAdminController consumerAdminController;
    @Autowired
    private IEhubConsumerRepository ehubConsumerRepository;
    @Autowired
    private ILanguageAdminController languageAdminController;
    @Autowired
    private IPlatformAdminController platformAdminController;

    /**
     * @see AbstractEhubRepositoryIT#initDevelopmentData()
     */
    @Override
    protected DevelopmentData initDevelopmentData() {
        return new DevelopmentData(
                contentProviderAdminController,
                formatAdminController,
                consumerAdminController,
                languageAdminController,
                platformAdminController);
    }

    @Test
    @Rollback(true)
    public void testFindEhubConsumer() {
        EhubConsumer ehubConsumer = ehubConsumerRepository.findById(developmentData.getEhubConsumerId()).orElse(null);
        Map<EhubConsumer.EhubConsumerPropertyKey, String> properties = ehubConsumer.getProperties();
        Assertions.assertEquals(2, properties.size());
        Assertions.assertEquals(1, ehubConsumer.getContentProviderConsumers().size());
    }

    @Test
    @Rollback(true)
    public void testNoEhubConsumerFound() {
        EhubConsumer ehubConsumer = ehubConsumerRepository.findById(0L).orElse(null);
        Assertions.assertNull(ehubConsumer);
    }

    @Test
    @Rollback(true)
    public void testFindAllOrderedByDescription() {
        Map<EhubConsumer.EhubConsumerPropertyKey, String> ehubConsumerProperties = new HashMap<>();
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_LOCAL_API_ENDPOINT, DevelopmentData.ARENA_LOCAL_API_ENDPOINT);
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, DevelopmentData.ARENA_AGENCY_M_IDENTIFIER);
        EhubConsumer expEhubConsumer2 =
                new EhubConsumer("Description1", DevelopmentData.EHUB_CONSUMER_SECRET_KEY, ehubConsumerProperties, DevelopmentData.DEFAULT_LANGUAGE);
        ehubConsumerRepository.save(expEhubConsumer2);

        List<EhubConsumer> ehubConsumers = ehubConsumerRepository.findAllOrderedByDescription();
        Assertions.assertEquals(2, ehubConsumers.size());
        EhubConsumer actEhubConsumer1 = ehubConsumers.get(0);
        Assertions.assertEquals(expEhubConsumer2.getDescription(), actEhubConsumer1.getDescription());
        EhubConsumer actEhubConsumer2 = ehubConsumers.get(1);
        Assertions.assertEquals(developmentData.getEhubConsumer().getDescription(), actEhubConsumer2.getDescription());
        ehubConsumerRepository.delete(expEhubConsumer2);
    }
}
