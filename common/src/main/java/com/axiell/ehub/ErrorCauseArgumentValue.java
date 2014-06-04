package com.axiell.ehub;

import com.axiell.ehub.language.Language;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "ERROR_CAUSE_ARGUMENT_VALUE", uniqueConstraints = @UniqueConstraint(columnNames = {"TYPE"}))
@Access(AccessType.PROPERTY)
public class ErrorCauseArgumentValue extends AbstractTimestampAwarePersistable<Long> {
    private Type type;
    private Map<Language, ErrorCauseArgumentValueTextBundle> textBundles;

    protected ErrorCauseArgumentValue() {
    }

    @Column(name = "TYPE")
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @OneToMany(mappedBy = "argumentValue", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @MapKeyJoinColumn(name = "LANGUAGE_ID")
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

    public static enum Type {
        PRODUCT_INACTIVE, MISSING_CONTENT_IN_LOAN, INACTIVE_LOAN, MAX_NO_OF_DOWNLOADS_FOR_PRODUCT_REACHED, LIBRARY_LIMIT_REACHED, BORROWER_LIMIT_REACHED,
        PRODUCT_UNAVAILABLE
    }
}
