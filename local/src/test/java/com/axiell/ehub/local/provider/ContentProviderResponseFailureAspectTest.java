package com.axiell.ehub.local.provider;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.EhubError;
import com.axiell.ehub.common.InternalServerErrorException;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"aspects-test.xml"})
public class ContentProviderResponseFailureAspectTest {
    private static final String CONTENT_PROVIDER_NAME = ContentProvider.CONTENT_PROVIDER_ELIB3;

    @Autowired
    private ExceptionContentProviderDataAccessorStub underTest;

    private CommandData commandData;

    @BeforeEach
    public void setUpCommandData() {
        final ContentProviderConsumer contentProviderConsumer = makeContentProviderConsumer();
        commandData = CommandData.newInstance(contentProviderConsumer, new Patron.Builder().libraryCard("card").pin("pin").build(), "language").setContentProviderRecordId("contentProviderRecordId");
    }

    @Test
    public void clientResponseFailureToInternalServerErrorException() {
        try {
            underTest.getIssues(commandData);
            Assertions.fail("An InternalServerErrorException should have been thrown");
        } catch (InternalServerErrorException e) {
            thenInternalServerErrorExceptionMessageContainsExpectedContentProviderName(e);
        }
    }

    private void thenInternalServerErrorExceptionMessageContainsExpectedContentProviderName(InternalServerErrorException e) {
        Assertions.assertNotNull(e);
        EhubError ehubError = e.getEhubError();
        Assertions.assertNotNull(ehubError);
        String actualMessage = ehubError.getMessage();
        Assertions.assertTrue(actualMessage.contains(CONTENT_PROVIDER_NAME));
    }

    private ContentProviderConsumer makeContentProviderConsumer() {
        final String name = CONTENT_PROVIDER_NAME;
        ContentProvider contentProvider = new ContentProvider();
        contentProvider.setName(name);
        final ContentProviderConsumer contentProviderConsumer = new ContentProviderConsumer();
        contentProviderConsumer.setContentProvider(contentProvider);
        return contentProviderConsumer;
    }
}
