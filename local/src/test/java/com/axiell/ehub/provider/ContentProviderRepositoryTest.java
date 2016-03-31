package com.axiell.ehub.provider;

import com.axiell.ehub.AbstractEhubRepositoryTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.language.ILanguageAdminController;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import com.axiell.ehub.provider.record.platform.IPlatformAdminController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/axiell/ehub/admin-controller-context.xml"})
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
        return new DevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController, languageAdminController,
                platformAdminController);
    }

    @Test
    @Rollback(true)
    public void testElibContentProvider() {
        ContentProvider contentProvider = contentProviderAdminController.getContentProvider(CONTENT_PROVIDER_TEST_EP);
        Assert.assertEquals(CONTENT_PROVIDER_TEST_EP, contentProvider.getName());
        Assert.assertEquals(1, contentProvider.getProperties().size());
    }
}
