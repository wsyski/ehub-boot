package com.axiell.ehub.it;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.test.TestDataConstants;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class RemoteAliasIT extends RemoteITFixture {

    private boolean isValidAlias;

    @Test
    public final void isValidAlias()  {
        whenIsValidAlias(TestDataConstants.CONTENT_PROVIDER_TEST_EP);
        thenExpectedIsValidAlias();
    }

    @Test
    public final void isValidAliasMultiple() {
        whenIsValidAlias(TestDataConstants.CONTENT_PROVIDER_TEST_EP);
        thenExpectedIsValidAlias();
        whenIsValidAlias(TestDataConstants.CONTENT_PROVIDER_ALIAS_PREFIX + TestDataConstants.CONTENT_PROVIDER_TEST_EP);
        thenExpectedIsValidAlias();
    }

    private void thenExpectedIsValidAlias() {
        Assert.assertThat(isValidAlias, is(true));
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
