/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import com.axiell.ehub.consumer.EhubConsumersBreadCrumbBarPanel;
import com.axiell.ehub.error.ErrorCausesBreadCrumbBarPanel;
import com.axiell.ehub.home.HomePanel;
import com.axiell.ehub.language.LanguagesBreadCrumbBarPanel;
import com.axiell.ehub.provider.ContentProvidersBreadCrumbBarPanel;
import com.axiell.ehub.user.AdminUser;
import com.axiell.ehub.user.LogoutPanel;
import com.axiell.ehub.version.VersionBreadCrumbBarPanel;
import org.apache.commons.lang3.Validate;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import java.util.Arrays;
import java.util.List;

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
        final Panel tabbedPanel = builder.getPanel();
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
                case ERROR_CAUSES:
                    return new ErrorCausesBreadCrumbBarPanel(panelId);
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
                case ERROR_CAUSES:
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
        HOME, EHUB_CONSUMERS, CONTENT_PROVIDERS, ERROR_CAUSES, LANGUAGES, VERSION;
    }
}
