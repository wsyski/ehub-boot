/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import java.text.MessageFormat;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.axiell.ehub.AbstractEhubRepositoryTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.IFormatAdminController;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/axiell/ehub/admin-controller-context.xml", "classpath:/com/axiell/ehub/invocation-context.xml"})
public class AuthInfoConverterTest extends AbstractEhubRepositoryTest<DevelopmentData> {
    
    @Autowired
    private AuthInfoConverter authInfoConverter;

    @Autowired
    private IContentProviderAdminController contentProviderAdminController;
    
    @Autowired
    private IFormatAdminController formatAdminController;

    @Autowired
    private IConsumerAdminController consumerAdminController;
    
    /**
     * @see com.axiell.ehub.AbstractEhubRepositoryTest#initDevelopmentData()
     */
    @Override
    protected DevelopmentData initDevelopmentData() {
        return new DevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController);
    }
    
    /**
     * Test method for {@link com.axiell.ehub.security.AuthInfoConverter#fromString(java.lang.String)}.
     */
    @Test
    public void testFromString() {
        Signature expSignature = new Signature(developmentData.getEhubConsumerId(), DevelopmentData.EHUB_CONSUMER_SECRET_KEY, DevelopmentData.ELIBU_LIBRARY_CARD, DevelopmentData.ELIBU_LIBRARY_CARD_PIN);
        final String expEncodedSignature = expSignature.toString();
        String expAuthHeader = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_pin=\"{2}\", ehub_signature=\"{3}\"",
                developmentData.getEhubConsumerId(), DevelopmentData.ELIBU_LIBRARY_CARD, DevelopmentData.ELIBU_LIBRARY_CARD_PIN, expEncodedSignature);
        
        AuthInfo actAuthInfo = authInfoConverter.fromString(expAuthHeader);
        
        Long ehubConsumerId = actAuthInfo.getEhubConsumerId();
        Assert.assertNotNull(ehubConsumerId);
        
        String actLibraryCard = actAuthInfo.getLibraryCard();
        Assert.assertEquals(DevelopmentData.ELIBU_LIBRARY_CARD, actLibraryCard);
        
        String actPin = actAuthInfo.getPin();
        Assert.assertEquals(DevelopmentData.ELIBU_LIBRARY_CARD_PIN, actPin);
    }

}
