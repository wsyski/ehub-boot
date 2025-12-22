package com.axiell.ehub.provider;

import com.axiell.ehub.AbstractEhubRepositoryTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.config.DataSourceConfig;
import com.axiell.ehub.config.PersistenceConfig;
import com.axiell.ehub.consumer.EhubConsumerRepositoryTest;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.language.ILanguageAdminController;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DataSourceConfig.class, PersistenceConfig.class, EhubConsumerRepositoryTest.TestConfig.class})
public class ContentProviderRepositoryTest extends AbstractEhubRepositoryTest<DevelopmentData> {
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    @Autowired
    private IContentProviderAdminController contentProviderAdminController;
    @Autowired
    private IFormatAdminController formatAdminController;
    @Autowired
    private IConsumerAdminController consumerAdminController;
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
    public void testElibContentProvider() {
        ContentProvider contentProvider = contentProviderAdminController.getContentProvider(CONTENT_PROVIDER_TEST_EP);
        Assertions.assertEquals(CONTENT_PROVIDER_TEST_EP, contentProvider.getName());
        Assertions.assertEquals(1, contentProvider.getProperties().size());
    }

    @Configuration
    @ComponentScan(basePackages = "com.axiell.ehub")
    public static class TestConfig {
    }
}
