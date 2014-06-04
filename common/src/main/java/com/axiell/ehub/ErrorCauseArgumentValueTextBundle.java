package com.axiell.ehub;

import com.axiell.ehub.language.Language;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

@Entity
@Table(name = "ERROR_C_A_V_TEXT_BUNDLE", uniqueConstraints = @UniqueConstraint(columnNames = {"ERROR_CAUSE_ARGUMENT_VALUE_ID", "LANGUAGE_ID"}))
@Access(AccessType.PROPERTY)
public class ErrorCauseArgumentValueTextBundle extends AbstractTimestampAwarePersistable<Long> {
    private ErrorCauseArgumentValue argumentValue;
    private Language language;
    private String text;

    protected ErrorCauseArgumentValueTextBundle() {
    }

    @ManyToOne
    @JoinColumn(name = "ERROR_CAUSE_ARGUMENT_VALUE_ID", nullable = false)
    @ForeignKey(name = "FK_ERROR_C_A_V_ERROR_C_A_V_T_B")
    public ErrorCauseArgumentValue getArgumentValue() {
        return argumentValue;
    }

    public void setArgumentValue(ErrorCauseArgumentValue errorCause) {
        this.argumentValue = errorCause;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LANGUAGE_ID", nullable = false)
    @ForeignKey(name = "FK_ERROR_C_A_V_LANGUAGE")
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(final Language language) {
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