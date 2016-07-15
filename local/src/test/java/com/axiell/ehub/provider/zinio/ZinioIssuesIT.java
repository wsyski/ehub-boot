package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.EhubRuntimeException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgumentType;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS;
import static com.axiell.ehub.provider.ContentProvider.CONTENT_PROVIDER_ZINIO;

public class ZinioIssuesIT extends AbstractZinioIT {
    private List<IssueDTO> issues;

    @Test
    public void getIssues() {
        givenConfigurationProperties();
        givenContentProvider();
        whenGetIssues(RECORD_ID);
        thenExpectedIssues();
    }

    @Test
    public void getIssuesWithInvalidRecordId() {
        givenConfigurationProperties();
        givenContentProvider();
        whenGetIssues(INVALID_RECORD_ID);
        thenExpectedInvalidRecordIdException();
    }


    private void whenGetIssues(final String contentProviderRecordId) {
        try {
            issues = underTest.getIssues(contentProviderConsumer, contentProviderRecordId, Locale.ENGLISH.getLanguage());
        }
        catch(EhubRuntimeException ex) {
            exception = ex;
        }
    }


    private void thenExpectedInvalidRecordIdException() {
        thenExpectedEhubRuntimeException(ErrorCause.CONTENT_PROVIDER_ERROR.toEhubError(new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, CONTENT_PROVIDER_ZINIO),
                new ErrorCauseArgument(CONTENT_PROVIDER_STATUS, ErrorCauseArgumentType.PRODUCT_UNAVAILABLE.name())));
    }

    private void thenExpectedIssues() {
        Assert.assertThat(issues, Matchers.notNullValue());
        Assert.assertThat(issues, Matchers.not(Matchers.empty()));
        Assert.assertThat(issues.get(0), Matchers.isA(IssueDTO.class));
    }
}