package com.axiell.ehub.lms.arena.client;

import com.axiell.ehub.lms.arena.resources.IRootResource;

import java.util.Locale;

public interface IRootResourceFactory {
    IRootResource createRootResource(String localRestApiEndpoint, Locale locale);
}
