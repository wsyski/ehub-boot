package com.axiell.ehub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FieldsDTO {
    private final Map<String, String> fields;

    public FieldsDTO() {
        fields = new HashMap<>();
    }

    public Map<String, String> getFields() {
        return fields;
    }
}
