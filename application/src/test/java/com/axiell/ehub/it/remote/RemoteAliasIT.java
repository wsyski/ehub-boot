package com.axiell.ehub.it.remote;

import com.axiell.ehub.EhubApplication;
import com.axiell.ehub.IEhubServiceClient;
import com.axiell.ehub.it.config.EhubServiceClientConfig;
import com.axiell.ehub.it.config.TestDataServiceClientConfig;
import com.axiell.ehub.test.ITestDataServiceClient;
import com.axiell.ehub.test.TestDataConstants;
import com.axiell.ehub.test.config.TestDataRestApiServerConfig;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = {EhubApplication.class, TestDataRestApiServerConfig.class, EhubServiceClientConfig.class, TestDataServiceClientConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "server.port=0")
@DirtiesContext
public class RemoteAliasIT extends RemoteITFixture {
    private static final String INVALID_ALIAS = "invalidAlias";

    @Getter
    @Autowired
    private IEhubServiceClient ehubServiceClient;
    @Getter
    @Autowired
    private ITestDataServiceClient testDataServiceClient;


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
        IEhubServiceClient ehubServiceClient = getEhubServiceClient();
        isValidAlias = ehubServiceClient.isValidAlias(alias);
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
