package com.axiell.ehub.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public final class EhubAddress {
    private static final String PRODUCTION_HOST = "ehub.axiell.com";

    private EhubAddress() {
    }

    public static String getProductionHostAddress() {
	try {
	    InetAddress address = Inet4Address.getByName(PRODUCTION_HOST);
	    return address.getHostAddress();
	} catch (UnknownHostException e) {
	    log.error("Could not get the host address of '" + PRODUCTION_HOST +  "'", e);
	    return PRODUCTION_HOST;
	}
    }
}
