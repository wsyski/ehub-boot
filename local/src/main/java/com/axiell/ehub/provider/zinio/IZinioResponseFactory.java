package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.consumer.ContentProviderConsumer;

public interface IZinioResponseFactory {
    IZinioResponse create(String response, ContentProviderConsumer contentProviderConsumer, String language);
}
