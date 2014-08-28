package com.axiell.ehub.lms.arena.index;

import net.sf.saxon.trans.XPathException;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AlmaExportParseExternalUrlsTest {
    private static final String ALMA_EXPORT_PATH = "src/main/webapp/xml/arena/index/alma-export.xml";
    private static final String ALMA_INDEX_CLASSPATH = "com/axiell/ehub/lms/arena/index";

    private String xQuery;
    private Document almaExportDocument;
    private Document almaExportCatalogueDocument;
    private XpathEngine xpathEngine;
    private XQueryFieldEvaluator xQueryFieldEvaluator;
    private Object[] xQueryResults;

    @Before
    public void setUp() throws IOException, SAXException, XpathException {
        XMLUnit.setIgnoreWhitespace(true);
        almaExportDocument = parseDocument(new FileSystemResource(ALMA_EXPORT_PATH));
        xpathEngine = XMLUnit.newXpathEngine();
        xQueryFieldEvaluator = new XQueryFieldEvaluator();
    }

    @Test
    public void validateSyntaxForContentProviderName() throws XpathException {
        givenAlmaExportIsParsedForFieldName("contentProviderName");
        thenDocumentHasValidSyntax();
    }

    @Test
    public void validateSyntaxForContentProviderRecordId() throws XpathException {
        givenAlmaExportIsParsedForFieldName("contentProviderRecordId");
        thenDocumentHasValidSyntax();
    }

    @Test
    public void evaluateContentProviderName() throws XpathException, IOException, SAXException, XPathException {
        givenAlmaExportIsParsedForFieldName("contentProviderName");
        givenAlmaExportCatalogueIsParsed(ALMA_INDEX_CLASSPATH + "/alma-export-catalogue.xml");
        whenXQueryIsEvaluated();
        thenXQueryReturnValueIsExtracted("contentProviderName");
    }

    @Test
    public void evaluateElibContentProviderName() throws XpathException, IOException, SAXException, XPathException {
        givenAlmaExportIsParsedForFieldName("contentProviderName");
        givenAlmaExportCatalogueIsParsed(ALMA_INDEX_CLASSPATH + "/alma-export-catalogue-elib.xml");
        whenXQueryIsEvaluated();
        thenXQueryReturnValueIsExtracted("elib");
    }

    @Test
    public void evaluateElibUppercaseContentProviderName() throws XpathException, IOException, SAXException, XPathException {
        givenAlmaExportIsParsedForFieldName("contentProviderName");
        givenAlmaExportCatalogueIsParsed(ALMA_INDEX_CLASSPATH + "/alma-export-catalogue-elib-uppercase.xml");
        whenXQueryIsEvaluated();
        thenXQueryReturnValueIsExtracted("elib");
    }

    @Test
    public void evaluateElibShortContentProviderName() throws XpathException, IOException, SAXException, XPathException {
        givenAlmaExportIsParsedForFieldName("contentProviderName");
        givenAlmaExportCatalogueIsParsed(ALMA_INDEX_CLASSPATH + "/alma-export-catalogue-elib-short.xml");
        whenXQueryIsEvaluated();
        thenXQueryReturnValueIsExtracted("elib");
    }

    @Test
    public void evaluatePublitContentProviderName() throws XpathException, IOException, SAXException, XPathException {
        givenAlmaExportIsParsedForFieldName("contentProviderName");
        givenAlmaExportCatalogueIsParsed(ALMA_INDEX_CLASSPATH + "/alma-export-catalogue-publit.xml");
        whenXQueryIsEvaluated();
        thenXQueryReturnValueIsExtracted("publit");
    }

    @Test
    public void evaluateContentProviderRecordId() throws XpathException, IOException, SAXException, XPathException {
        givenAlmaExportIsParsedForFieldName("contentProviderRecordId");
        givenAlmaExportCatalogueIsParsed(ALMA_INDEX_CLASSPATH + "/alma-export-catalogue.xml");
        whenXQueryIsEvaluated();
        thenXQueryReturnValueIsExtracted("contentProviderRecordId");
    }

    @Test
    public void evaluateElibContentProviderRecordId() throws XpathException, IOException, SAXException, XPathException {
        givenAlmaExportIsParsedForFieldName("contentProviderRecordId");
        givenAlmaExportCatalogueIsParsed(ALMA_INDEX_CLASSPATH + "/alma-export-catalogue-elib.xml");
        whenXQueryIsEvaluated();
        thenXQueryReturnValueIsExtracted("918501120X");
    }

    @Test
    public void evaluateElibUppercaseContentProviderRecordId() throws XpathException, IOException, SAXException, XPathException {
        givenAlmaExportIsParsedForFieldName("contentProviderRecordId");
        givenAlmaExportCatalogueIsParsed(ALMA_INDEX_CLASSPATH + "/alma-export-catalogue-elib-uppercase.xml");
        whenXQueryIsEvaluated();
        thenXQueryReturnValueIsExtracted("918501120X");
    }

    @Test
    public void evaluateElibShortContentProviderRecordId() throws XpathException, IOException, SAXException, XPathException {
        givenAlmaExportIsParsedForFieldName("contentProviderRecordId");
        givenAlmaExportCatalogueIsParsed(ALMA_INDEX_CLASSPATH + "/alma-export-catalogue-elib-short.xml");
        whenXQueryIsEvaluated();
        thenXQueryReturnValueIsExtracted("918501120X");
    }

    @Test
    public void evaluatePublitContentProviderRecordId() throws XpathException, IOException, SAXException, XPathException {
        givenAlmaExportIsParsedForFieldName("contentProviderRecordId");
        givenAlmaExportCatalogueIsParsed(ALMA_INDEX_CLASSPATH + "/alma-export-catalogue-publit.xml");
        whenXQueryIsEvaluated();
        thenXQueryReturnValueIsExtracted("9789174376838");
    }

    private void thenXQueryReturnValueIsExtracted(final String value) {
        assertNotNull(xQueryResults);
        assertEquals(1, xQueryResults.length);
        assertEquals(value, xQueryResults[0]);
    }

    private void whenXQueryIsEvaluated() throws XPathException {
        xQueryResults = xQueryFieldEvaluator.evaluateExpression(xQuery, almaExportCatalogueDocument, null, null, null);
    }

    private void givenAlmaExportCatalogueIsParsed(final String path) throws IOException, SAXException, XpathException {
        almaExportCatalogueDocument = parseDocument(new ClassPathResource(path));
    }


    private void givenAlmaExportIsParsedForFieldName(final String fieldName) throws XpathException {
        xQuery = evaluateAlmaExportXPath("/config/index/field[@name=\"" + fieldName + "\"]/query[@type=\"xquery\"]/text()");
    }

    private String evaluateAlmaExportXPath(final String xPath) throws XpathException {
        return xpathEngine.evaluate(xPath, almaExportDocument);
    }

    private Document parseDocument(final Resource resource) throws IOException, SAXException {
        return XMLUnit.buildControlDocument(new InputSource(resource.getInputStream()));
    }

    private void thenDocumentHasValidSyntax() throws XpathException {
        String format = evaluateAlmaExportXPath("/config/@format");
        assertNotNull(format);
        assertEquals(format, "alma-export");
        assertNotNull(xQuery);
    }
}
