package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.*;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.alias.IAliasBusinessController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ContentProviderDataAccessorFacadeTest {
    public static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    private static final String CONTENT_PROVIDER_ALIAS = CONTENT_PROVIDER_TEST_EP;
    //    public static final String LIBRARY_CARD = "libraryCard";
//    public static final String PIN = "pin";
    public static final String LANGUAGE = "language";
    public static final String CONTENT_PROVIDER_RECORD_ID = "contentProviderRecordId";
    public static final String CONTENT_PROVIDER_FORMAT_ID = "contentProviderFormatdId";
    private IContentProviderDataAccessorFacade underTest;
    @Mock
    private IAliasBusinessController aliasBusinessController;
    @Mock
    private IContentProviderDataAccessorFactory contentProviderDataAccessorFactory;
    @Mock
    private IContentProviderDataAccessor contentProviderDataAccessor;
    @Mock
    private EhubConsumer ehubConsumer;
    @Mock
    private PendingLoan pendingLoan;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private EhubLoan ehubLoan;
    @Mock
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private ContentProviderLoan contentProviderLoan;
    @Mock
    private ContentLink contentLink;
    @Mock
    private Patron patron;

    @Before
    public void setUpEhubConsumer() {
        given(ehubConsumer.getContentProviderConsumer(any(String.class))).willReturn(contentProviderConsumer);
    }

    @Before
    public void setUpContentProviderDataAccessorFacade() {
        underTest = new ContentProviderDataAccessorFacade();
        ReflectionTestUtils.setField(underTest, "aliasBusinessController", aliasBusinessController);
        ReflectionTestUtils.setField(underTest, "contentProviderDataAccessorFactory", contentProviderDataAccessorFactory);
    }

    @Before
    public void setUpContentProviderBusinessController() {
        given(aliasBusinessController.getName(anyString())).willReturn(CONTENT_PROVIDER_TEST_EP);
    }

    @Before
    public void setUpContentProviderDataAccessor() {
        given(contentProviderDataAccessorFactory.getInstance(any(String.class))).willReturn(contentProviderDataAccessor);
    }

    @Before
    public void setUpPendingLoan() {
        given(pendingLoan.contentProviderRecordId()).willReturn(CONTENT_PROVIDER_RECORD_ID);
        given(pendingLoan.contentProviderFormatId()).willReturn(CONTENT_PROVIDER_FORMAT_ID);
        given(pendingLoan.contentProviderAlias()).willReturn(CONTENT_PROVIDER_ALIAS);
    }

    @Before
    public void setUpEhubLoan() {
        given(ehubLoan.getContentProviderLoanMetadata()).willReturn(contentProviderLoanMetadata);
        given(contentProviderLoanMetadata.getContentProvider()).willReturn(contentProvider);
        given(contentProvider.getName()).willReturn(CONTENT_PROVIDER_TEST_EP);
    }

    @Before
    public void setUpContentProviderLoan() {
        given(contentProviderDataAccessor.createLoan(any(CommandData.class))).willReturn(contentProviderLoan);
    }

    @Before
    public void setUpContent() {
        given(contentProviderDataAccessor.getContent(any(CommandData.class))).willReturn(contentLink);
    }

    @Test
    public void getFormats() {
        whenGetFormats();
        thenFormatsAreRetrievedFromContentProvider();
        thenContentProviderNameIsRetrievedFromAliasBusinessController();
    }

    @Test
    public void createLoan() {
        whenCreateLoan();
        thenLoanIsCreatedByContentProvider();
        thenContentProviderNameIsRetrievedFromAliasBusinessController();
    }

    @Test
    public void getContent() {
        whenGetContent();
        thenContentIsRetrievedFromContentProvider();
    }

    private void thenContentProviderNameIsRetrievedFromAliasBusinessController() {
        verify(aliasBusinessController).getName(CONTENT_PROVIDER_ALIAS);
    }

    private void whenCreateLoan() {
        underTest.createLoan(ehubConsumer, patron, pendingLoan, LANGUAGE);
    }

    private void thenLoanIsCreatedByContentProvider() {
        InOrder inOrder = inOrder(contentProviderDataAccessorFactory, contentProviderDataAccessor);
        inOrder.verify(contentProviderDataAccessorFactory).getInstance(any(String.class));
        inOrder.verify(contentProviderDataAccessor).createLoan(argThat(new CreateLoanCommandData()));
    }

    private void whenGetContent() {
        underTest.getContent(ehubConsumer, ehubLoan, patron, LANGUAGE);
    }

    private void thenContentIsRetrievedFromContentProvider() {
        InOrder inOrder = inOrder(contentProviderDataAccessorFactory, contentProviderDataAccessor);
        inOrder.verify(contentProviderDataAccessorFactory).getInstance(any(String.class));
        inOrder.verify(contentProviderDataAccessor).getContent(argThat(new GetContentCommandData()));
    }

    private void whenGetFormats() {
        underTest.getFormats(ehubConsumer, CONTENT_PROVIDER_ALIAS, patron, CONTENT_PROVIDER_RECORD_ID, LANGUAGE);
    }

    private void thenFormatsAreRetrievedFromContentProvider() {
        InOrder inOrder = inOrder(contentProviderDataAccessorFactory, contentProviderDataAccessor);
        inOrder.verify(contentProviderDataAccessorFactory).getInstance(any(String.class));
        inOrder.verify(contentProviderDataAccessor).getFormats(argThat(new GetFormatsCommandData()));
    }

    private class GetFormatsCommandData extends ArgumentMatcher<CommandData> {

        @Override
        public boolean matches(Object argument) {
            if (argument instanceof CommandData) {
                final CommandData data = (CommandData) argument;
                final CommandDataMatcherHelper helper = new CommandDataMatcherHelper(data);
                return helper.isExpectedContentProviderConsumer(contentProviderConsumer) && helper.isExpectedPatron(patron)
                        && helper.isExpectedContentProviderRecordId(CONTENT_PROVIDER_RECORD_ID) && helper.isExpectedLanguage(LANGUAGE);
            }
            return false;
        }
    }

    private class CreateLoanCommandData extends ArgumentMatcher<CommandData> {

        @Override
        public boolean matches(Object argument) {
            if (argument instanceof CommandData) {
                final CommandData data = (CommandData) argument;
                final CommandDataMatcherHelper helper = new CommandDataMatcherHelper(data);
                return helper.isExpectedContentProviderConsumer(contentProviderConsumer) && helper.isExpectedPatron(patron) && helper.isExpectedPendingLoan(pendingLoan) && helper.isExpectedLanguage(LANGUAGE);
            }
            return false;
        }
    }

    private class GetContentCommandData extends ArgumentMatcher<CommandData> {

        @Override
        public boolean matches(Object argument) {
            if (argument instanceof CommandData) {
                final CommandData data = (CommandData) argument;
                final CommandDataMatcherHelper helper = new CommandDataMatcherHelper(data);
                return helper.isExpectedContentProviderConsumer(contentProviderConsumer) && helper.isExpectedPatron(patron)
                        && helper.isExpectedContentProviderLoanMetadata(contentProviderLoanMetadata) && helper.isExpectedLanguage(LANGUAGE);
            }
            return false;
        }
    }
}
