package com.axiell.ehub.provider.ep;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordDTO {
    private String id;
    private List<FormatDTO> formats;

    public String getId() {
        return id;
    }

    public List<FormatDTO> getFormats() {
        return formats;
    }
}
