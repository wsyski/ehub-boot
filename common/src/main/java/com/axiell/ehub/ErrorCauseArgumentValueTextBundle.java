package com.axiell.ehub;

import com.axiell.ehub.language.Language;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

@Entity
@Table(name = "ERROR_C_A_V_TEXT_BUNDLE")
@Access(AccessType.PROPERTY)
public class ErrorCauseArgumentValueTextBundle extends AbstractTimestampAwarePersistable<Long> {
    public static final int MAX_TEXT_LENGTH = 255;
    private ErrorCauseArgumentValue argumentValue;
    private Language language;
    private String text;

    protected ErrorCauseArgumentValueTextBundle() {
    }

    public ErrorCauseArgumentValueTextBundle(ErrorCauseArgumentValue argumentValue, Language language, String text) {
        this.argumentValue = argumentValue;
        this.language = language;
        this.text = text;
    }

    @ManyToOne
    @JoinColumn(name = "ERROR_CAUSE_ARGUMENT_VALUE_ID", nullable = false)
    @ForeignKey(name = "FK_ERROR_C_A_V_T_B_ERROR_C_A_V")
    public ErrorCauseArgumentValue getArgumentValue() {
        return argumentValue;
    }

    public void setArgumentValue(ErrorCauseArgumentValue errorCause) {
        this.argumentValue = errorCause;
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

    @Column(name = "TEXT", length = MAX_TEXT_LENGTH)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}