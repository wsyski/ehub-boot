package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OcdAllMediaIT extends AbstractOcdIT {
    private String contentProviderRecordId;
    private List<MediaDTO> allMedia;

    @Test
    public void eAudio() throws IFinder.NotFoundException {
        givenApiBaseUrl();
        givenBasicToken();
        givenLibraryId();
        givenContentProvider();
        givenContentProviderRecordId(FORMAT_ID_EAUDIO);
        whenGetAllMedia();
        thenExpectedMediaType(FORMAT_ID_EAUDIO);
    }

    @Test
    public void eEbook() throws IFinder.NotFoundException {
        givenApiBaseUrl();
        givenBasicToken();
        givenLibraryId();
        givenContentProvider();
        givenContentProviderRecordId(FORMAT_ID_EBOOK);
        whenGetAllMedia();
        thenExpectedMediaType(FORMAT_ID_EBOOK);
    }

    private void whenGetAllMedia() {
        allMedia = underTest.getAllMedia(contentProviderConsumer);
        //for(MediaDTO mediaDTO: allMedia) {
        //    System.out.println(mediaDTO.getMediaType()+" "+mediaDTO.getIsbn());
        //}
    }

    private void thenExpectedMediaType(final String mediaType) throws IFinder.NotFoundException {
        IMatcher<MediaDTO> matcher = new ContentProviderRecordIdMediaMatcher(contentProviderRecordId);
        MediaDTO media = new CollectionFinder<MediaDTO>().find(matcher, allMedia);
        assertEquals(mediaType, media.getMediaType());
    }

    private void givenContentProviderRecordId(final String contentProviderFormatId) {
        if (FORMAT_ID_EAUDIO.equals(contentProviderFormatId)) {
            contentProviderRecordId = RECORD_ID_EAUDIO;
        } else {
            contentProviderRecordId = RECORD_ID_EBOOK;
        }
    }
}
