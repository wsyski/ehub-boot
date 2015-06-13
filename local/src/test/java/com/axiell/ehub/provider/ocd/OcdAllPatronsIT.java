package com.axiell.ehub.provider.ocd;

import junit.framework.Assert;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OcdAllPatronsIT extends AbstractOcdIT {
    protected static final String CARD = "4100000009";
    private List<PatronDTO> allPatrons;

    @Test
    public void allPatrons() {
        givenApiBaseUrl();
        givenBasicToken();
        givenLibraryId();
        givenContentProvider();
        whenGetAllPatrons();
        thenPatronIsFound();
    }

    private void whenGetAllPatrons() {
        allPatrons = underTest.getAllPatrons(contentProviderConsumer);
        //for(PatronDTO patronDTO : allPatrons) {
        //    System.out.println(patronDTO.getUserName());
        //}
    }

    private void thenPatronIsFound() {
       boolean isFound=false;
       for (PatronDTO patron: allPatrons) {
           if (CARD.equals(patron.getLibraryCardNumber())) {
               isFound=true;
               System.out.println("patron: "+ ToStringBuilder.reflectionToString(patron, ToStringStyle.MULTI_LINE_STYLE));
               break;
           }
       }
        Assert.assertTrue("Expected card: "+CARD,isFound);
    }

}