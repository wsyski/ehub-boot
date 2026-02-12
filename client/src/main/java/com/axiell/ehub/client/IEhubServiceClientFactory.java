package com.axiell.ehub.client;

public interface IEhubServiceClientFactory {

    IEhubServiceClient create(String baseUri);
}
