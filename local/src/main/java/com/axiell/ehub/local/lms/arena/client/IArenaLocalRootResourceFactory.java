package com.axiell.ehub.local.lms.arena.client;

import com.axiell.ehub.local.lms.arena.controller.IArenaLocalRootResource;

import java.util.Locale;

public interface IArenaLocalRootResourceFactory {
    IArenaLocalRootResource create(String localRestApiEndpoint, Locale locale);
}
