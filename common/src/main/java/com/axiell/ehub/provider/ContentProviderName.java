/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.NotFoundException;

import javax.xml.bind.annotation.XmlEnum;
import java.util.HashMap;
import java.util.Map;

/**
 * The constant name of a {@link ContentProvider}.
 */
@XmlEnum(String.class)
public enum ContentProviderName {
    ELIB, ELIBU, PUBLIT;
    
    private static final Map<String, ContentProviderName> STRING_TO_ENUM = new HashMap<>();
    
    static {
        for (ContentProviderName name : values()) {
            STRING_TO_ENUM.put(name.toString(), name);
        }
    }

    /**
     * Gets the {@link ContentProviderName} from the provided {@link String}.
     * 
     * <p>
     * This method is case-insensitive, i.e. the strings <code>elib</code>, <code>Elib</code> and <code>ELIB</code>
     * returns the same {@link ContentProviderName}.
     * </p>
     * 
     * @param name the name of the {@link ContentProviderName} to return
     * @return the {@link ContentProviderName} with the given name
     * @throws BadRequestException if the provided name is <code>null</code>
     * @throws NotFoundException if no {@link ContentProviderName} could be found for the given name
     */
    public static ContentProviderName fromString(String name) {
        if (name == null) {
            throw new BadRequestException(ErrorCause.MISSING_CONTENT_PROVIDER_NAME);
        } else {
            final String upperCaseStr = name.toUpperCase();            
            final ContentProviderName contentProviderName = STRING_TO_ENUM.get(upperCaseStr);

            if (contentProviderName == null) {
                final ErrorCauseArgument argument = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, name);
                throw new NotFoundException(ErrorCause.UNKNOWN_CONTENT_PROVIDER, argument);
            } else {
                return contentProviderName;
            }
        }
    }
}
