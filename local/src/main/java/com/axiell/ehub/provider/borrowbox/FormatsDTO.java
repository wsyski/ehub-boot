package com.axiell.ehub.provider.borrowbox;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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
