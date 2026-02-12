/*
 * Copyright (c) 2010 Axiell Library Group AB.
 */
package com.axiell.ehub.local;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides the possibility to getInstance new instances of
 * AjaxTabbedPanel. It should be sub-classed to provide the specific content
 * panels.
 *
 * @param I the type of the tab identifiers
 */
public abstract class AbstractTabbedPanelBuilder<I> implements Serializable {
    /**
     * The first tab is the selected tab by default.
     */
    protected static final int DEFAULT_SELECTED_TAB = 0;
    /**
     * Indicates that there are no tabs to be shown.
     */
    private static final int ZERO_TABS = 0;
    // Required parameters
    private final String panelId;

    // Optional parameters
    private int selectedTab = DEFAULT_SELECTED_TAB;

    /**
     * Constructs a new {@link AbstractTabbedPanelBuilder}.
     *
     * @param panelId the unique ID of the {@link AjaxTabbedPanel} to be created
     */
    public AbstractTabbedPanelBuilder(String panelId) {
        Validate.notNull(panelId, "The panel ID can't be null");
        this.panelId = panelId;
    }

    /**
     * Creates a contentLink {@link Panel} to be included in a tab.
     *
     * @param panelId    the ID of the panel
     * @param identifier the identifier of the tab
     * @return a new contentLink {@link Panel}
     */
    public abstract Panel getContentPanel(String panelId, I identifier);

    /**
     * Creates a new instance of {@link TabbedPanel} or {@link AjaxTabbedPanel}
     * (depending on whether ajaxable parameter is set to <code>true</code> or
     * <code>false</code>) containing different tabs.
     *
     * @return a new instance of {@link TabbedPanel} or {@link AjaxTabbedPanel}
     */
    private TabbedPanel getTabbedPanel() {
        final List<ITab> tabList = new ArrayList<ITab>();

        List<I> identifiers = getIdentifiers();
        Validate.notNull(identifiers, "The set of tab identifiers can't be null");
        Validate.notEmpty(identifiers, "The set of tab identifiers can't be empty");

        for (I identifier : identifiers) {
            ITab tab = createTab(identifier);
            tabList.add(tab);
        }

        TabbedPanel tabbedPanel = new IndicatingAjaxTabbedPanel(panelId, tabList);
        tabbedPanel.setSelectedTab(selectedTab);
        return tabbedPanel;
    }

    /**
     * Creates a new {@link Panel}. Which {@link Panel} that is created depends
     * on the number of identifiers.
     *
     * @return a new {@link Panel}
     */
    public final Panel getPanel() {
        List<I> identifiers = getIdentifiers();

        if (identifiers.size() == ZERO_TABS)
            return new EmptyPanel(panelId);
        return getTabbedPanel();
    }

    /**
     * Sets the selected tab.
     *
     * @param selectedTab the number of the selected tab
     */
    protected final void setSelectedTab(int selectedTab) {
        this.selectedTab = selectedTab;
    }

    /**
     * Returns a {@link List} of tab identifiers. The order of this {@link List}
     * determines the order of the tabs. However, it is possible to place a
     * specific tab first using the method {@link #isFirstTab(Object)}.
     *
     * @return a {@link List} of tab identifiers
     */
    protected abstract List<I> getIdentifiers();

    /**
     * Gets the title of the tab represented by the provided identifier.
     *
     * @param identifier the identifier of the tab to getInstance the title from
     * @return the title of the tab
     */
    protected abstract String getTitle(I identifier);

    /**
     * Creates a {@link ITab} using the provided identifier.
     *
     * @param identifier the identifier of the tab
     * @return a new {@link ITab}
     */
    private ITab createTab(final I identifier) {
        String title = getTitle(identifier);
        IModel<String> titleModel = new Model<String>(title);
        return new IdentifierTab<I>(titleModel, this, identifier);
    }

    private static class IndicatingAjaxTabbedPanel extends AjaxTabbedPanel implements IAjaxIndicatorAware {
        private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();

        private IndicatingAjaxTabbedPanel(String id, List<ITab> tabs) {
            super(id, tabs);
            add(indicatorAppender);
        }

        @Override
        public String getAjaxIndicatorMarkupId() {
            return indicatorAppender.getMarkupId();
        }
    }

    private static class IdentifierTab<I> extends AbstractTab {
        private final AbstractTabbedPanelBuilder<I> tabbedPanelBuilder;
        private final I identifier;

        IdentifierTab(final IModel<String> titleModel, final AbstractTabbedPanelBuilder<I> tabbedPanelBuilder, final I identifier) {
            super(titleModel);
            this.tabbedPanelBuilder = tabbedPanelBuilder;
            this.identifier = identifier;
        }

        @Override
        public Panel getPanel(String panelId) {
            return tabbedPanelBuilder.getContentPanel(panelId, identifier);
        }
    }
}
