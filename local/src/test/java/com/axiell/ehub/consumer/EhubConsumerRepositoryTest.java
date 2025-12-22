package com.axiell.ehub.consumer;

import com.axiell.ehub.AbstractEhubRepositoryTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.config.DataSourceConfig;
import com.axiell.ehub.config.PersistenceConfig;
import com.axiell.ehub.language.ILanguageAdminController;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import com.axiell.ehub.provider.record.platform.IPlatformAdminController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes =  {DataSourceConfig.class, PersistenceConfig.class, EhubConsumerRepositoryTest.TestConfig.class})
public class EhubConsumerRepositoryTest extends AbstractEhubRepositoryTest<DevelopmentData> {

    @Configuration
    @ComponentScan(basePackages = "com.axiell.ehub")
    public static class TestConfig {
    }

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
     * @see com.axiell.ehub.AbstractEhubRepositoryTest#initDevelopmentData()
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
