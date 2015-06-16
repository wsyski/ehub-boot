package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.util.CollectionFinder;
import com.axiell.ehub.util.IFinder;
import com.axiell.ehub.util.IMatcher;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

public class OcdAllPatronsIT extends AbstractOcdIT {
    protected static final String CARD = "4100000009";
    private List<PatronDTO> allPatrons;

    @Test
    public void allPatrons() throws IFinder.NotFoundException {
        givenApiBaseUrl();
        givenBasicToken();
        givenLibraryId();
        givenContentProvider();
        whenGetAllPatrons();
        thenExpectedPatronIsFound();
    }

    private void whenGetAllPatrons() {
        allPatrons = underTest.getAllPatrons(contentProviderConsumer);
        for (PatronDTO patronDTO : allPatrons) {
            System.out.println(patronDTO.getUserName());
        }
    }

    private void thenExpectedPatronIsFound() throws IFinder.NotFoundException {
        IMatcher<PatronDTO> matcher = new LibraryCardPatronMatcher(CARD);
        PatronDTO patron = new CollectionFinder<PatronDTO>().find(matcher, allPatrons);
        Assert.assertEquals("Expected card: " + CARD, CARD, patron.getLibraryCardNumber());
    }
}