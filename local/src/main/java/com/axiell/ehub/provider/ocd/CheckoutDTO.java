package com.axiell.ehub.provider.ocd;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(value = {"minutes", "encryptionKey", "size", "bookmarks", "hasBookmark", "dateAdded", "lastRead",
        "description", "narrators", "images", "authors", "canRenew", "mediaType", "patronId", "libraryId", "isbn",
        "title", "publisher", "hasDrm"})
public class CheckoutDTO {
    private String transactionId;
    @JsonProperty(value = "expiration")
    private Date expirationDate;
    private String downloadUrl;
    private String titleId;
    private List<FileDTO> files;
    private String output;

    public String getTransactionId() {
        return transactionId;
    }

    public String getTitleId() {
        return titleId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getOutput() {
        return output;
    }

    public List<FileDTO> getFiles() {
        return files;
    }
    @JsonIgnoreProperties(value = {"id", "display", "filename", "minutes", "size", "fileFormat", "fileFormatId", "acsResourceId", "textFormatType"})
    public static class FileDTO {
        private String downloadUrl;

        public String getDownloadUrl() {
            return downloadUrl;
        }
    }
}
