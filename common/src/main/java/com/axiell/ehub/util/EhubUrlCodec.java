/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import com.axiell.ehub.InternalServerErrorException;

/**
 * Utility class that provides the possibility to URL encode and decode strings.
 */
public final class EhubUrlCodec {
    private static final URLCodec URL_CODEC = new URLCodec(EhubCharsets.UTF_8);
    
    /**
     * Private constructor that prevents direct instantiation.
     */
    private EhubUrlCodec() {        
    }
    
    /**
     * Auth Info URL encodes the provided string.
     * 
     * <p>
     * Note that the eHUB encodes some characters differently (same as OAuth).
     * </p>
     * 
     * @param str the string to URL encode
     * @return the URL encoded string
     */
    public static String authInfoEncode(String str) {
        try {
            return URL_CODEC.encode(str).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (EncoderException e) {
            throw new InternalServerErrorException("Could not URL encode string '" + str + "'", e);
        }
    }

    /**
     * URL encodes the provided string.
     *
     * <p>
     * Note that the eHUB encodes some characters differently (same as OAuth).
     * </p>
     *
     * @param str the string to URL encode
     * @return the URL encoded string
     */
    public static String encode(String str) {
        try {
            return URL_CODEC.encode(str);
        } catch (EncoderException e) {
            throw new InternalServerErrorException("Could not URL encode string '" + str + "'", e);
        }
    }

    /**
     * URL decodes the provided string.
     * 
     * @param str the string to URL decode.
     * @return the URL decoded string
     */
    public static String decode(String str) {
        try {
            return URL_CODEC.decode(str);
        } catch (DecoderException e) {
            throw new InternalServerErrorException("Could not URL decode string '" + str + "'", e);
        }
    }
}
