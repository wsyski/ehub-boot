package com.axiell.ehub.error;

import java.io.Serializable;

class ErrorCauseArgumentValueMediator implements Serializable {
    private ErrorCauseArgumentValuePanel errorCauseArgumentValuePanel;

    void registerErrorCauseArgumentValuePanel(final ErrorCauseArgumentValuePanel errorCauseArgumentValuePanel) {
        this.errorCauseArgumentValuePanel = errorCauseArgumentValuePanel;
    }

    void afterSavedTexts() {
        errorCauseArgumentValuePanel.activate(errorCauseArgumentValuePanel);
    }
}
