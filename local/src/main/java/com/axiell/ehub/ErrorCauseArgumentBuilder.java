package com.axiell.ehub;

public class ErrorCauseArgumentBuilder {
    private final IErrorCauseArgumentValueRepository errorCauseArgumentValueRepository;
    private final ErrorCauseArgument.Type argumentType;
    private final ErrorCauseArgumentValue.Type valueType;
    private String language;
    private String defaultLanguage;

    public ErrorCauseArgumentBuilder(final IErrorCauseArgumentValueRepository errorCauseArgumentValueRepository, final ErrorCauseArgument.Type type,
                                     final ErrorCauseArgumentValue.Type valueType) {
        this.errorCauseArgumentValueRepository = errorCauseArgumentValueRepository;
        this.argumentType = type;
        this.valueType = valueType;
    }

    public ErrorCauseArgumentBuilder language(final String language) {
        this.language = language;
        return this;
    }

    public ErrorCauseArgumentBuilder defaultLanguage(final String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
        return this;
    }

    public ErrorCauseArgument build() {
        final String value = getValue();
        return new ErrorCauseArgument(argumentType, value);
    }

    private String getValue() {
        final ErrorCauseArgumentValue errorCauseArgumentValue = errorCauseArgumentValueRepository.findByType(valueType);
        return errorCauseArgumentValue.getText(language, defaultLanguage);
    }
}
