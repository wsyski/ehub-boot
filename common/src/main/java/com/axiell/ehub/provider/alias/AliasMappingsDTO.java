package com.axiell.ehub.provider.alias;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AliasMappingsDTO {

    @JsonProperty(value="aliasMappings")
    private Set<AliasMappingDTO> aliasMappingsDTO = new HashSet<>();

    public Set<AliasMappingDTO> toDTO() {
        return aliasMappingsDTO;
    }

    private AliasMappingsDTO() {
    }

    public AliasMappingsDTO(final Set<AliasMappingDTO> aliasMappingsDTO) {
        this.aliasMappingsDTO = aliasMappingsDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AliasMappingsDTO that = (AliasMappingsDTO) o;
        return new EqualsBuilder().append(aliasMappingsDTO, that.aliasMappingsDTO).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(aliasMappingsDTO).toHashCode();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
