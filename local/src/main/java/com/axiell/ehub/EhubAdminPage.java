/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.axiell.ehub.consumer.EhubConsumersPanel;
import com.axiell.ehub.home.HomePanel;
import com.axiell.ehub.language.LanguagesPanel;
import com.axiell.ehub.provider.ContentProvidersPanel;
import com.axiell.ehub.user.AdminUser;
import com.axiell.ehub.user.LogoutPanel;
import com.axiell.ehub.version.VersionPanel;

/**
 * The main page of the {@link EhubAdminApplication} when the user is logged in.
 */
public class EhubAdminPage extends AbstractBasePage {
    
    public EhubAdminPage(final AdminUser adminUser) {
        Validate.notNull(adminUser, "Invalid usage of this page - the user must be logged in to access this page");
        addLogoutPanel();
        addTabbedPanel();
    }

    private void addLogoutPanel() {
	final LogoutPanel logoutPanel = new LogoutPanel("logout");
        add(logoutPanel);
    }
    
    private void addTabbedPanel() {
	final EhubAdminTabbedPanelBuilder builder = new EhubAdminTabbedPanelBuilder("tabs", this);        
        final TabbedPanel tabbedPanel = builder.getTabbedPanel();
        add(tabbedPanel);
    }
    
    private static class EhubAdminTabbedPanelBuilder extends AbstractTabbedPanelBuilder<Tab> {
	private final EhubAdminPage ehubAdminPage;
	
	private EhubAdminTabbedPanelBuilder(final String panelId, final EhubAdminPage ehubAdminPage) {
	    super(panelId);
	    this.ehubAdminPage = ehubAdminPage;
	}

	@Override
        public Panel getContentPanel(final String panelId, final Tab identifier) {
            switch (identifier) {
                case HOME:
                    return new HomePanel(panelId);
                case EHUB_CONSUMERS:
                    return new EhubConsumersBreadCrumbBarPanel(panelId);
                case CONTENT_PROVIDERS:
                    return new ContentProvidersBreadCrumbBarPanel(panelId);
                case LANGUAGES:
                    return new LanguagesBreadCrumbBarPanel(panelId);
                case VERSION:
                    return new VersionBreadCrumbBarPanel(panelId);
                default:
                    throw new IllegalArgumentException("Unknown tab identifier '" + identifier + "'");
            }
        }

        @Override
        protected List<Tab> getIdentifiers() {
            return Arrays.asList(Tab.values());
        }

        @Override
        protected String getTitle(final Tab identifier) {
            final String titleKey;
            
            switch (identifier) {
                case HOME:
                case EHUB_CONSUMERS:
                case CONTENT_PROVIDERS:
                case LANGUAGES:
                case VERSION:
                    titleKey = identifier.toString();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown tab identifier '" + identifier + "'");
            }
            
            final StringResourceModel titleModel = new StringResourceModel(titleKey, ehubAdminPage, new Model<>());
            return titleModel.getString();
        }
    }

    /**
     * Represents a tab in the tabbed panel.
     */
    private static enum Tab {
        HOME, EHUB_CONSUMERS, CONTENT_PROVIDERS, LANGUAGES, VERSION;
    }
    
    private static class EhubConsumersBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<EhubConsumersPanel> {
	
	private EhubConsumersBreadCrumbBarPanel(final String panelId) {
	    super(panelId);
	}
	
	@Override
	EhubConsumersPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
	    return new EhubConsumersPanel(activePanelId, breadCrumbModel);
	}
    }
    
    private static class ContentProvidersBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<ContentProvidersPanel> {
	
	private ContentProvidersBreadCrumbBarPanel(final String panelId) {
	    super(panelId);
	}
	
	@Override
	ContentProvidersPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
	    return new ContentProvidersPanel(activePanelId, breadCrumbModel);
	}
    }
    
    private static class LanguagesBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<LanguagesPanel> {
	
	private LanguagesBreadCrumbBarPanel(final String panelId) {
	    super(panelId);
	}
	
	@Override
	LanguagesPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
	    return new LanguagesPanel(activePanelId, breadCrumbModel);
	}
    }
    
    private static class VersionBreadCrumbBarPanel extends AbstractBreadCrumbBarPanel<VersionPanel> {
	private VersionBreadCrumbBarPanel(final String panelId) {
	    super(panelId);
	}
	
	@Override
	VersionPanel getActivePanel(String activePanelId, IBreadCrumbModel breadCrumbModel) {
	    return new VersionPanel(activePanelId, breadCrumbModel);
	}
    }
}
