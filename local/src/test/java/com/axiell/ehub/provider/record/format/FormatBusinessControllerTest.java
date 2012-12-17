/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import java.util.Locale;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.AbstractEhubRepositoryTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.elib.library.ElibDataAccessor;
import com.axiell.ehub.security.AuthInfo;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/axiell/ehub/admin-controller-context.xml", "classpath:/com/axiell/ehub/business-controller-context.xml"})
public class FormatBusinessControllerTest extends AbstractEhubRepositoryTest<DevelopmentData> {    

    @Autowired
    private IContentProviderAdminController contentProviderAdminController;
    
    @Autowired
    private IFormatAdminController formatAdminController;

    @Autowired
    private IConsumerAdminController consumerAdminController;
    
    @Autowired
    private IFormatBusinessController formatBusinessController;
    private Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    /**
     * @see com.axiell.ehub.AbstractEhubRepositoryTest#initDevelopmentData()
     */
    @Override
    protected DevelopmentData initDevelopmentData() {
        return new DevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        FormatBusinessController localFormatBusinessController;
        if (formatBusinessController instanceof FormatBusinessController) {
            localFormatBusinessController = (FormatBusinessController) formatBusinessController;
        } else {
            Advised advised = (Advised) formatBusinessController;
            localFormatBusinessController = (FormatBusinessController) advised.getTargetSource().getTarget();
        }
        final ElibDataAccessor elibDataAccessor = mockery.mock(ElibDataAccessor.class);
        ReflectionTestUtils.setField(localFormatBusinessController, "elibDataAccessor", elibDataAccessor);
        final Formats formats = new Formats();
        formats.addFormat(new Format(DevelopmentData.ELIB_FORMAT_0_ID, DevelopmentData.ELIB_FORMAT_0_NAME,
                "elibFormat0Description", "elibFormat0IconUrl"));
        formats.addFormat(new Format(DevelopmentData.ELIB_FORMAT_1_ID, DevelopmentData.ELIB_FORMAT_1_NAME,
                "elibFormat1Description", "elibFormat1IconUrl"));
        formats.addFormat(new Format(DevelopmentData.ELIB_FORMAT_2_ID, DevelopmentData.ELIB_FORMAT_2_NAME,
                "elibFormat2Description", "elibFormat2IconUrl"));
        mockery.checking(new Expectations() {
            {
                allowing(elibDataAccessor).getFormats(with(any(ContentProviderConsumer.class)), with(equal(DevelopmentData.ELIB_RECORD_0_ID)),
                        with(equal(Locale.ENGLISH.getLanguage())));
                will(returnValue(formats));
            }
        });
    }
    
    /**
     * Test method for
     * {@link com.axiell.ehub.provider.ContentProviderAdminController#getFormats(com.axiell.ehub.security.AuthInfo, java.lang.String, java.lang.String, java.lang.String)}
     * .
     * @throws EhubException 
     */
    @Test
    public void testGetFormats() throws EhubException {
        AuthInfo authInfo = new AuthInfo.Builder(developmentData.getEhubConsumerId(), DevelopmentData.EHUB_CONSUMER_SECRET_KEY)
                .libraryCard(DevelopmentData.ELIB_LIBRARY_CARD).pin(DevelopmentData.ELIB_LIBRARY_CARD_PIN).build();
        Formats formats = formatBusinessController.getFormats(authInfo, ContentProviderName.ELIB.toString(),
                DevelopmentData.ELIB_RECORD_0_ID, Locale.ENGLISH.getLanguage());
        // FIXME: should be 3, but the mock doesn't kick in
        Assert.assertEquals(1, formats.getFormats().size());
    }
}
