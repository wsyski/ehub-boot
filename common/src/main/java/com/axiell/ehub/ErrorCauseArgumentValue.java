package com.axiell.ehub;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "ERROR_CAUSE_ARGUMENT_VALUE", uniqueConstraints = @UniqueConstraint(columnNames = {"TYPE"}))
@Access(AccessType.PROPERTY)
public class ErrorCauseArgumentValue extends AbstractTimestampAwarePersistable<Long> {
    private Type type;
    private String defaultText;
    private Map<String , ErrorCauseArgumentValueTextBundle> textBundles;

    protected ErrorCauseArgumentValue() {
    }

    @Column(name = "TYPE")
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Column(name = "DEFAULT_TEXT", nullable = false)
    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    @OneToMany(mappedBy = "argumentValue", fetch = FetchType.LAZY)
    @MapKey(name = "language")
    public Map<String, ErrorCauseArgumentValueTextBundle> getTextBundles() {
        return textBundles;
    }

    public void setTextBundles(Map<String, ErrorCauseArgumentValueTextBundle> textBundles) {
        this.textBundles = textBundles;
    }

    @Transient
    public String getText(final String language) {
        if (language == null)
            return getDefaultText();
        final ErrorCauseArgumentValueTextBundle textBundle = getTextBundles().get(language);
        if (textBundle == null)
            return getDefaultText();
        final String text = textBundle.getText();
        return text == null ? getDefaultText() : text;
    }

    public static enum Type {
        PRODUCT_INACTIVE, MISSING_CONTENT_IN_LOAN, INACTIVE_LOAN, MAX_NO_OF_DOWNLOADS_FOR_PRODUCT_REACHED, LIBRARY_LIMIT_REACHED, BORROWER_LIMIT_REACHED, PRODUCT_UNAVAILABLE
    }
}
