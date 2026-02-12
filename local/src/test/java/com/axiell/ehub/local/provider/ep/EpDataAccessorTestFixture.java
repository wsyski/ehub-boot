package com.axiell.ehub.local.provider.ep;

import com.axiell.ehub.common.InternalServerErrorException;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
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

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class EpDataAccessorTestFixture<C extends ICheckoutDTO, A extends AbstractEpDataAccessor> extends ContentProviderDataAccessorTestFixture<A> {

    @Mock
    private InternalServerErrorException internalServerErrorException;
    @Mock
    private IEhubExceptionFactory ehubExceptionFactory;
    @Mock
    private RecordDTO record;
    @Mock
    private FormatDTO format;

    @BeforeEach
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
        givenFormatDecorationInContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormatInFormatFactory();
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
