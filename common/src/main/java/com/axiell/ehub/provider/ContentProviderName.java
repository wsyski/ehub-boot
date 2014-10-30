/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import javax.xml.bind.annotation.XmlEnum;

/**
 * The constant name of a {@link ContentProvider}.
 */
@XmlEnum(String.class)
public enum ContentProviderName {
    ELIB, ELIBU, PUBLIT, ASKEWS, OVERDRIVE, ELIB3, F1, OCD;
}
