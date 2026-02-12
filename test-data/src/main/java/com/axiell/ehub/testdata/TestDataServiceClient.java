package com.axiell.ehub.testdata;

import com.axiell.ehub.testdata.controller.internal.ITestDataRootResource;
import com.axiell.ehub.testdata.controller.internal.dto.TestDataDTO;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
class TestDataServiceClient implements ITestDataServiceClient {

    private ITestDataRootResource testDataRootResource;

    @Override
    public TestDataDTO init(final String contentProviderName, final boolean isLoanPerProduct) {
        TestDataDTO testDataDTO = testDataRootResource.init(contentProviderName, isLoanPerProduct);
        log.info("Test data initialized: " + testDataDTO.toString());
        return testDataDTO;
    }

    @Override
    public void delete() {
        try (Response response = testDataRootResource.delete()) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                log.info("Test data deleted");
            } else {
                throw new IllegalStateException("Could not delete testdata data: " + response.getStatusInfo().getReasonPhrase());
            }
        }
    }
}
