package com.axiell.ehub.support.request;

import com.axiell.ehub.consumer.EhubConsumer;
import org.apache.wicket.Component;
import org.apache.wicket.protocol.http.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class RequestArguments implements Serializable {
    private EhubConsumer ehubConsumer;
    private String libraryCard;
    private String pin;
    private String contentProviderName;
    private String contentProviderRecordId;
    private String language;
    private String baseUri;
    private String lmsRecordId;
    private String formatId;
    private String lmsLoanId;

    RequestArguments(Component component) {
        setBaseUri(component);
    }

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

    public String getFormatId() {
        return formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

    public String getLmsRecordId() {
        return lmsRecordId;
    }

    public void setLmsRecordId(String lmsRecordId) {
        this.lmsRecordId = lmsRecordId;
    }

    public String getLmsLoanId() {
        return lmsLoanId;
    }

    public void setLmsLoanId(String lmsLoanId) {
        this.lmsLoanId = lmsLoanId;
    }

    public String getBaseUri() {
        return baseUri;
    }

    private void setBaseUri(Component component) {
        WebRequest webRequest = (WebRequest) component.getRequest();
        HttpServletRequest httpServletRequest = webRequest.getHttpServletRequest();
        int port = httpServletRequest.getServerPort();
        String serverName = httpServletRequest.getServerName();
        String scheme = httpServletRequest.getScheme();
        baseUri = scheme + "://" + serverName + ":" + port;
    }
}
