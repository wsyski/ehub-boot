package com.axiell.ehub.provider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"aspects-test.xml"})
public class ContentProviderResponseFailureAspectTest {
    private static final String CONTENT_PROVIDER_NAME = ContentProviderName.PUBLIT.toString();

    @Autowired
    private ExceptionContentProviderDataAccessor underTest;
    private ContentProviderConsumer contentProviderConsumer;

    @Before
    public void setUpContentProviderConsumer() {
        ContentProviderName name = ContentProviderName.fromString(CONTENT_PROVIDER_NAME);
        ContentProvider contentProvider = new ContentProvider();
        contentProvider.setName(name);
        contentProviderConsumer = new ContentProviderConsumer();
        contentProviderConsumer.setContentProvider(contentProvider);
    }

    @Test
    public void clientResponseFailureToInternalServerErrorException() {
        try {
            underTest.getFormats(contentProviderConsumer, "libraryCard", "contentProviderRecordId", "language");
            Assert.fail("An InternalServerErrorException should have been thrown");
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionMessageContainsExpectedContentProviderName(e);
        }
    }

    private void thenInternalServerErrorExceptionMessageContainsExpectedContentProviderName(InternalServerErrorException e) {
        Assert.assertNotNull(e);
        EhubError ehubError = e.getEhubError();
        Assert.assertNotNull(ehubError);
        String actualMessage = ehubError.getMessage();
        Assert.assertTrue(actualMessage.contains(CONTENT_PROVIDER_NAME));
    }
}