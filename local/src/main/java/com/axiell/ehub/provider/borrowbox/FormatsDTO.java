package com.axiell.ehub.provider.borrowbox;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormatsDTO {
    private String recordId;
    private List<FormatDTO> formats;

    public String getRecordId() {
        return recordId;
    }

    public List<FormatDTO> getFormats() {
        return formats;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FormatDTO {
        private String formatId;
        private String formatName;

        public String getFormatId() {
            return formatId;
        }

        public String getFormatName() {
            return formatName;
        }
    }
}
