package com.axiell.ehub.support.about;

import java.io.IOException;

public class ManifestException extends Exception {

    public ManifestException(IOException e) {
        super(e);
    }
}
