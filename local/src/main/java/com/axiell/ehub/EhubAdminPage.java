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

/**
 * The main page of the {@link EhubAdminApplication} when the user is logged in.
 */
public class EhubAdminPage extends AbstractBasePage {

    /**
     * Constructs a new {@link EhubAdminPage}.
     * 
     * @param adminUser the non-null {@link AdminUser}
     * @throws NullPointerException if the provided {@link AdminUser} is null
     */
    public EhubAdminPage(final AdminUser adminUser) {
        Validate.notNull(adminUser, "Invalid usage of this page - the user must be logged in to access this page");        
        
        final LogoutPanel logoutPanel = new LogoutPanel("logout");
        add(logoutPanel);

        final AbstractTabbedPanelBuilder<Tab> builder = new AbstractTabbedPanelBuilder<Tab>("tabs") {
            private static final long serialVersionUID = 5838155087334369948L;

            /**
             * @see com.axiell.ehub.AbstractTabbedPanelBuilder#getContentPanel(java.lang.String, java.lang.Object)
             */
            @Override
            public Panel getContentPanel(final String panelId, final Tab identifier) {
                switch (identifier) {
                    case HOME:
                        return new HomePanel(panelId);
                    case EHUB_CONSUMERS:
                        return new AbstractBreadCrumbBarPanel<EhubConsumersPanel>(panelId) {
                            private static final long serialVersionUID = -8252187196691276634L;

                            /**
                             * @see com.axiell.ehub.AbstractBreadCrumbBarPanel#getActivePanel(java.lang.String,
                             * org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel)
                             */
                            @Override
                            EhubConsumersPanel getActivePanel(final String activePanelId, final IBreadCrumbModel breadCrumbModel) {
                                return new EhubConsumersPanel(activePanelId, breadCrumbModel);
                            };
                        };
                    case CONTENT_PROVIDERS:
                        return new AbstractBreadCrumbBarPanel<ContentProvidersPanel>(panelId) {
                            private static final long serialVersionUID = 6996325139696021882L;

                            /**
                             * @see com.axiell.ehub.AbstractBreadCrumbBarPanel#getActivePanel(java.lang.String,
                             * org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel)
                             */
                            @Override
                            ContentProvidersPanel getActivePanel(final String activePanelId, final IBreadCrumbModel breadCrumbModel) {
                                return new ContentProvidersPanel(activePanelId, breadCrumbModel);
                            };
                        };
                    case LANGUAGES:
                        return new AbstractBreadCrumbBarPanel<LanguagesPanel>(panelId) {
                            private static final long serialVersionUID = -7786529779062948810L;

                            /**
                             * @see com.axiell.ehub.AbstractBreadCrumbBarPanel#getActivePanel(java.lang.String, org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel)
                             */
                            @Override
                            LanguagesPanel getActivePanel(final String activePanelId, final IBreadCrumbModel breadCrumbModel) {
                                return new LanguagesPanel(activePanelId, breadCrumbModel);
                            }
                        };
                    default:
                        throw new IllegalArgumentException("Unknown tab identifier '" + identifier + "'");
                }
            }

            /**
             * @see com.axiell.ehub.AbstractTabbedPanelBuilder#getIdentifiers()
             */
            @Override
            protected List<Tab> getIdentifiers() {
                return Arrays.asList(Tab.values());
            }

            /**
             * @see com.axiell.ehub.AbstractTabbedPanelBuilder#getTitle(java.lang.Object)
             */
            @Override
            protected String getTitle(final Tab identifier) {
                final String titleKey;
                
                switch (identifier) {
                    case HOME:
                    case EHUB_CONSUMERS:
                    case CONTENT_PROVIDERS:
                    case LANGUAGES:
                        titleKey = identifier.toString();
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown tab identifier '" + identifier + "'");
                }
                
                final StringResourceModel titleModel = new StringResourceModel(titleKey, EhubAdminPage.this, new Model<>());
                return titleModel.getString();
            }
        };
        
        final TabbedPanel tabbedPanel = builder.getTabbedPanel();
        add(tabbedPanel);
    }

    /**
     * Represents a tab in the tabbed panel.
     */
    private static enum Tab {
        HOME, EHUB_CONSUMERS, CONTENT_PROVIDERS, LANGUAGES;
    }
}
