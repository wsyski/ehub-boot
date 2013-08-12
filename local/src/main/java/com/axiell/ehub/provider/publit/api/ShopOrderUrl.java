package com.axiell.ehub.provider.publit.api;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopOrderUrl {
    private Integer orderId;
    private String date;
    @JsonProperty(value = "downloadItem")
    private List<DownloadItem> downloadItems;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(final Integer orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public List<DownloadItem> getDownloadItems() {
        return downloadItems;
    }

    public void setDownloadItems(final List<DownloadItem> downloadItems) {
        this.downloadItems = downloadItems;
    }

    @JsonAutoDetect
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DownloadItem {
        private String title;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(final String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(final String url) {
            this.url = url;
        }
    }
}
