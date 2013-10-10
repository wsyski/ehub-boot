package com.axiell.ehub.provider.publit;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private Integer id;
    private String type;
    private String title;
    private String subTitle;
    private String publisher;
    @JsonProperty("original_publication_year")
    private String originalPublicationYear;

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getOriginalPublicationYear() {
        return originalPublicationYear;
    }
}
