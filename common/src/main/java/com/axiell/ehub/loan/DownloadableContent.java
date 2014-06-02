/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Represents a downloadable content.
 */
public class DownloadableContent implements IContent {
    private String url;

    /**
     * Empty constructor required by JAXB.
     */
    protected DownloadableContent() {
    }
    
    /**
     * Constructs a new {@link DownloadableContent}.
     * 
     * @param url the URL of the content to be downloaded
     */
    public DownloadableContent(String url) {
        this.url = url;
    }
    
    /**
     * Returns the URL of the content to be downloaded.
     * 
     * @return the URL of the content to be downloaded
     */
    @XmlAttribute(name = "url", required = true)
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the content to be downloaded.
     * 
     * @param url the URL to set
     */
    protected void setUrl(String url) {
        this.url = url;
    }
}
