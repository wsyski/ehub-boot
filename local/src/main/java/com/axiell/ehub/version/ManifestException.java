package com.axiell.ehub.version;

import java.io.IOException;

public class ManifestException extends Exception {

    public ManifestException(IOException e) {
	super(e);
    }
}
