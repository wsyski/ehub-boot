package com.axiell.ehub.v1.provider.record.format;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axiell.ehub.v1.XjcSupport;

public class FormatsV1Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormatsV1Test.class);
    private String expXml;
    private Formats_v1 expFormats;
    private Format_v1 expFormat;
    private Formats_v1 actFormats;

    @Before
    public void setUp() {
	expFormat = new Format_v1("id1", "name1", "description1", "iconUrl1");
	expFormats = new Formats_v1();
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
	actFormats = (Formats_v1) XjcSupport.unmarshal(expXml);
    }

    private void thenActualFormatsEqualsExpectedFormats() {
	Assert.assertEquals(1, actFormats.getFormats().size());
	Format_v1 actFormat = actFormats.getFormats().iterator().next();
	Assert.assertEquals(expFormat.getDescription(), actFormat.getDescription());
	Assert.assertEquals(expFormat.getIconUrl(), actFormat.getIconUrl());
	Assert.assertEquals(expFormat.getId(), actFormat.getId());
	Assert.assertEquals(expFormat.getName(), actFormat.getName());
    }

}
