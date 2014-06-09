package com.axiell.ehub;

import com.axiell.ehub.language.Language;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Access(AccessType.PROPERTY)
public class ErrorCauseArgumentValueTextBundle implements Serializable {
    private String text;

    protected ErrorCauseArgumentValueTextBundle() {
    }

    @Column(name = "BUNDLE_TEXT", nullable = false)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}