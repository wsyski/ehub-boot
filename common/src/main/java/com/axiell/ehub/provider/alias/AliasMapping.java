package com.axiell.ehub.provider.alias;

import com.axiell.ehub.AbstractTimestampAwarePersistable;

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
}
