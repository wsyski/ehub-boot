package com.axiell.ehub.testdata;

public interface ITestDataServiceClientFactory {

    ITestDataServiceClient create(String baseUri);
}
