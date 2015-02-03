/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Represents a getContent that can be streamed, e.g. it can be a Flash player.
 */
public class StreamingContent implements IContent {
    private String url;
    private int width;
    private int height;

    /**
     * Empty constructor required by JAXB.
     */
    protected StreamingContent() {
    }
    
    /**
     * Constructs a new {@link StreamingContent}.
     * 
     * @param url the URL to the getContent to be streamed
     * @param width the width of the player in pixels
     * @param height the height of the player in pixels 
     */
    public StreamingContent(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the URL to the getContent to be streamed.
     * 
     * @return the URL to the getContent
     */
    @XmlAttribute(name = "url", required = true)
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL to the getContent to be streamed.
     * 
     * @param url the URL to the getContent to be streamed to set
     */
    protected void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the width of the player in pixels.
     * 
     * @return the width of the player in pixels
     */
    @XmlAttribute(name = "width", required = true)
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the player in pixels.
     * 
     * @param width the width of the player in pixels to set
     */
    protected void setWidth(int width) {
        this.width = width;
    }

    /**
     * Returns the height of the player in pixels.
     * 
     * @return the height of the player in pixels
     */
    @XmlAttribute(name = "height", required = true)
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the player in pixels.
     * 
     * @param height the height of the player in pixels to set
     */
    protected void setHeight(int height) {
        this.height = height;
    }
}
