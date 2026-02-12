package com.axiell.ehub.local.it.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.local.EhubApplication;
import com.axiell.ehub.local.consumer.IConsumerAdminController;
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

@SpringBootTest(classes = {EhubApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ContentProviderRepositoryIT extends AbstractEhubRepositoryIT<DevelopmentData> {
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
    public void testElibContentProvider() {
        ContentProvider contentProvider = contentProviderAdminController.getContentProvider(CONTENT_PROVIDER_TEST_EP);
        Assertions.assertEquals(CONTENT_PROVIDER_TEST_EP, contentProvider.getName());
        Assertions.assertEquals(1, contentProvider.getProperties().size());
    }


}
