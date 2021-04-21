package com.axiell.ehub.lms.arena.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CheckedArenaException extends Exception {

    /**
     * The expectation given could not be met by this server, or, if the server is a proxy, the server has unambiguous
     * evidence that the request could not be met by the next-hop server.
     */
    public static final int STATUS = 417;

    private String originatingExceptionClass;
    private String creatableExceptionClass;
    private Map<String, String> propertyMap = new HashMap<String, String>();
    private List<String> messageArguments = new ArrayList<String>();

    protected CheckedArenaException() {
        setOriginatingExceptionClass(this.getClass().getName());
    }

    public CheckedArenaException(String originatingExceptionClass, String creatableExceptionClass,
                                 Map<String, String> propertyMap, List<String> messageArguments) {
        this.originatingExceptionClass = originatingExceptionClass;
        this.creatableExceptionClass = creatableExceptionClass;
        this.propertyMap = propertyMap;
        this.messageArguments = messageArguments;
    }

    protected final void setOriginatingExceptionClass(String originatingExceptionClass) {
        this.originatingExceptionClass = originatingExceptionClass;
    }

    protected final void setCreatableExceptionClass(String creatableExceptionClass) {
        this.creatableExceptionClass = creatableExceptionClass;
    }

    protected String getProperty(String propertyName) {
        if (getPropertyMap().containsKey(propertyName)) {
            return getPropertyMap().get(propertyName);
        }
        return "";
    }

    public boolean hasProperty(String property) {
        return getPropertyMap().containsKey(property);
    }

    public String getOriginatingExceptionClass() {
        return originatingExceptionClass;
    }

    public String getCreatableExceptionClass() {
        return creatableExceptionClass;
    }

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    protected final void setPropertyMap(Map<String, String> propertyMap) {
        this.propertyMap.putAll(propertyMap);
    }

    protected final void addProperty(String key, String value) {
        propertyMap.put(key, value);
    }

    public String getPropertyValue(String key) {
        return propertyMap.get(key);
    }

    public List<String> getMessageArguments() {
        return messageArguments;
    }

    protected final void setMessageArguments(List<String> messageArguments) {
        this.messageArguments.addAll(messageArguments);
    }

    protected final void addArgument(String arg) {
        messageArguments.add(arg);
    }
}
