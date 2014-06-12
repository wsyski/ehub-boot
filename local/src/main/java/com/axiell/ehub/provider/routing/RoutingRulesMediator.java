package com.axiell.ehub.provider.routing;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import java.io.Serializable;

class RoutingRulesMediator implements Serializable {
    private RoutingRulesPanel routingRulesPanel;
    private RoutingRuleCreateModalWindow createModalWindow;
    private FeedbackPanel createFeedbackPanel;

    public void registerRoutingRulesPanel(final RoutingRulesPanel routingRulesPanel) {
        this.routingRulesPanel = routingRulesPanel;
    }

    void registerCreateModalWindow(final RoutingRuleCreateModalWindow modalWindow) {
        this.createModalWindow = modalWindow;
    }

    void registerCreateFeedbackPanel(final FeedbackPanel feedback) {
        this.createFeedbackPanel = feedback;
    }

    void showCreateRoutingRuleWindow(final AjaxRequestTarget target) {
        createModalWindow.show(target);
    }

    void afterRoutingRuleWasCreated(final AjaxRequestTarget target) {
        createModalWindow.close(target);
        replaceCurrentRoutingRulesPanelWithNewInstance(target);
    }

    void onRoutingRuleCreateError(final AjaxRequestTarget target) {
        if (target != null)
            target.addComponent(createFeedbackPanel);
    }

    void afterRoutingRuleWasDeleted() {
        routingRulesPanel.activate(routingRulesPanel);
    }

    private void replaceCurrentRoutingRulesPanelWithNewInstance(AjaxRequestTarget target) {
        final RoutingRulesPanel newRoutingRulesPanel = new RoutingRulesPanel(routingRulesPanel.getId(), routingRulesPanel.getBreadCrumbModel());
        routingRulesPanel.replaceWith(newRoutingRulesPanel);
        routingRulesPanel = newRoutingRulesPanel;

        if (target != null)
            target.addComponent(newRoutingRulesPanel);
    }
}
