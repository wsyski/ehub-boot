package com.axiell.ehub.provider.overdrive;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
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
