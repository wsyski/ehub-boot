package com.axiell.ehub.provider.ep;

import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.BDDMockito.given;

public class EpGetFormatsIT extends AbstractEpIT {
    private static final String PATRON_ID = "patronId";

    private RecordDTO record;

    @Test
    public void getFormats() throws IFinder.NotFoundException {
        givenPatron();
        givenConfigurationProperties();
        givenContentProvider();
        givenEhubConsumer();
        whenGetFormats();
        thenExpectedMediaType(FORMAT_ID);
    }

    private void whenGetFormats() {
        record = underTest.getRecord(contentProviderConsumer, patron, RECORD_ID);
    }

    private void thenExpectedMediaType(final String contentProviderFormatId) throws IFinder.NotFoundException {
        IMatcher<FormatDTO> matcher = new FormatIdFormatMatcher(contentProviderFormatId);
        FormatDTO format = new CollectionFinder<FormatDTO>().find(matcher, record.getFormats());
        Assert.assertThat(contentProviderFormatId, Matchers.is(format.getId()));
    }

    private void givenPatron() {
        given(patron.getId()).willReturn(PATRON_ID);
    }
}
