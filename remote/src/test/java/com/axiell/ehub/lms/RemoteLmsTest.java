/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.axiell.ehub.AbstractEhubClientTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.IEhubService;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.IFormatAdminController;

/**
 * 
 */
public class RemoteLmsTest extends AbstractEhubClientTest<DevelopmentData> {

    /**
     * @see com.axiell.ehub.AbstractEhubClientTest#initDevelopmentData(org.springframework.web.context.support.XmlWebApplicationContext, com.axiell.ehub.provider.IContentProviderAdminController, com.axiell.ehub.provider.record.format.IFormatAdminController, com.axiell.ehub.consumer.IConsumerAdminController)     * com.axiell.ehub.provider.IContentProviderAdminController,
     * com.axiell.ehub.provider.record.format.IFormatAdminController, com.axiell.ehub.consumer.IConsumerAdminController)
     */
    @Override
    protected DevelopmentData initDevelopmentData(XmlWebApplicationContext applicationContext,
            IContentProviderAdminController contentProviderAdminController,
            IFormatAdminController formatAdminController,
            IConsumerAdminController consumerAdminController) {
        return new DevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController);
    }
}
