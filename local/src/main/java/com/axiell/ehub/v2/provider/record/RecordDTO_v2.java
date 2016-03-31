package com.axiell.ehub.v2.provider.record;

import com.axiell.ehub.provider.record.format.FormatDTO;
import com.axiell.ehub.v2.provider.record.format.FormatDTO_v2;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordDTO_v2 implements Serializable {
    private String id;
    private List<FormatDTO_v2> formats;

    public String getId() {
        return id;
    }

    public RecordDTO_v2 id(String id) {
        this.id = id;
        return this;
    }

    public List<FormatDTO_v2> getFormats() {
        return formats;
    }

    public RecordDTO_v2 formats(List<FormatDTO_v2> formats) {
        this.formats = formats;
        return this;
    }
}
