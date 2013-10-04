/*
 * Copyright (c) 2010 Axiell Library Group AB.
 */
package com.axiell.ehub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

/**
 * This class provides the possibility to create new instances of {@link TabbedPanel} or {@link AjaxTabbedPanel}. It
 * should be sub-classed to provide the specific content panels.
 * 
 * @param I the type of the tab identifiers
 */
public abstract class AbstractTabbedPanelBuilder<I> implements Serializable {
    private static final long serialVersionUID = 6891072246705982114L;

    /**
     * Indicates that there are no tabs to be shown.
     */
    public static final int ZERO_TABS = 0;
    /**
     * Indicates that there is only one tab to be shown.
     */
    public static final int ONE_TAB = 1;

    /**
     * The first tab is the selected tab by default.
     */
    protected static final int DEFAULT_SELECTED_TAB = 0;

    // Required parameters
    private final String panelId;

    // Optional parameters
    private int selectedTab = DEFAULT_SELECTED_TAB;
    private boolean ajaxable = true;

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
     * Sets whether a {@link TabbedPanel} or an {@link AjaxTabbedPanel} should be built, default is <code>true</code>
     * i.e. that an {@link AjaxTabbedPanel} should be built.
     * 
     * @param value <code>true</code> if and and only an {@link AjaxTabbedPanel} should be built, <code>false</code> if
     * a {@link TabbedPanel} should be built
     * @return this builder
     */
    public final AbstractTabbedPanelBuilder<I> ajaxable(boolean value) {
        this.ajaxable = value;
        return this;
    }

    /**
     * Creates a content {@link Panel} to be included in a tab.
     * 
     * @param panelId the ID of the panel
     * @param identifier the identifier of the tab
     * @return a new content {@link Panel}
     */
    public abstract Panel getContentPanel(String panelId, I identifier);

    /**
     * Creates a new instance of {@link TabbedPanel} or {@link AjaxTabbedPanel} (depending on whether ajaxable parameter
     * is set to <code>true</code> or <code>false</code>) containing different tabs.
     * 
     * @return a new instance of {@link TabbedPanel} or {@link AjaxTabbedPanel}
     */
    public final TabbedPanel getTabbedPanel() {
        final List<ITab> tabList = new ArrayList<ITab>();

        List<I> identifiers = getIdentifiers();
        Validate.notNull(identifiers, "The set of tab identifiers can't be null");
        Validate.notEmpty(identifiers, "The set of tab identifiers can't be empty");

        for (I identifier : identifiers) {
            ITab tab = createTab(identifier);
            tabList.add(tab);
        }

        TabbedPanel tabbedPanel;
        if (ajaxable) {
            tabbedPanel = new IndicatingAjaxTabbedPanel(panelId, tabList);
        } else {
            tabbedPanel = new TabbedPanel(panelId, tabList);
        }
        tabbedPanel.setSelectedTab(selectedTab);
        return tabbedPanel;
    }

    /**
     * Creates a new {@link Panel}. Which {@link Panel} that is created depends on the number of identifiers.
     * 
     * <ul>
     * <li>If there are no identifiers the provided 'zero tabs panel' (see
     * {@link AbstractTabbedPanelBuilder#zeroTabsPanel(Panel)}) is returned if it's not null, otherwise an
     * {@link EmptyPanel} is returned</li>
     * <li>If there is only one identifier the {@link Panel} returned by {@link AbstractTabbedPanelBuilder#getContentPanel(String, Object)} is returned</li>
     * <li>If there are more than one identifier the {@link Panel} returned by {@link AbstractTabbedPanelBuilder#getTabbedPanel()} is returned</li>
     * </ul>
     * 
     * @return a new {@link Panel}
     */
    public final Panel getPanel() {
        List<I> identifiers = getIdentifiers();

        if (identifiers.size() == ZERO_TABS) {
            return new EmptyPanel(panelId);
        } else if (identifiers.size() == ONE_TAB) {
            I identifier = identifiers.get(0);
            return getContentPanel(panelId, identifier);
        } else {
            return getTabbedPanel();
        }
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
     * Returns a {@link List} of tab identifiers. The order of this {@link List} determines the order of the tabs.
     * However, it is possible to place a specific tab first using the method {@link #isFirstTab(Object)}.
     * 
     * @return a {@link List} of tab identifiers
     */
    protected abstract List<I> getIdentifiers();

    /**
     * Gets the title of the tab represented by the provided identifier.
     * 
     * @param identifier the identifier of the tab to create the title from
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
        return new AbstractTab(titleModel) {
            private static final long serialVersionUID = 7859053907977388949L;

            /**
             * @see org.apache.wicket.extensions.markup.html.tabs.AbstractTab#getPanel(java.lang.String)
             */
            @Override
            public Panel getPanel(String panelId) {
                return getContentPanel(panelId, identifier);
            }
        };
    }

    /**
     * An {@link AjaxTabbedPanel} that displays an indicator when the tabs are changed.
     * 
     */
    private static class IndicatingAjaxTabbedPanel extends AjaxTabbedPanel implements IAjaxIndicatorAware {
        private static final long serialVersionUID = -5450368523869500939L;
        private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();

        /**
         * Constructs a new {@link IndicatingAjaxTabbedPanel}.
         * 
         * @param id the panel id
         * @param tabs the tabs to show
         */
        public IndicatingAjaxTabbedPanel(String id, List<ITab> tabs) {
            super(id, tabs);
            add(indicatorAppender);
        }

        /**
         * @see org.apache.wicket.ajax.IAjaxIndicatorAware#getAjaxIndicatorMarkupId()
         */
        @Override
        public String getAjaxIndicatorMarkupId() {
            return indicatorAppender.getMarkupId();
        }
    }
}
