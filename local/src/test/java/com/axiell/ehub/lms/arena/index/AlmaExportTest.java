package com.axiell.ehub.lms.arena.index;

import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AlmaExportTest {
    private static final String ALMA_EXPORT_PATH = "src/main/webapp/xml/arena/index/alma-export.xml";

    private String contentProviderNameXQuery;
    private String contentProviderRecordId;

    @Before
    public void setUp() throws IOException, SAXException, XpathException {
        FileSystemResource fileSystemResource = new FileSystemResource(ALMA_EXPORT_PATH);
        XMLUnit.setIgnoreWhitespace(true);
        Document doc = XMLUnit.buildControlDocument(new InputSource(fileSystemResource.getInputStream()));
        XpathEngine xpathEngine = XMLUnit.newXpathEngine();
        String format = xpathEngine.evaluate("/config/@format", doc);
        assertNotNull(format);
        assertEquals(format, "alma-export");
        contentProviderNameXQuery = xpathEngine.evaluate("/config/index/field[@name=\"contentProviderName\"]/query[@type=\"xquery\"]/text()", doc);
        assertNotNull(contentProviderNameXQuery);
        contentProviderRecordId = xpathEngine.evaluate("/config/index/field[@name=\"contentProviderRecordId\"]/query[@type=\"xquery\"]/text()", doc);
        assertNotNull(contentProviderRecordId);
    }

    @Test
    public void parseElib() {

    }
}
