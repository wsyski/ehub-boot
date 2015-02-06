/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.loan;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Represents a downloadable contentLink.
 */
public class DownloadableContent_v1 implements IContent_v1 {
    private String url;

    /**
     * Empty constructor required by JAXB.
     */
    protected DownloadableContent_v1() {
    }
    
    /**
     * Constructs a new {@link DownloadableContent_v1}.
     * 
     * @param url the URL of the contentLink to be downloaded
     */
    public DownloadableContent_v1(String url) {
        this.url = url;
    }
    
    /**
     * Returns the URL of the contentLink to be downloaded.
     * 
     * @return the URL of the contentLink to be downloaded
     */
    @XmlAttribute(name = "url", required = true)
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the contentLink to be downloaded.
     * 
     * @param url the URL to set
     */
    protected void setUrl(String url) {
        this.url = url;
    }
}
