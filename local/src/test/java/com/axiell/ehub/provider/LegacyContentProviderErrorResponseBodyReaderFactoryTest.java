package com.axiell.ehub.provider;

import com.axiell.ehub.provider.elib.library3.LegacyElib3ErrorResponseBodyReader;
import com.axiell.ehub.provider.overdrive.LegacyOverdriveErrorResponseBodyReaderLegacy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.axiell.ehub.provider.ContentProviderName.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class LegacyContentProviderErrorResponseBodyReaderFactoryTest {
    private ILegacyContentProviderErrorResponseBodyReader actualReader;
    @Mock
    private ContentProvider contentProvider;

    @Test
    public void noContentProvider() {
        givenNoContentProvider();
        whenCreate();
        thenActualReaderIsDefaultReader();
    }

    @Test
    public void elib() {
        givenElibContentProvider();
        whenCreate();
        thenActualReaderIsDefaultReader();
    }

    @Test
    public void elibu() {
        givenElibUContentProvider();
        whenCreate();
        thenActualReaderIsDefaultReader();
    }

    @Test
    public void publit() {
        givenPublitContentProvider();
        whenCreate();
        thenActualReaderIsDefaultReader();
    }

    @Test
    public void askews() {
        givenAskewsContentProvider();
        whenCreate();
        thenActualReaderIsDefaultReader();
    }

    @Test
    public void overdrive() {
        givenOverdriveContentProvider();
        whenCreate();
        thenActualReaderIsOverdriveReader();
    }

    @Test
    public void elib3() {
        givenElib3ContentProvider();
        whenCreate();
        thenActualReaderIsElib3Reader();
    }

    private void givenAskewsContentProvider() {
        given(contentProvider.getName()).willReturn(ASKEWS);
    }

    private void givenElibUContentProvider() {
        given(contentProvider.getName()).willReturn(ELIBU);
    }

    private void givenPublitContentProvider() {
        given(contentProvider.getName()).willReturn(PUBLIT);
    }

    private void givenElibContentProvider() {
        given(contentProvider.getName()).willReturn(ELIB);
    }

    private void givenNoContentProvider() {
        contentProvider = null;
    }

    private void givenOverdriveContentProvider() {
        given(contentProvider.getName()).willReturn(OVERDRIVE);
    }

    private void thenActualReaderIsElib3Reader() {
        assertTrue(actualReader instanceof LegacyElib3ErrorResponseBodyReader);
    }

    private void givenElib3ContentProvider() {
        given(contentProvider.getName()).willReturn(ELIB3);
    }

    private void thenActualReaderIsDefaultReader() {
        assertTrue(actualReader instanceof LegacyDefaultContentProviderErrorResponseBodyReader);
    }

    private void whenCreate() {
        actualReader = LegacyContentProviderErrorResponseBodyReaderFactory.create(contentProvider);
    }

    private void thenActualReaderIsOverdriveReader() {
        assertTrue(actualReader instanceof LegacyOverdriveErrorResponseBodyReaderLegacy);
    }
}
