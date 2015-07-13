package com.axiell.ehub.provider.ep;

import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class EpGetFormatsIT extends AbstractEpIT {

    private RecordDTO record;

    @Ignore
    @Test
    public void getFormats() throws IFinder.NotFoundException {
        givenPatronIdInPatron();
        givenConfigurationProperties(EpUserIdValue.PATRON_ID);
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
}
