package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.overdrive.CirculationFormat.LinkTemplates.DownloadLinkTemplate;

public interface IOverDriveFacade {
    
    OAuthAccessToken getOAuthAccessToken(ContentProviderConsumer contentProviderConsumer);
    
    Product getProduct(ContentProviderConsumer contentProviderConsumer, String productId);

    OAuthAccessToken getPatronOAuthAccessToken(ContentProviderConsumer contentProviderConsumer, String libraryCard, String pin);
    
    Checkout checkout(ContentProviderConsumer contentProviderConsumer, OAuthAccessToken patronAccessToken, String productId, String formatType);    
    
    Checkouts getCheckouts(ContentProviderConsumer contentProviderConsumer, OAuthAccessToken patronAccessToken);
    
    DownloadLink getDownloadLink(ContentProviderConsumer contentProviderConsumer, OAuthAccessToken patronAccessToken, DownloadLinkTemplate downloadLinkTemplate);
    
    void returnTitle(ContentProviderConsumer contentProviderConsumer, OAuthAccessToken patronAccessToken, String productId);
}
