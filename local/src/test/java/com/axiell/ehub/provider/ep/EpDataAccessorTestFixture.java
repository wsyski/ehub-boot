package com.axiell.ehub.provider.ep;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import com.axiell.ehub.provider.ep.lpf.ILpfEpFacade;
import com.axiell.ehub.provider.ep.lpf.LpfCheckoutDTO;
import com.axiell.ehub.provider.ep.lpf.LpfEpDataAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public abstract class EpDataAccessorTestFixture<F extends IEpFacade, C extends ICheckoutDTO, A extends AbstractEpDataAccessor> extends ContentProviderDataAccessorTestFixture {

    private A underTest;
    @Mock
    private F epFacade;
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
        ReflectionTestUtils.setField(underTest, "contentFactory", contentFactory);
        ReflectionTestUtils.setField(underTest, "ehubExceptionFactory", ehubExceptionFactory);
    }

    protected abstract A createEpDataAccessor();

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
        whenGetFormats();
        thenFormatSetContainsOneFormat();
        thenActualFormatEqualsExpected();
    }

    private void givenEpFacadeReturnsFormats() {
        given(epFacade.getRecord(contentProviderConsumer, patron, RECORD_ID)).willReturn(record);
        given(record.getFormats()).willReturn(Collections.singletonList(format));
        given(format.getId()).willReturn(FORMAT_ID);
    }

    private void whenGetFormats() {
        actualFormats = underTest.getFormats(commandData);
    }

    private void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
    }

    protected void whenGetContent() {
        actualContentLink = underTest.getContent(commandData).getContentLinks().getContentLinks().get(0);
    }

    @Override
    protected String getContentProviderName() {
        return CONTENT_PROVIDER_TEST_EP;
    }
}
