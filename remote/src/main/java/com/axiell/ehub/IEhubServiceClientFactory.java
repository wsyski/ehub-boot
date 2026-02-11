package com.axiell.ehub;

public interface IEhubServiceClientFactory {

    IEhubServiceClient create(String baseUri);
}
