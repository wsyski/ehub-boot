package com.axiell.ehub.common;

import com.axiell.ehub.common.language.Language;
import org.apache.commons.lang3.StringUtils;

import  jakarta.persistence.Access;
import  jakarta.persistence.AccessType;
import  jakarta.persistence.CascadeType;
import  jakarta.persistence.Column;
import  jakarta.persistence.Entity;
import  jakarta.persistence.EnumType;
import  jakarta.persistence.Enumerated;
import  jakarta.persistence.ForeignKey;
import  jakarta.persistence.JoinColumn;
import  jakarta.persistence.MapKeyJoinColumn;
import  jakarta.persistence.OneToMany;
import  jakarta.persistence.Table;
import  jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "ERROR_CAUSE_ARGUMENT_VALUE")
@Access(AccessType.PROPERTY)
public class ErrorCauseArgumentValue extends AbstractTimestampAwarePersistable<Long> implements Serializable {
    private ErrorCauseArgumentType type;
    private Map<Language, ErrorCauseArgumentValueTextBundle> textBundles;

    protected ErrorCauseArgumentValue() {
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", unique = true)
    public ErrorCauseArgumentType getType() {
        return type;
    }

    public void setType(ErrorCauseArgumentType type) {
        this.type = type;
    }

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ERROR_CAUSE_ARGUMENT_VALUE_ID")
    @MapKeyJoinColumn(name = "LANGUAGE_ID", nullable = false, foreignKey= @ForeignKey(name = "FK_ERROR_C_A_V_T_B_ERROR_C_A_V"))
    public Map<Language, ErrorCauseArgumentValueTextBundle> getTextBundles() {
        return textBundles;
    }

    public void setTextBundles(Map<Language, ErrorCauseArgumentValueTextBundle> textBundles) {
        this.textBundles = textBundles;
    }

    @Transient
    public String getText(final String language, final String defaultLanguage) {
        String text = getText(language);
        if (StringUtils.isEmpty(text)) {
            text = getText(defaultLanguage);
            if (StringUtils.isEmpty(text)) {
                text = getType().name();
            }
        }
        return text;
    }

    private String getText(final String language) {
        if (language == null) {
            return null;
        }
        final ErrorCauseArgumentValueTextBundle textBundle = getTextBundles().get(new Language(language));
        if (textBundle == null) {
            return null;
        }
        return textBundle.getText();
    }
}
