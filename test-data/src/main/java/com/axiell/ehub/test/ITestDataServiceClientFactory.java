package com.axiell.ehub.test;

public interface ITestDataServiceClientFactory {

    ITestDataServiceClient create(String baseUri);
}
