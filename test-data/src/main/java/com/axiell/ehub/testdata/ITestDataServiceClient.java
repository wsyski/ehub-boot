package com.axiell.ehub.testdata;

import com.axiell.ehub.testdata.controller.internal.dto.TestDataDTO;

public interface ITestDataServiceClient {

    TestDataDTO init(String contentProviderName, boolean isLoanPerProduct);

    void delete();
}
