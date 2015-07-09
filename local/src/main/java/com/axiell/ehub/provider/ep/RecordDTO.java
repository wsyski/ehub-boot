package com.axiell.ehub.provider.ep;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordDTO {
    private String id;

    public String getId() {
        return id;
    }

    private List<String> formats;

    public List<String> getFormats() {
        return formats;
    }
}
