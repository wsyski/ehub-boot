package com.axiell.ehub.local.language;

import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.language.Language;

import java.util.List;

public class LanguageReferencedException extends Exception {
    private List<EhubConsumer> referencedObjects;
    private Language language;

    public LanguageReferencedException(final Language language, final List<EhubConsumer> referencedObjects) {
        super();
        this.language = language;
        this.referencedObjects = referencedObjects;
    }

    public List<EhubConsumer> getReferencedObjects() {
        return referencedObjects;
    }

    public Language getLanguage() {
        return language;
    }

    public String getReferencedObjectNames() {
        return referencedObjectNames(referencedObjects);
    }

    private String referencedObjectNames(final List<EhubConsumer> ehubConsumers) {
        StringBuilder sb = new StringBuilder();
        for (EhubConsumer ehubConsumer : ehubConsumers) {
            if (sb.length() != 0) {
                sb.append(',');
                sb.append(' ');
            }
            sb.append(ehubConsumer.getDefaultLanguage());
        }
        return sb.toString();
    }
}
