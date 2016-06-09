package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.util.JacksonTimestampInSecondsDeserializer;
import com.axiell.ehub.util.JacksonTimestampInSecondsSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private String loanId;
    private String contentUrl;
    @JsonSerialize(using = JacksonTimestampInSecondsSerializer.class)
    @JsonDeserialize(using = JacksonTimestampInSecondsDeserializer.class)
    @JsonProperty(value = "endDate")
    private Date expirationDate;

    public CheckoutDTO(final String loanId, final String contentUrl, final Date expirationDate) {
        this.loanId = loanId;
        this.contentUrl = contentUrl;
        this.expirationDate = expirationDate;
    }

    private CheckoutDTO() {
    }

    public String getLoanId() {
        return loanId;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CheckoutDTO)) {
            return false;
        }
        final CheckoutDTO rhs = (CheckoutDTO) obj;
        return new EqualsBuilder().append(getLoanId(), rhs.getLoanId()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getLoanId()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

