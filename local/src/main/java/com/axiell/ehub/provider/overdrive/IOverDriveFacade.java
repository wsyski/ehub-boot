package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.overdrive.CirculationFormatDTO.LinkTemplatesDTO.DownloadLinkTemplateDTO;

public interface IOverDriveFacade {

    OAuthAccessToken getOAuthAccessToken(ContentProviderConsumer contentProviderConsumer);

    Product getProduct(ContentProviderConsumer contentProviderConsumer, String crossRefId, String formatType);

    OAuthAccessToken getPatronOAuthAccessToken(ContentProviderConsumer contentProviderConsumer, String libraryCard, String pin);

    CheckoutDTO checkout(ContentProviderConsumer contentProviderConsumer, OAuthAccessToken patronAccessToken, String productId, String formatType);

    CheckoutsDTO getCheckouts(ContentProviderConsumer contentProviderConsumer, OAuthAccessToken patronAccessToken);

    CirculationFormatsDTO getCirculationFormats(ContentProviderConsumer contentProviderConsumer, OAuthAccessToken patronAccessToken, String productId);

    CirculationFormatDTO lockFormat(ContentProviderConsumer contentProviderConsumer, OAuthAccessToken patronAccessToken, String productId, String formatType);

    DownloadLinkDTO getDownloadLink(ContentProviderConsumer contentProviderConsumer, OAuthAccessToken patronAccessToken,
                                    DownloadLinkTemplateDTO downloadLinkTemplate);

    void checkin(ContentProviderConsumer contentProviderConsumer, OAuthAccessToken patronAccessToken, String productId);
}
