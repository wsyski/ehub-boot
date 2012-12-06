/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.language.ILanguageAdminController;
import com.axiell.ehub.language.Language;

/**
 * 
 */
public final class FormatDecorationPanel extends BreadCrumbPanel {
    private static final long serialVersionUID = 707321430528902920L;
    private final FormatDecorationFormPanel decorationFormPanel;
    private FormatDecoration formatDecoration;

    private final StatelessForm<FormatDecoration> textsForm;

    @SpringBean(name = "formatAdminController")
    private IFormatAdminController formatAdminController;

    @SpringBean(name = "languageAdminController")
    private ILanguageAdminController languageAdminController;

    /**
     * @param panelId
     * @param breadCrumbModel
     * @param formatDecoration
     */
    public FormatDecorationPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final FormatDecoration formatDecoration) {
        super(panelId, breadCrumbModel);
        this.formatDecoration = formatDecoration;
        this.decorationFormPanel = new FormatDecorationFormPanel("decorationFormPanel", false) {
            private static final long serialVersionUID = 2576304583773628287L;

            /**
             * @see com.axiell.ehub.provider.record.format.FormatDecorationFormPanel#afterSave(com.axiell.ehub.provider.record.format.FormatDecoration)
             */
            @Override
            protected void afterSave(FormatDecoration formatDecoration) {
                FormatDecorationPanel.this.formatDecoration = formatDecoration;
            }            
        };
        add(decorationFormPanel);

        this.textsForm = createTextsForm();
        add(textsForm);
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant#getTitle()
     */
    @Override
    public String getTitle() {
        return formatDecoration.getContentProviderFormatId();
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel#onActivate(org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant)
     */
    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        formatDecoration = formatAdminController.getFormatDecoration(formatDecoration.getId());
        final IModel<FormatDecoration> textsFormModel = new Model<>(formatDecoration);        
        textsForm.setModel(textsFormModel);

        decorationFormPanel.setFormModelObject(formatDecoration);
        super.onActivate(previous);
    }

    /**
     * 
     * @return
     */
    private StatelessForm<FormatDecoration> createTextsForm() {
        final StatelessForm<FormatDecoration> textsForm = new StatelessForm<>("textsForm");
        final List<Language> languages = languageAdminController.getLanguages();        
        final ListView<Language> textsView = new ListView<Language>("texts", languages) {
            private static final long serialVersionUID = -6957318602613662217L;

            /**
             * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
             */
            @Override
            protected void populateItem(ListItem<Language> item) {
                final Language language = item.getModelObject();

                final IModel<String> languageModel = new Model<>(language.getId());
                final TextField<String> languageField = new TextField<>("language", languageModel);
                languageField.setEnabled(false);
                item.add(languageField);

                final IModel<String> nameModel = new NameModel(language);
                final TextField<String> nameField = new TextField<>("name", nameModel);
                item.add(nameField);

                final IModel<String> descriptionModel = new DescriptionModel(language);
                final TextField<String> descriptionField = new TextField<>("description", descriptionModel);
                item.add(descriptionField);

                final Link<Void> deleteLink = new Link<Void>("deleteLink") {
                    private static final long serialVersionUID = 4149539500080668524L;

                    /**
                     * @see org.apache.wicket.markup.html.link.Link#onClick()
                     */
                    @Override
                    public void onClick() {
                        final FormatDecoration formatDecoration = textsForm.getModelObject();
                        final Map<String, FormatTextBundle> textBundles = formatDecoration.getTextBundles();
                        final FormatTextBundle textBundle = textBundles.get(language.getId());

                        if (textBundle != null) {
                            formatAdminController.delete(textBundle);
                        }

                        activate(FormatDecorationPanel.this);
                    }
                };
                item.add(deleteLink);
            }
        };
        textsForm.add(textsView);

        final Button submitButton = new Button("submit") {
            private static final long serialVersionUID = -3017984355645366964L;

            /**
             * @see org.apache.wicket.markup.html.form.Button#onSubmit()
             */
            @Override
            public void onSubmit() {
                Map<String, FormatTextBundle> textBundles = formatDecoration.getTextBundles();

                for (FormatTextBundle textBundle : textBundles.values()) {
                    formatAdminController.save(textBundle);
                }

                formatAdminController.save(formatDecoration);
                activate(FormatDecorationPanel.this);
            }
        };
        textsForm.add(submitButton);

        return textsForm;
    }

    /**
     * 
     */
    private class NameModel extends AbstractTextModel {
        private static final long serialVersionUID = -6940028627691973989L;

        /**
         * 
         * @param language
         */
        private NameModel(Language language) {
            super(language);
        }

        /**
         * @see com.axiell.ehub.provider.record.format.FormatDecorationPanel.AbstractTextModel#getText(com.axiell.ehub.provider.record.format.FormatTextBundle)
         */
        @Override
        String getText(FormatTextBundle textBundle) {
            return textBundle.getName();
        }

        /**
         * @see com.axiell.ehub.provider.record.format.FormatDecorationPanel.AbstractTextModel#setText(com.axiell.ehub.provider.record.format.FormatTextBundle,
         * java.lang.String)
         */
        @Override
        void setText(FormatTextBundle textBundle, String text) {
            textBundle.setName(text);
        }
    }

    /**
     * 
     */
    private class DescriptionModel extends AbstractTextModel {
        private static final long serialVersionUID = 1680643347273491981L;

        /**
         * 
         * @param language
         */
        private DescriptionModel(Language language) {
            super(language);
        }

        /**
         * @see com.axiell.ehub.provider.record.format.FormatDecorationPanel.AbstractTextModel#getText(com.axiell.ehub.provider.record.format.FormatTextBundle)
         */
        @Override
        String getText(FormatTextBundle textBundle) {
            return textBundle.getDescription();
        }

        /**
         * @see com.axiell.ehub.provider.record.format.FormatDecorationPanel.AbstractTextModel#setText(com.axiell.ehub.provider.record.format.FormatTextBundle,
         * java.lang.String)
         */
        @Override
        void setText(FormatTextBundle textBundle, String text) {
            textBundle.setDescription(text);
        }
    }

    /**
     * 
     */
    private abstract class AbstractTextModel implements IModel<String> {
        private static final long serialVersionUID = -8187035309314440128L;
        private final String language;

        /**
         * 
         * @param language
         */
        protected AbstractTextModel(Language language) {
            this.language = language.getId();
        }

        /**
         * 
         * @param textBundle
         * @return
         */
        abstract String getText(FormatTextBundle textBundle);

        /**
         * 
         * @param textBundle
         * @param text
         */
        abstract void setText(FormatTextBundle textBundle, String text);

        /**
         * @see org.apache.wicket.model.IModel#getObject()
         */
        @Override
        public final String getObject() {
            final FormatDecoration formatDecoration = textsForm.getModelObject();
            final Map<String, FormatTextBundle> textBundles = formatDecoration.getTextBundles();
            final FormatTextBundle textBundle = textBundles == null ? null : textBundles.get(language);
            return textBundle == null ? null : getText(textBundle);
        }

        /**
         * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
         */
        @Override
        public final void setObject(String value) {
            final FormatDecoration formatDecoration = textsForm.getModelObject();
            Map<String, FormatTextBundle> textBundles = formatDecoration.getTextBundles();

            if (textBundles == null) {
                textBundles = new HashMap<>();
                formatDecoration.setTextBundles(textBundles);
            }

            FormatTextBundle textBundle = textBundles.get(language);

            if (textBundle == null) {
                // Temporarily add a default name and description. These will be overridden shortly.
                textBundle = new FormatTextBundle(formatDecoration, language, "name", "desc");
                textBundle = formatAdminController.save(textBundle);
                textBundles.put(language, textBundle);
            }

            setText(textBundle, value);
        }

        /**
         * @see org.apache.wicket.model.IDetachable#detach()
         */
        @Override
        public void detach() {
            final IModel<FormatDecoration> formModel = textsForm.getModel();
            formModel.detach();
        }
    }
}
