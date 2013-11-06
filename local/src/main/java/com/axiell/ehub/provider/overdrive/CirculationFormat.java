package com.axiell.ehub.provider.overdrive;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.axiell.ehub.util.HashCodeBuilderFactory;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CirculationFormat {
    private String reserveId;
    private String formatType;
    private LinkTemplates linkTemplates;
    
    public CirculationFormat() {	
    }
    
    CirculationFormat(final String reserveId, final String formatType) {
	this.reserveId = reserveId;
	this.formatType = formatType;
    }
    
    public String getReserveId() {
	return reserveId;
    }
    
    public String getFormatType() {
	return formatType;
    }
    
    public LinkTemplates getLinkTemplates() {
	return linkTemplates;
    }
    
    @Override
    public boolean equals(Object obj) {
	if (obj == this) {
	    return true;
	}
	
	if (!(obj instanceof CirculationFormat)) {
	    return false;
	}
	
	final CirculationFormat rhs = (CirculationFormat) obj;	
        return new EqualsBuilder().append(reserveId, rhs.getReserveId()).append(formatType, rhs.getFormatType()).isEquals();
    }
    
    @Override
    public int hashCode() {
        return HashCodeBuilderFactory.create().append(reserveId).append(formatType).toHashCode();
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LinkTemplates {
	private DownloadLinkTemplate downloadLink;
	
	public DownloadLinkTemplate getDownloadLink() {
	    return downloadLink;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DownloadLinkTemplate {
	    private String href;
	    
	    public String getHref() {
		return href;
	    }
	    
	    void setHref(String href) {
		this.href = href;
	    }
	}
    }
}
