package com.axiell.ehub.provider.ep;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public abstract class EpDataAccessorTestFixture<C extends ICheckoutDTO, A extends AbstractEpDataAccessor> extends ContentProviderDataAccessorTestFixture<A> {

    @Mock
    private InternalServerErrorException internalServerErrorException;
    @Mock
    private IEhubExceptionFactory ehubExceptionFactory;
    @Mock
    private RecordDTO record;
    @Mock
    private FormatDTO format;

    @Before
    public void setUp() {
        underTest = createEpDataAccessor();
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
        ReflectionTestUtils.setField(underTest, "ehubExceptionFactory", ehubExceptionFactory);
    }

    @Test
    public void getFormats() {
        givenLanguageInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderAliasInCommandData();
        givenPatronInCommandData();
        givenEpFacadeReturnsFormats();
        givenFormatDecorationFromContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormatFromFormatFactory();
        whenGetIssues();
        thenActualFormatsContainsOneFormat();
        thenActualFormatEqualsExpected();
    }

    protected abstract A createEpDataAccessor();

    protected abstract IEpFacade getEpFacade();

    private void givenEpFacadeReturnsFormats() {
        given(getEpFacade().getRecord(contentProviderConsumer, patron, RECORD_ID)).willReturn(record);
        given(record.getFormats()).willReturn(Collections.singletonList(format));
        given(format.getId()).willReturn(FORMAT_ID);
    }

    @Override
    protected String getContentProviderName() {
        return CONTENT_PROVIDER_TEST_EP;
    }
}
