package com.axiell.ehub.local.feedback;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import static org.apache.wicket.feedback.FeedbackMessage.ERROR;
import static org.apache.wicket.feedback.FeedbackMessage.INFO;

public class EhubFeedbackPanel extends FeedbackPanel {
    private static final String CSS_INFO = "alert info";
    private static final String CSS_ERROR = "alert error";
    private static final String CSS_DEFAULT = "alert";

    public EhubFeedbackPanel(final String panelId) {
        super(panelId);
    }

    @Override
    protected String getCSSClass(final FeedbackMessage message) {
        final int level = message.getLevel();
        switch (level) {
            case INFO:
                return CSS_INFO;
            case ERROR:
                return CSS_ERROR;
            default:
                return CSS_DEFAULT;
        }
    }
}
