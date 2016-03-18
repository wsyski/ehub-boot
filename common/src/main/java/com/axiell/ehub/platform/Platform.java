package com.axiell.ehub.platform;

import com.axiell.ehub.AbstractTimestampAwarePersistable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

@Entity
@Table(name = "PLATFORM", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}, name = "UK_PLATFORM"))
@Access(AccessType.PROPERTY)
@XmlAccessorType(XmlAccessType.NONE)
public class Platform extends AbstractTimestampAwarePersistable<Long> implements Serializable {
    private String name;

    public Platform() {
    }

    public Platform(final String name) {
        this.name = name;
    }

    @Column(name = "NAME")
    @XmlAttribute(name = "name", required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        Platform rhs = (Platform) obj;
        return new EqualsBuilder().append(getName(), rhs.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(getName()).toHashCode();
    }

}