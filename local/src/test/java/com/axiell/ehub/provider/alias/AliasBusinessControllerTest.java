package com.axiell.ehub.provider.alias;


import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.NotFoundException;
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
public class AliasBusinessControllerTest {
    private static final String CONTENT_PROVIDER_NAME = "TEST_EP";
    private AliasBusinessController underTest;
    @Mock
    private IAliasMappingRepository aliasMappingRepository;
    @Mock
    private AliasMapping aliasMapping;
    private String aliasValue;
    private String actualContentProviderName;

    @Before
    public void setUpUnderTest() {
        underTest = new AliasBusinessController();
        ReflectionTestUtils.setField(underTest, "aliasMappingRepository", aliasMappingRepository);
    }

    @Test
    public void getName_noAlias() {
        givenNoAlias();
        try {
            whenGetName();
            fail("A BadRequestException should have been thrown");
        } catch (NullPointerException e) {
            fail("A NullPointerException should not be thrown");
        } catch (BadRequestException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void getName_unknownAlias() {
        givenUnknownAlias();
        try {
            whenGetName();
            fail("A NotFoundException should have been thrown");
        } catch (NotFoundException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void getName_knownAlias() {
        givenKnownAlias();
        givenRoutingRule();
        whenGetName();
        thenActualContentProviderNameEqualsExpectedContentProviderName();
    }

    private void thenActualContentProviderNameEqualsExpectedContentProviderName() {
        Assert.assertEquals(CONTENT_PROVIDER_NAME, actualContentProviderName);
    }

    private void givenRoutingRule() {
        given(aliasMapping.getName()).willReturn(CONTENT_PROVIDER_NAME);
        given(aliasMappingRepository.findOneByAlias(any(Alias.class))).willReturn(aliasMapping);
    }

    private void givenKnownAlias() {
        aliasValue = CONTENT_PROVIDER_NAME;
    }

    private void givenNoAlias() {
        aliasValue = null;
    }

    private void whenGetName() {
        actualContentProviderName = underTest.getName(aliasValue);
    }

    private void givenUnknownAlias() {
        aliasValue = "unknownAlias";
    }
}
