package com.axiell.ehub.provider.routing;


import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.provider.ContentProviderName;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class RoutingBusinessControllerTest {
    private static final ContentProviderName CONTENT_PROVIDER_NAME = ContentProviderName.ELIB;
    private RoutingBusinessController underTest;
    @Mock
    private IRoutingRuleRepository routingRuleRepository;
    @Mock
    private RoutingRule routingRule;
    private String sourceValue;
    private ContentProviderName actualContentProviderName;

    @Before
    public void setUpUnderTest() {
        underTest = new RoutingBusinessController();
        ReflectionTestUtils.setField(underTest, "routingRuleRepository", routingRuleRepository);
    }

    @Test
    public void getTarget_noSource() {
        givenNoSource();
        try {
            whenGetTarget();
            fail("A BadRequestException should have been thrown");
        } catch (NullPointerException e) {
            fail("A NullPointerException should not be thrown");
        } catch (BadRequestException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void getTarget_unknownSource() {
        givenUnknownSource();
        try {
            whenGetTarget();
            fail("A NotFoundException should have been thrown");
        } catch (NotFoundException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void getTarget_knownSource() {
        givenKnownSource();
        givenRoutingRule();
        whenGetTarget();
        thenActualContentProviderNameEqualsExpectedContentProviderName();
    }

    private void thenActualContentProviderNameEqualsExpectedContentProviderName() {
        Assert.assertEquals(CONTENT_PROVIDER_NAME, actualContentProviderName);
    }

    private void givenRoutingRule() {
        given(routingRule.getTarget()).willReturn(CONTENT_PROVIDER_NAME);
        given(routingRuleRepository.findBySource(any(Source.class))).willReturn(routingRule);
    }

    private void givenKnownSource() {
        sourceValue = CONTENT_PROVIDER_NAME.name();
    }

    private void givenNoSource() {
        sourceValue = null;
    }

    private void whenGetTarget() {
        actualContentProviderName = underTest.getTarget(sourceValue);
    }

    private void givenUnknownSource() {
        sourceValue = "getTarget_unknownSource";
    }
}
