/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.axiell.ehub.AbstractEhubClientTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderAdminController;

/**
 * 
 */
public class RemoteFormatTest extends AbstractEhubClientTest<DevelopmentData> {

    /**
     * @see com.axiell.ehub.AbstractRemoteEhubClientTest#initDevelopmentData(org.springframework.web.context.support.XmlWebApplicationContext, com.axiell.ehub.provider.IContentProviderAdminController, com.axiell.ehub.provider.record.format.IFormatAdminController, com.axiell.ehub.consumer.IConsumerAdminController)
     */
    @Override
    protected DevelopmentData initDevelopmentData(XmlWebApplicationContext applicationContext,
            IContentProviderAdminController contentProviderAdminController,
            IFormatAdminController formatAdminController,
            IConsumerAdminController consumerAdminController) {
        return new DevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController);
    }
    
    /**
     * Test method for {@link com.axiell.ehub.provider.record.format.RemoteFormatAdminController#getFormats(com.axiell.ehub.security.AuthInfo, java.lang.String, java.lang.String, java.lang.String)}.
     * @throws EhubException 
     */
    @Test
    public void testGetFormats() throws EhubException {
        final String expRecordId = DevelopmentData.ELIB_RECORD_0_ID;
        final String expLanguage = Locale.ENGLISH.getLanguage();
        Formats formats = ehubService.getFormats(authInfo, ContentProviderName.ELIB.toString(), expRecordId, expLanguage);
        assertNotNull(formats);
    }
    
}
