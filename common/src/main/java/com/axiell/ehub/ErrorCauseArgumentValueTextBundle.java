package com.axiell.ehub;

import com.axiell.ehub.language.Language;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

@Entity
@Table(name = "ERROR_C_A_V_TEXT_BUNDLE")
@Access(AccessType.PROPERTY)
public class ErrorCauseArgumentValueTextBundle extends AbstractTimestampAwarePersistable<Long> {
    private Language language;
    private String text;

    protected ErrorCauseArgumentValueTextBundle() {
    }

    @ManyToOne
    @JoinColumn(name = "LANGUAGE_ID", nullable = false)
    @ForeignKey(name = "FK_ERROR_C_A_V_T_B_LANGUAGE")
    public Language getLanguage() {
        return language;
    }

    protected void setLanguage(final Language language) {
        this.language = language;
    }

    @Column(name = "TEXT", nullable = false)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}