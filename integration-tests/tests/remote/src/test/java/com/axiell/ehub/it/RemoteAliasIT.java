package com.axiell.ehub.it;

import com.axiell.ehub.test.TestDataConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RemoteAliasIT extends RemoteITFixture {
    private static final String INVALID_ALIAS = "invalidAlias";

    private boolean isValidAlias;

    @Test
    public final void validAlias() {
        whenIsValidAlias(TestDataConstants.CONTENT_PROVIDER_TEST_EP);
        thenExpectedIsValidAlias(true);
    }

    @Test
    public final void invalidAlias() {
        whenIsValidAlias(INVALID_ALIAS);
        thenExpectedIsValidAlias(false);
    }

    @Test
    public final void isValidAliasMultiple() {
        whenIsValidAlias(TestDataConstants.CONTENT_PROVIDER_TEST_EP);
        thenExpectedIsValidAlias(true);
        whenIsValidAlias(INVALID_ALIAS);
        thenExpectedIsValidAlias(false);
        whenIsValidAlias(TestDataConstants.CONTENT_PROVIDER_ALIAS_PREFIX + TestDataConstants.CONTENT_PROVIDER_TEST_EP);
        thenExpectedIsValidAlias(true);
    }

    private void thenExpectedIsValidAlias(final boolean value) {
        assertThat(isValidAlias, is(value));
    }

    private void whenIsValidAlias(final String alias) {
        isValidAlias = underTest.isValidAlias(alias);
    }

    @Override
    protected boolean isLoanPerProduct() {
        return true;
    }

    @Override
    protected String getContentProviderName() {
        return TestDataConstants.CONTENT_PROVIDER_TEST_EP;
    }
}
