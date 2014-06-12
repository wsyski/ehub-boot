package com.axiell.ehub.provider.routing;

import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

class RoutingRuleCreateForm extends StatelessForm<RoutingRule> {
    private final RoutingRulesMediator mediator;

    RoutingRuleCreateForm(final String id, final RoutingRulesMediator mediator) {
        super(id);
        this.mediator = mediator;
        final RoutingRule routingRule = new RoutingRule();
        setModel(routingRule);
        addSourceField(routingRule);
        addTargetDropDownChoice(routingRule);
        addCreateButton();
        addFeedbackPanel();
    }

    private void setModel(final RoutingRule routingRule) {
        final IModel<RoutingRule> formModel = new Model<>(routingRule);
        setModel(formModel);
    }

    private void addSourceField(final RoutingRule routingRule) {
        final SourceField sourceField = new SourceField("source", routingRule);
        add(sourceField);
    }

    private void addTargetDropDownChoice(final RoutingRule routingRule) {
        final TargetDropDownChoice targetDropDownChoice = new TargetDropDownChoice("target", routingRule);
        add(targetDropDownChoice);
    }

    private void addCreateButton() {
        final RoutingRuleCreateButton createButton = new RoutingRuleCreateButton("createButton", mediator);
        add(createButton);
    }

    private void addFeedbackPanel() {
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        mediator.registerCreateFeedbackPanel(feedbackPanel);
        add(feedbackPanel);
    }

}
