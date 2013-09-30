package com.axiell.ehub;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.util.Locale;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatAdminController;

public abstract class AbstractRemoteFormatIT extends AbstractRemoteIT<DevelopmentData> {    
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();    
    protected Formats actualFormats;

    @Override
    protected DevelopmentData initDevelopmentData(XmlWebApplicationContext applicationContext, IContentProviderAdminController contentProviderAdminController,
            IFormatAdminController formatAdminController, IConsumerAdminController consumerAdminController) {
        return new DevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController);
    }
    
    @Test
    public final void getFormats() throws EhubException {
	givenGetProductResponse();
	whenGetFormats();
	thenActualFormatsContainsExpectedComponents();
	thenCustomFormatsValidation();
    }
    
    private void givenGetProductResponse() {
	stubFor(post(urlEqualTo("/webservices/GetProduct.asmx/GetProduct")).willReturn(aResponse().withBodyFile("GetProductResponse.xml").withStatus(200)));
    }
    
    protected abstract void whenGetFormats() throws EhubException;
    
    private void thenActualFormatsContainsExpectedComponents() {
	Assert.assertNotNull(actualFormats);
	Set<Format> formatSet = actualFormats.getFormats();
	Assert.assertNotNull(formatSet);
	Assert.assertFalse(formatSet.isEmpty());
	
	for (Format format : formatSet) {
	    thenFormatContainsExpectedComponents(format);
	}
    }
    
    private void thenFormatContainsExpectedComponents(Format format) {
	String id = format.getId();
	Assert.assertNotNull(id);
	String name = format.getName();
	Assert.assertNotNull(name);
    }
    
    protected void thenCustomFormatsValidation() {	
    }
}
