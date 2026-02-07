package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.ErrorCauseArgumentValue;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class ErrorCauseArgumentBuilder {
    private final IErrorCauseArgumentValueRepository errorCauseArgumentValueRepository;
    private final ErrorCauseArgument.Type argumentType;
    private final ErrorCauseArgumentType valueType;
    private String language;
    private String defaultLanguage;

    public ErrorCauseArgumentBuilder(final IErrorCauseArgumentValueRepository errorCauseArgumentValueRepository, final ErrorCauseArgument.Type type,
                                     final ErrorCauseArgumentType valueType) {
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
        final ErrorCauseArgumentValue errorCauseArgumentValue = errorCauseArgumentValueRepository.findOneByType(valueType);
        if (errorCauseArgumentValue == null) {
            log.warn("Missing error cause argument value for type: " + valueType.name());
            return valueType.name();
        } else {
            return errorCauseArgumentValue.getText(language, defaultLanguage);
        }
    }
}
