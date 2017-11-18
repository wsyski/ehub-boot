package com.axiell.ehub.provider.alias;

import com.axiell.ehub.AbstractTimestampAwarePersistable;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.util.HashCodeBuilderFactory;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.*;

@Entity
@Table(name = "CONTENT_PROVIDER_ALIAS")
@Access(AccessType.PROPERTY)
public class AliasMapping extends AbstractTimestampAwarePersistable<Long> {
    private Alias alias;
    private String name;

    public AliasMapping() {
    }

    @Embedded
    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    @Column(name = "NAME", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AliasMappingDTO toDTO() {
        return new AliasMappingDTO(alias.getValue(), name);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AliasMapping)) {
            return false;
        }
        AliasMapping rhs = (AliasMapping) obj;
        return new EqualsBuilder().append(getAlias(), rhs.getAlias()).append(getName(), rhs.getName()).isEquals();
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilderFactory.create().append(getAlias()).append(getName()).toHashCode();
    }

}
