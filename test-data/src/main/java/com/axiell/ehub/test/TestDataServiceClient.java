package com.axiell.ehub.test;

import com.axiell.ehub.test.controller.internal.ITestDataRootResource;
import com.axiell.ehub.test.controller.internal.dto.TestDataDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class TestDataServiceClient implements ITestDataServiceClient {

    private ITestDataRootResource testDataRootResource;

    @Override
    public TestDataDTO init(final String contentProviderName, final boolean isLoanPerProduct) {
        return testDataRootResource.init(contentProviderName, isLoanPerProduct);
    }

    @Override
    public void delete() {
        testDataRootResource.delete();
    }
}
