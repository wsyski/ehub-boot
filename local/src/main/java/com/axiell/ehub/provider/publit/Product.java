package com.axiell.ehub.provider.publit;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private Integer id;
    private String type;
    private String title;
    private String subTitle;
    private String publisher;
    private String original_publication_year;
    
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

    public String getOriginal_publication_year() {
        return original_publication_year;
    }
}
