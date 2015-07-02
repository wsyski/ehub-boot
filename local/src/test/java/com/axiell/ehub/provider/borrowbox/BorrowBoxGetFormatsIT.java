package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

public class BorrowBoxGetFormatsIT extends AbstractBorrowBoxIT {
    protected static final String CARD = "D0200000000000";

    private String contentProviderRecordId;
    private FormatsDTO formats;

    @Test
    public void eAudio() throws IFinder.NotFoundException {
        givenPatron();
        givenConfigurationProperties();
        givenContentProvider();
        givenContentProviderRecordId(FORMAT_ID_EAUDIO);
        whenGetFormats();
        thenExpectedMediaType(FORMAT_ID_EAUDIO);
    }

    @Test
    public void eEbook() throws IFinder.NotFoundException {
        givenPatron();
        givenConfigurationProperties();
        givenContentProvider();
        givenContentProviderRecordId(FORMAT_ID_EBOOK);
        whenGetFormats();
        thenExpectedMediaType(FORMAT_ID_EBOOK);
    }

    private void whenGetFormats() {
        formats = underTest.getFormats(contentProviderConsumer, patron, LANGUAGE, contentProviderRecordId);
    }

    private void thenExpectedMediaType(final String contentProviderFormatId) throws IFinder.NotFoundException {
        IMatcher<FormatsDTO.FormatDTO> matcher = new FormatIdFormatMatcher(contentProviderFormatId);
        FormatsDTO.FormatDTO format = new CollectionFinder<FormatsDTO.FormatDTO>().find(matcher, formats.getFormats());
        assertEquals(contentProviderFormatId, format.getFormatId());
    }

    private void givenContentProviderRecordId(final String contentProviderFormatId) {
        if (FORMAT_ID_EAUDIO.equals(contentProviderFormatId)) {
            contentProviderRecordId = RECORD_ID_EAUDIO;
        } else {
            contentProviderRecordId = RECORD_ID_EBOOK;
        }
    }

    private void givenPatron() {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(CARD);
    }
}
