package com.axiell.ehub.support;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.security.AuthInfo;
import org.apache.wicket.model.Model;

import java.io.Serializable;

class RequestParameters implements Serializable {
    private EhubConsumer ehubConsumer;
    private String libraryCard;
    private String pin;
    private String contentProviderName;
    private String contentProviderRecordId;
    private String language;
    private String baseUri;
    private String absoluteUri;

    public EhubConsumer getEhubConsumer() {
        return ehubConsumer;
    }

    public void setEhubConsumer(EhubConsumer ehubConsumer) {
        this.ehubConsumer = ehubConsumer;
    }

    public String getLibraryCard() {
        return libraryCard;
    }

    public void setLibraryCard(String libraryCard) {
        this.libraryCard = libraryCard;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getContentProviderName() {
        return contentProviderName;
    }

    public void setContentProviderName(String contentProviderName) {
        this.contentProviderName = contentProviderName;
    }

    public String getContentProviderRecordId() {
        return contentProviderRecordId;
    }

    public void setContentProviderRecordId(String contentProviderRecordId) {
        this.contentProviderRecordId = contentProviderRecordId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getAbsoluteUri() {
        return absoluteUri;
    }

    public void setAbsoluteUri(String absoluteUri) {
        this.absoluteUri = absoluteUri;
    }
}
