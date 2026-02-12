package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.ehub.common.checkout.ContentBuilder;
import com.axiell.ehub.common.checkout.ContentLinkBuilder;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.Format;
import com.axiell.ehub.common.provider.record.format.FormatBuilder;
import com.axiell.ehub.local.loan.ContentProviderLoan;
import com.axiell.ehub.local.provider.CommandData;
import com.axiell.ehub.local.provider.ContentProviderDataAccessorTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class Elib3DataAccessorTest extends ContentProviderDataAccessorTestFixture<Elib3DataAccessor> {

    @Mock
    private IElib3CommandChainFactory commandChainFactory;
    @Mock
    private GetFormatsCommandChain getFormatsCommandChain;
    @Mock
    private CreateLoanCommandChain createLoanCommandChain;
    @Mock
    private ContentProviderLoan expectedLoan;
    @Mock
    private GetContentCommandChain getContentCommandChain;

    private List<Format> expectedFormats = Collections.singletonList(FormatBuilder.streamingFormat());

    @BeforeEach
    public void setUpUnderTest() {
        underTest = new Elib3DataAccessor();
        ReflectionTestUtils.setField(underTest, "commandChainFactory", commandChainFactory);
    }

    @Test
    public void getFormats() {
        givenExpectedFormats();
        givenGetFormatsCommandChain();
        whenGetIssues();
        thenActualFormatsEqualsExpectedFormats();
    }

    @Test
    public void createLoan() {
        givenExpectedLoan();
        givenCreateLoanCommandChain();
        whenCreateLoan();
        thenActualLoanEqualsExpectedLoan();
    }

    @Test
    public void getContent() {
        givenExpectedContent();
        givenGetContentCommandChain();
        whenGetContent();
        thenActualContentEqualsExpectedContent();
    }

    private void givenExpectedContent() {
        given(getContentCommandChain.execute(any(CommandData.class))).willReturn(ContentBuilder.contentWithSupplementLinks());
    }

    private void givenGetContentCommandChain() {
        given(commandChainFactory.createGetContentCommandChain()).willReturn(getContentCommandChain);
    }

    private void thenActualContentEqualsExpectedContent() {
        assertThat(getActualContentLink().href(), is(ContentLinkBuilder.HREF));
    }

    private void givenExpectedLoan() {
        given(createLoanCommandChain.execute(any(CommandData.class))).willReturn(expectedLoan);
    }

    private void givenCreateLoanCommandChain() {
        given(commandChainFactory.createCreateLoanCommandChain()).willReturn(createLoanCommandChain);
    }

    private void thenActualLoanEqualsExpectedLoan() {
        assertThat(actualLoan, is(expectedLoan));
    }

    private void givenExpectedFormats() {
        given(getFormatsCommandChain.execute(any(Elib3CommandData.class))).willReturn(expectedFormats);
    }

    private void givenGetFormatsCommandChain() {
        given(commandChainFactory.createGetFormatsCommandChain()).willReturn(getFormatsCommandChain);
    }

    private void thenActualFormatsEqualsExpectedFormats() {
        assertThat(getActualFormats(), is(expectedFormats));
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_ELIB3;
    }
}
