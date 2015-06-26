package com.axiell.ehub.provider.epi;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormatsDTO {
    private List<String> formats;

    public List<String> getFormats() {
        return formats;
    }
}
