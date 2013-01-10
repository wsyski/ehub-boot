package com.axiell.ehub.util;

import com.axiell.ehub.loan.PendingLoan;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.axiell.ehub.util.XjcSupport.marshal;
import static com.axiell.ehub.util.XjcSupport.unmarshal;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLUnit.getControlDocumentBuilderFactory;

public class XjcSupportTest {
    @BeforeClass
    public static void setNameSpaceUnaware() throws Exception {
        getControlDocumentBuilderFactory().setNamespaceAware(false);
    }

    @Test
    public void testMarshalUnmarshal() throws Exception {
        PendingLoan pendingLoan = new PendingLoan("id", "contentProvider", "otherId", "format");
        final String xml = marshal(pendingLoan);
        System.out.println("xml = " + xml);
        assertXpathExists("/pendingLoan", xml);
        assertXpathEvaluatesTo("format", "/pendingLoan/@contentProviderFormatId", xml);
        assertXpathEvaluatesTo("contentProvider", "/pendingLoan/@contentProviderName", xml);
        assertXpathEvaluatesTo("otherId", "/pendingLoan/@contentProviderRecordId", xml);
        assertXpathEvaluatesTo("id", "/pendingLoan/@lmsRecordId", xml);

        final PendingLoan afterUnmarshal1 = unmarshal(xml, PendingLoan.class);
        final PendingLoan afterUnmarshal2 = (PendingLoan) unmarshal(xml);
        assertTrue(EqualsBuilder.reflectionEquals(afterUnmarshal1, afterUnmarshal2));
        assertEquals("id", afterUnmarshal1.getLmsRecordId());
        assertEquals("format", afterUnmarshal1.getContentProviderFormatId());
        assertEquals("contentProvider", afterUnmarshal1.getContentProviderName());
        assertEquals("otherId", afterUnmarshal1.getContentProviderRecordId());
    }
}
