package com.axiell.ehub.provider.record.format;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axiell.ehub.util.XjcSupport;

public class FormatsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormatsTest.class);
    private String expXml;
    private Formats expFormats;
    private Format expFormat;
    private Formats actFormats;

    @Before
    public void setUp() {
	expFormat = new Format("id1", "name1", "description1", "iconUrl1");
	expFormats = new Formats();
	expFormats.addFormat(expFormat);
    }

    @Test
    public void unmarshalFormatsXml() throws JAXBException {
	givenFormatsAsXml();
	whenUnmarshalFormatsXml();
	thenActualFormatsEqualsExpectedFormats();
    }

    private void givenFormatsAsXml() {
	expXml = XjcSupport.marshal(expFormats);
	LOGGER.debug(expXml);
    }

    private void whenUnmarshalFormatsXml() throws JAXBException {
	actFormats = (Formats) XjcSupport.unmarshal(expXml);
    }

    private void thenActualFormatsEqualsExpectedFormats() {
	Assert.assertEquals(1, actFormats.getFormats().size());
	Format actFormat = actFormats.getFormats().iterator().next();
	Assert.assertEquals(expFormat.getDescription(), actFormat.getDescription());
	Assert.assertEquals(expFormat.getIconUrl(), actFormat.getIconUrl());
	Assert.assertEquals(expFormat.getId(), actFormat.getId());
	Assert.assertEquals(expFormat.getName(), actFormat.getName());
    }

}
