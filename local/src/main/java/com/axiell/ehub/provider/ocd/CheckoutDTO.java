package com.axiell.ehub.provider.ocd;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(value = {"minutes", "encryptionKey", "size", "bookmarks", "hasBookmark", "dateAdded", "lastRead",
        "description", "narrators", "images", "authors", "canRenew", "mediaType", "patronId", "libraryId", "titleId", "isbn",
        "title", "publisher", "hasDrm"})
public class CheckoutDTO {
    private String transactionId;
    @JsonProperty(value = "expiration")
    private Date expirationDate;
    private String downloadUrl;
    private List<FileDTO> files;
    private String output;

    public String getTransactionId() {
        return transactionId;
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
    @JsonIgnoreProperties(value = {"id", "display", "filename", "minutes", "size", "fileFormat"})
    public static class FileDTO {
        private String downloadUrl;

        public String getDownloadUrl() {
            return downloadUrl;
        }
    }
}
