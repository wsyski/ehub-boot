package com.axiell.ehub.provider;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/axiell/ehub/invocation-context.xml"})
public class ContentProviderDataAccessorFactoryTest {
    @Autowired(required = true)
    private IContentProviderDataAccessorFactory contentProviderDataAccessorFactory;
    
    @Test
    public void testContentProviderDataAccessorFactory() {
        Assert.assertEquals(ContentProviderName.ELIB, contentProviderDataAccessorFactory.getInstance(ContentProviderName.ELIB).getContentProviderName());
        Assert.assertEquals(ContentProviderName.ELIBU, contentProviderDataAccessorFactory.getInstance(ContentProviderName.ELIBU).getContentProviderName());
        Assert.assertEquals(ContentProviderName.PUBLIT, contentProviderDataAccessorFactory.getInstance(ContentProviderName.PUBLIT).getContentProviderName());
    }
}
