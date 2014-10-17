package com.axiell.ehub.provider;

import com.axiell.ehub.patron.Patron;
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
    private static final ContentProviderName CONTENT_PROVIDER_NAME = ContentProviderName.PUBLIT;

    @Autowired
    private ExceptionContentProviderDataAccessor underTest;
    private CommandData commandData;

    @Before
    public void setUpCommandData() {
        final ContentProviderConsumer contentProviderConsumer = makeContentProviderConsumer();
        commandData = CommandData.newInstance(contentProviderConsumer, new Patron.Builder("card", "pin").build()).setContentProviderRecordId("contentProviderRecordId").setLanguage("language");
    }

    @Test
    public void clientResponseFailureToInternalServerErrorException() {
        try {
            underTest.getFormats(commandData);
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
        Assert.assertTrue(actualMessage.contains(CONTENT_PROVIDER_NAME.toString()));
    }

    private ContentProviderConsumer makeContentProviderConsumer() {
        final ContentProviderName name = CONTENT_PROVIDER_NAME;
        ContentProvider contentProvider = new ContentProvider();
        contentProvider.setName(name);
        final ContentProviderConsumer contentProviderConsumer = new ContentProviderConsumer();
        contentProviderConsumer.setContentProvider(contentProvider);
        return contentProviderConsumer;
    }
}