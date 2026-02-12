package com.axiell.ehub.local.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {
    private final List<Field> fields;

    private Fields() {
        fields = new ArrayList<>();
    }

    public List<Field> getFields() {
        return fields;
    }

    private void add(Field field) {
        fields.add(field);
    }

    static class Builder {
        private final ReserveIdField reserverIdField;
        private FormatTypeField formatTypeField;

        Builder(final String productId) {
            this.reserverIdField = new ReserveIdField(productId);
        }

        Builder formatType(final String formatType) {
            this.formatTypeField = formatType == null ? null : new FormatTypeField(formatType);
            return this;
        }

        Fields build() {
            final Fields fields = new Fields();
            fields.add(reserverIdField);

            if (formatTypeField != null) {
                fields.add(formatTypeField);
            }

            return fields;
        }
    }
}
