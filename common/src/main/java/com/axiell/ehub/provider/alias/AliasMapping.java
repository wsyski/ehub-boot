package com.axiell.ehub.provider.alias;

import com.axiell.ehub.AbstractTimestampAwarePersistable;
import com.axiell.ehub.provider.ContentProviderName;

import javax.persistence.*;

@Entity
@Table(name = "CONTENT_PROVIDER_ALIAS")
@Access(AccessType.PROPERTY)
public class AliasMapping extends AbstractTimestampAwarePersistable<Long> {
    private Alias alias;
    private ContentProviderName name;

    public AliasMapping() {
    }

    @Embedded
    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "NAME", nullable = false)
    public ContentProviderName getName() {
        return name;
    }

    public void setName(ContentProviderName name) {
        this.name = name;
    }
}
