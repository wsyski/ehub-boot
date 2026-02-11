package com.axiell.ehub.test;

import com.axiell.ehub.test.controller.internal.dto.TestDataDTO;

public interface ITestDataServiceClient {

    TestDataDTO init(String contentProviderName, boolean isLoanPerProduct);

    void delete();
}
