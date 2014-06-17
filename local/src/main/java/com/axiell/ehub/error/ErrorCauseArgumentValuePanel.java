package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgumentValue;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type;

class ErrorCauseArgumentValuePanel extends BreadCrumbPanel {
    private final ErrorCauseArgumentValueMediator mediator;
    private final Type type;

    @SpringBean(name = "errorCauseArgumentValueAdminController")
    private IErrorCauseArgumentValueAdminController errorCauseArgumentValueAdminController;

    ErrorCauseArgumentValuePanel(final String id, final IBreadCrumbModel breadCrumbModel, final Type type) {
        super(id, breadCrumbModel);
        mediator = new ErrorCauseArgumentValueMediator();
        mediator.registerErrorCauseArgumentValuePanel(this);
        this.type = type;
        addFeedbackPanel();
        addOrReplaceErrorCauseArgumentValueTextsForm();
    }

    @Override
    public String getTitle() {
        return type.name();
    }

    @Override
    public void onActivate(final IBreadCrumbParticipant previous) {
        addOrReplaceErrorCauseArgumentValueTextsForm();
        super.onActivate(previous);
    }

    private void addFeedbackPanel() {
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
    }

    private void addOrReplaceErrorCauseArgumentValueTextsForm() {
        final ErrorCauseArgumentValue argumentValue = errorCauseArgumentValueAdminController.findBy(type);
        final ErrorCauseArgumentValueTextsForm textsForm = new ErrorCauseArgumentValueTextsForm("textsForm", argumentValue, mediator);
        addOrReplace(textsForm);
    }
}
