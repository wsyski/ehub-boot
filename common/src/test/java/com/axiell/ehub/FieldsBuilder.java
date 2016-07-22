package com.axiell.ehub;

public class FieldsBuilder {
    public static final String LMS_RECORD_ID = "recordId";
    public static final String CONTENT_PROVIDER_FORMAT_ID = "providerFormatId";
    public static final String CONTENT_PROVIDER_RECORD_ID = "providerRecordId";
    public static final String CONTENT_PROVIDER_ISSUE_ID = "providerIssueId";
    public static final String CONTENT_PROVIDER_ALIAS = "ELIB3";
    private Fields fields;

    public FieldsBuilder() {
        this.fields = new Fields();
    }

    public FieldsBuilder withLmsRecordId() {
        fields.addValue("lmsRecordId", LMS_RECORD_ID);
        return this;
    }

    public FieldsBuilder withContentProviderFormatId() {
        fields.addValue("contentProviderFormatId", CONTENT_PROVIDER_FORMAT_ID);
        return this;
    }

    public FieldsBuilder withContentProviderRecordId() {
        fields.addValue("contentProviderRecordId", CONTENT_PROVIDER_RECORD_ID);
        return this;
    }

    public FieldsBuilder withContentProviderAlias() {
        fields.addValue("contentProviderAlias", CONTENT_PROVIDER_ALIAS);
        return this;
    }

    public FieldsBuilder withContentProviderIssueId() {
        fields.addValue("contentProviderIssueId", CONTENT_PROVIDER_ALIAS);
        return this;
    }

    public Fields build() {
        return fields;
    }

    public static Fields defaultFields() {
        return new FieldsBuilder().withContentProviderAlias().withContentProviderFormatId().withContentProviderRecordId().withLmsRecordId().build();
    }
}
