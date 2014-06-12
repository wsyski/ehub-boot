package com.axiell.ehub.provider.routing;

import com.axiell.ehub.AbstractTimestampAwarePersistable;
import com.axiell.ehub.provider.ContentProviderName;

import javax.persistence.*;

@Entity
@Table(name = "CONTENT_PROVIDER_ROUTING_RULE")
@Access(AccessType.PROPERTY)
public class RoutingRule extends AbstractTimestampAwarePersistable<Long> {
    private Source source;
    private ContentProviderName target;

    public RoutingRule() {
    }

    @Embedded
    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TARGET", nullable = false)
    public ContentProviderName getTarget() {
        return target;
    }

    public void setTarget(ContentProviderName target) {
        this.target = target;
    }
}
