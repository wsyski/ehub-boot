package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgumentValue;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.ErrorCauseArgumentType;

class ErrorCauseArgumentValuePanel extends BreadCrumbPanel {
    private final ErrorCauseArgumentValueMediator mediator;
    private final ErrorCauseArgumentType type;

    @SpringBean(name = "errorCauseArgumentValueAdminController")
    private IErrorCauseArgumentValueAdminController errorCauseArgumentValueAdminController;

    ErrorCauseArgumentValuePanel(final String id, final IBreadCrumbModel breadCrumbModel, final ErrorCauseArgumentType type) {
        super(id, breadCrumbModel);
        mediator = new ErrorCauseArgumentValueMediator();
        mediator.registerErrorCauseArgumentValuePanel(this);
        this.type = type;
        addFeedbackPanel();
        addOrReplaceErrorCauseArgumentValueTextsForm();
    }

    @Override
    public IModel<String> getTitle() {
        return new Model<>(type.name());
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
