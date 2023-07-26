package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.util.HashCodeBuilderFactory;
import org.apache.commons.lang3.builder.EqualsBuilder;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CirculationFormatDTO {
    private String reserveId;
    private String formatType;
    private LinkTemplatesDTO linkTemplates;

    public CirculationFormatDTO() {
    }

    CirculationFormatDTO(final String reserveId, final String formatType) {
        this.reserveId = reserveId;
        this.formatType = formatType;
    }

    public String getReserveId() {
        return reserveId;
    }

    public String getFormatType() {
        return formatType;
    }

    public LinkTemplatesDTO getLinkTemplates() {
        return linkTemplates;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof CirculationFormatDTO)) {
            return false;
        }

        final CirculationFormatDTO rhs = (CirculationFormatDTO) obj;
        return new EqualsBuilder().append(reserveId, rhs.getReserveId()).append(formatType, rhs.getFormatType()).isEquals();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilderFactory.create().append(reserveId).append(formatType).toHashCode();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LinkTemplatesDTO {
        private DownloadLinkTemplateDTO downloadLink;

        public DownloadLinkTemplateDTO getDownloadLink() {
            return downloadLink;
        }

    }
}
