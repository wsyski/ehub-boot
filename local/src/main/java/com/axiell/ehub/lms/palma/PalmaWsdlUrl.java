package com.axiell.ehub.lms.palma;

import com.axiell.ehub.InternalServerErrorException;

import java.net.MalformedURLException;
import java.net.URL;

class PalmaWsdlUrl {
    private final String url;

    PalmaWsdlUrl(String url) {
        this.url = url;
    }

    String asString() {
        return url;
    }

    URL asURL() {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            throw new InternalServerErrorException("Invalid PALMA WSDL URL: " + url, ex);
        }
    }
}
