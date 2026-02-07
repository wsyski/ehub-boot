/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Skeletal implementation of a {@link Panel} that contains a {@link BreadCrumbBar}. It should be sub-classed to provide
 * the default active {@link Panel} of the {@link BreadCrumbBar}.
 */
public abstract class AbstractBreadCrumbBarPanel<P extends BreadCrumbPanel> extends Panel {
    private static final long serialVersionUID = -1938099865547679793L;

    /**
     * Constructs a new {@link AbstractBreadCrumbBarPanel}
     * 
     * @param panelId the ID of this {@link Panel}
     */
    public AbstractBreadCrumbBarPanel(final String panelId) {
        super(panelId);

        final BreadCrumbBar breadCrumbBar = new BreadCrumbBar("breadCrumbBar");
        add(breadCrumbBar);

        final BreadCrumbPanel activePanel = getActivePanel("breadCrumb", breadCrumbBar);
        add(activePanel);
        breadCrumbBar.setActive(activePanel);
    }

    /**
     * Gets the default active {@link Panel} in the {@link BreadCrumbBar}.
     * 
     * @param activePanelId the ID of the active {@link Panel}
     * @param breadCrumbModel the Model of the {@link BreadCrumbBar}
     * @return a {@link Panel}
     */
    public abstract P getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel);
}
