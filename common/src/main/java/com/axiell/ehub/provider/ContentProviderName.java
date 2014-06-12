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
    ELIB, ELIBU, PUBLIT, ASKEWS, OVERDRIVE, ELIB3;
}
