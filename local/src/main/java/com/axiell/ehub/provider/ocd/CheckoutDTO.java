package com.axiell.ehub.provider.ocd;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(value = {"minutes", "encryptionKey", "size", "bookmarks", "hasBookmark", "dateAdded", "lastRead",
        "description", "narrators", "images", "authors", "canRenew", "mediaType", "patronId", "libraryId", "titleId",
        "title", "publisher", "hasDrm"})
public class CheckoutDTO {
    private String transactionId;
    @JsonProperty(value = "expiration")
    private Date expirationDate;
    private String downloadUrl;
    private String isbn;
    private List<FileDTO> files;
    private String output;

    public String getTransactionId() {
        return transactionId;
    }

    public String getIsbn() {
        return isbn;
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
