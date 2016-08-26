/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import com.axiell.ehub.AbstractEhubRepositoryTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.language.ILanguageAdminController;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import com.axiell.ehub.provider.record.platform.IPlatformAdminController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/axiell/ehub/admin-controller-context.xml")
public class EhubConsumerRepositoryTest extends AbstractEhubRepositoryTest<DevelopmentData> {

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
        return new DevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController, languageAdminController,
                platformAdminController);
    }

    @Test
    @Rollback(true)
    public void testFindEhubConsumer() {
        EhubConsumer ehubConsumer = ehubConsumerRepository.findOne(developmentData.getEhubConsumerId());
        Assert.assertEquals(2, ehubConsumer.getProperties().size());
        Assert.assertEquals(1, ehubConsumer.getContentProviderConsumers().size());
    }

    @Test
    @Rollback(true)
    public void testNoEhubConsumerFound() {
        EhubConsumer ehubConsumer = ehubConsumerRepository.findOne(0L);
        Assert.assertNull(ehubConsumer);
    }

    @Test
    @Rollback(true)
    public void testFindAllOrderedByDescription() {
        Map<EhubConsumer.EhubConsumerPropertyKey, String> ehubConsumerProperties = new HashMap<>();
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, DevelopmentData.ARENA_PALMA_URL);
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, DevelopmentData.ARENA_AGENCY_M_IDENTIFIER);
        EhubConsumer expEhubConsumer2 =
                new EhubConsumer("Description1", DevelopmentData.EHUB_CONSUMER_SECRET_KEY, ehubConsumerProperties, DevelopmentData.DEFAULT_LANGUAGE);
        ehubConsumerRepository.save(expEhubConsumer2);

        List<EhubConsumer> ehubConsumers = ehubConsumerRepository.findAllOrderedByDescription();
        Assert.assertEquals(2, ehubConsumers.size());
        EhubConsumer actEhubConsumer1 = ehubConsumers.get(0);
        Assert.assertEquals(expEhubConsumer2.getDescription(), actEhubConsumer1.getDescription());
        EhubConsumer actEhubConsumer2 = ehubConsumers.get(1);
        Assert.assertEquals(developmentData.getEhubConsumer().getDescription(), actEhubConsumer2.getDescription());
        ehubConsumerRepository.delete(expEhubConsumer2);
    }
}
