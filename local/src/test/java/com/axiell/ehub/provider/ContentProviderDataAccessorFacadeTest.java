package com.axiell.ehub.provider;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.checkout.ContentBuilder;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.alias.IAliasBusinessController;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ContentProviderDataAccessorFacadeTest {
    public static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    public static final String LANGUAGE = "language";
    public static final String LMS_RECORD_ID = "lmsRecordId";
    public static final String CONTENT_PROVIDER_RECORD_ID = "contentProviderRecordId";
    public static final String CONTENT_PROVIDER_FORMAT_ID = "contentProviderFormatdId";
    private static final String CONTENT_PROVIDER_ALIAS = CONTENT_PROVIDER_TEST_EP;
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
    private Patron patron;
    @Mock
    private FormatDecoration formatDecoration;

    @BeforeEach
    public void setUpEhubConsumer() {
        given(ehubConsumer.getContentProviderConsumer(any(String.class))).willReturn(contentProviderConsumer);
    }

    @BeforeEach
    public void setUpContentProviderDataAccessorFacade() {
        underTest = new ContentProviderDataAccessorFacade();
        ReflectionTestUtils.setField(underTest, "aliasBusinessController", aliasBusinessController);
        ReflectionTestUtils.setField(underTest, "contentProviderDataAccessorFactory", contentProviderDataAccessorFactory);
    }

    @BeforeEach
    public void setUpContentProviderBusinessController() {
        given(aliasBusinessController.getName(anyString())).willReturn(CONTENT_PROVIDER_TEST_EP);
    }

    @BeforeEach
    public void setUpContentProviderDataAccessor() {
        given(contentProviderDataAccessorFactory.getInstance(any(ContentProvider.class))).willReturn(contentProviderDataAccessor);
    }

    @BeforeEach
    public void setUpContentProviderConsumer() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }

    @BeforeEach
    public void setUpPendingLoan() {
        given(pendingLoan.contentProviderRecordId()).willReturn(CONTENT_PROVIDER_RECORD_ID);
        given(pendingLoan.contentProviderFormatId()).willReturn(CONTENT_PROVIDER_FORMAT_ID);
        given(pendingLoan.contentProviderAlias()).willReturn(CONTENT_PROVIDER_ALIAS);
        given(pendingLoan.lmsRecordId()).willReturn(LMS_RECORD_ID);
    }

    @BeforeEach
    public void setUpEhubLoan() {
        given(ehubLoan.getContentProviderLoanMetadata()).willReturn(contentProviderLoanMetadata);
        given(contentProviderLoanMetadata.getContentProvider()).willReturn(contentProvider);
        given(contentProvider.getName()).willReturn(CONTENT_PROVIDER_TEST_EP);
    }

    @BeforeEach
    public void setUpContentProviderLoan() {
        given(contentProviderDataAccessor.createLoan(any(CommandData.class))).willReturn(contentProviderLoan);
    }

    @BeforeEach
    public void setUpContent() {
        given(contentProviderDataAccessor.getContent(any(CommandData.class))).willReturn(ContentBuilder.contentWithSupplementLinks());
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
        inOrder.verify(contentProviderDataAccessorFactory).getInstance(any(ContentProvider.class));
        inOrder.verify(contentProviderDataAccessor).createLoan(any(CommandData.class));
    }

    private void whenGetContent() {
        underTest.getContent(ehubConsumer, ehubLoan, formatDecoration, patron, LANGUAGE);
    }

    private void thenContentIsRetrievedFromContentProvider() {
        InOrder inOrder = inOrder(contentProviderDataAccessorFactory, contentProviderDataAccessor);
        inOrder.verify(contentProviderDataAccessorFactory).getInstance(any(ContentProvider.class));
        inOrder.verify(contentProviderDataAccessor).getContent(argThat(new GetContentCommandData()));
    }

    private void whenGetFormats() {
        underTest.getIssues(ehubConsumer, CONTENT_PROVIDER_ALIAS, patron, CONTENT_PROVIDER_RECORD_ID, LANGUAGE);
    }

    private void thenFormatsAreRetrievedFromContentProvider() {
        InOrder inOrder = inOrder(contentProviderDataAccessorFactory, contentProviderDataAccessor);
        inOrder.verify(contentProviderDataAccessorFactory).getInstance(any(ContentProvider.class));
        inOrder.verify(contentProviderDataAccessor).getIssues(argThat(new GetFormatsCommandData()));
    }

    private class GetFormatsCommandData implements ArgumentMatcher<CommandData> {


        @Override
        public boolean matches(final CommandData data) {
            final CommandDataMatcherHelper helper = new CommandDataMatcherHelper(data);
            return helper.isExpectedContentProviderConsumer(contentProviderConsumer) && helper.isExpectedPatron(patron)
                    && helper.isExpectedContentProviderRecordId(CONTENT_PROVIDER_RECORD_ID) && helper.isExpectedLanguage(LANGUAGE);
        }
    }

    private class CreateLoanCommandData implements ArgumentMatcher<CommandData> {

        @Override
        public boolean matches(final CommandData data) {
            final CommandDataMatcherHelper helper = new CommandDataMatcherHelper(data);
            return helper.isExpectedContentProviderConsumer(contentProviderConsumer) && helper.isExpectedPatron(patron) &&
                    helper.isExpectedPendingLoan(pendingLoan) && helper.isExpectedLanguage(LANGUAGE);
        }
    }

    private class GetContentCommandData implements ArgumentMatcher<CommandData> {

        @Override
        public boolean matches(final CommandData data) {
            final CommandDataMatcherHelper helper = new CommandDataMatcherHelper(data);
            return helper.isExpectedContentProviderConsumer(contentProviderConsumer) && helper.isExpectedPatron(patron)
                    && helper.isExpectedContentProviderLoanMetadata(contentProviderLoanMetadata) && helper.isExpectedLanguage(LANGUAGE);
        }
    }
}
