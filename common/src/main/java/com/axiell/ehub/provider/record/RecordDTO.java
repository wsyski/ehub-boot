package com.axiell.ehub.provider.record;

import com.axiell.ehub.provider.record.format.FormatDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordDTO implements Serializable {
    private String id;
    private List<FormatDTO> formats;

    public String getId() {
        return id;
    }

    public RecordDTO id(String id) {
        this.id = id;
        return this;
    }

    public List<FormatDTO> getFormats() {
        return formats;
    }

    public RecordDTO formats(List<FormatDTO> formats) {
        this.formats = formats;
        return this;
    }
}
