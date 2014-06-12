package com.axiell.ehub.provider.routing;

import com.axiell.ehub.provider.ContentProviderName;

public interface IRoutingBusinessController {

    ContentProviderName getTarget(String source);
}
