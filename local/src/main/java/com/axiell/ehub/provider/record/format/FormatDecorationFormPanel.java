/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;
import java.util.List;

/**
 * Skeletal implementation of a {@link Panel} containing a {@link StatelessForm} that provides the possibility to either
 * create a new {@link FormatDecoration} or o modify an existing {@link FormatDecoration}.
 */
public abstract class FormatDecorationFormPanel extends Panel {
    private static final long serialVersionUID = 8362085580448033511L;
    private final StatelessForm<FormatDecoration> decorationForm;
    private WebMarkupContainer playerContainer;

    private Link<?> onCancelVisibleLink;

    @SpringBean(name = "formatAdminController")
    private IFormatAdminController formatAdminController;

    /**
     * Constructs a new {@link FormatDecorationPanel}.
     * 
     * @param panelId the ID of this {@link Panel}
     * @param newFormatDecoration <code>true</code> if and only if a new {@link FormatDecoration} should be created,
     * <code>false</code> otherwise
     */
    public FormatDecorationFormPanel(final String panelId, final boolean newFormatDecoration) {
        super(panelId);
        setOutputMarkupPlaceholderTag(true);
        this.decorationForm = createFormatDecorationForm(newFormatDecoration);
        add(decorationForm);

        final IndicatingAjaxFallbackLink<Void> cancelLink = new IndicatingAjaxFallbackLink<Void>("cancelLink") {
            private static final long serialVersionUID = -7003824024593083262L;

            /**
             * @see org.apache.wicket.ajax.markup.html.AjaxFallbackLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
             */
            @Override
            public void onClick(AjaxRequestTarget target) {
                FormatDecorationFormPanel.this.setVisible(false);
                onCancelVisibleLink.setVisible(true);

                if (target != null) {
                    target.addComponent(FormatDecorationFormPanel.this);
                    target.addComponent(onCancelVisibleLink);
                }
            }

            /**
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return newFormatDecoration;
            }
        };
        add(cancelLink);
    }

    /**
     * Invoked after the provided {@link FormatDecoration} has been saved.
     * 
     * @param formatDecoration the saved {@link FormatDecoration}
     */
    protected abstract void afterSave(FormatDecoration formatDecoration);

    /**
     * Sets the {@link Link} that should be made visible when the end-user cancels the creation or modification of the
     * {@link FormatDecoration}.
     * 
     * @param link the {@link Link} to set
     */
    public void setOnCancelVisibleLink(Link<?> link) {
        this.onCancelVisibleLink = link;
    }

    /**
     * Sets the model object of the underlying {@link StatelessForm}.
     * 
     * @param formatDecoration the {@link FormatDecoration} to be wrapped in the {@link IModel} of the underlying
     * {@link StatelessForm}
     */
    public void setFormModelObject(FormatDecoration formatDecoration) {
        final IModel<FormatDecoration> formatDecorationFormModel = new CompoundPropertyModel<>(formatDecoration);
        decorationForm.setModel(formatDecorationFormModel);
    }

    /**
     * Creates a {@link StatelessForm} that provides the possibility to either create a new {@link FormatDecoration} or
     * o modify an existing {@link FormatDecoration}.
     * 
     * @param newFormatDecoration <code>true</code> if and only if a new {@link FormatDecoration} should be created,
     * <code>false</code> otherwise
     * @return a {@link StatelessForm}
     */
    private StatelessForm<FormatDecoration> createFormatDecorationForm(boolean newFormatDecoration) {
        final StatelessForm<FormatDecoration> form = new StatelessForm<>("decorationForm");

        final TextField<String> formatIdField = new TextField<>("contentProviderFormatId");
        formatIdField.setVisible(newFormatDecoration);
        formatIdField.setRequired(newFormatDecoration);
        form.add(formatIdField);

        final IModel<ContentDisposition> dispositionModel = new ContentDispositionModel();
        final ContentDispositionsModel dispositionsModel = new ContentDispositionsModel();
        final ContentDispositionChoiceRenderer dispositionChoiceRenderer = new ContentDispositionChoiceRenderer();
        final ContentDispositionChoiceOnChangeBehavior dispositionChoiceOnChangeBehavior = new ContentDispositionChoiceOnChangeBehavior();
        final DropDownChoice<ContentDisposition> dispositionChoice = new DropDownChoice<>("contentDisposition", dispositionModel, dispositionsModel,
                dispositionChoiceRenderer);
        dispositionChoice.add(dispositionChoiceOnChangeBehavior);
        form.add(dispositionChoice);

        playerContainer = new WebMarkupContainer("playerContainer") {
            private static final long serialVersionUID = 3447388479264130007L;

            /**
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                final FormatDecoration formatDecoration = decorationForm.getModelObject();
                final ContentDisposition contentDisposition = formatDecoration.getContentDisposition();
                return ContentDisposition.STREAMING.equals(contentDisposition);
            }
        };
        playerContainer.setOutputMarkupPlaceholderTag(true);

        final RequiredTextField<Integer> playerWidthField = new RequiredTextField<>("playerWidth");
        playerContainer.add(playerWidthField);

        final RequiredTextField<Integer> playerHeightField = new RequiredTextField<>("playerHeight");
        playerContainer.add(playerHeightField);

        form.add(playerContainer);

        final Button submitButton = new Button("submit") {
            private static final long serialVersionUID = 573189878150333446L;

            /**
             * @see org.apache.wicket.markup.html.form.Button#onSubmit()
             */
            @Override
            public void onSubmit() {
                final FormatDecoration formatDecoration = formatAdminController.save(decorationForm.getModelObject());
                afterSave(formatDecoration);
            }
        };
        form.add(submitButton);

        return form;
    }

    /**
     * The {@link IModel} of the {@link ContentDisposition}.
     */
    private class ContentDispositionModel implements IModel<ContentDisposition> {
        private static final long serialVersionUID = 3858364501120958978L;

        /**
         * @see org.apache.wicket.model.IModel#getObject()
         */
        @Override
        public ContentDisposition getObject() {
            final FormatDecoration formatDecoration = decorationForm.getModelObject();
            return formatDecoration.getContentDisposition();
        }

        /**
         * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
         */
        @Override
        public void setObject(ContentDisposition contentDisposition) {
            final FormatDecoration formatDecoration = decorationForm.getModelObject();
            formatDecoration.setContentDisposition(contentDisposition);
        }

        /**
         * @see org.apache.wicket.model.IDetachable#detach()
         */
        @Override
        public void detach() {
            final IModel<FormatDecoration> formModel = decorationForm.getModel();
            formModel.detach();
        }
    }

    /**
     * The {@link IModel} of the available {@link ContentDisposition}s.
     */
    private static class ContentDispositionsModel extends LoadableDetachableModel<List<ContentDisposition>> {
        private static final long serialVersionUID = -20079717419878398L;

        /**
         * @see org.apache.wicket.model.LoadableDetachableModel#load()
         */
        @Override
        protected List<ContentDisposition> load() {
            return Arrays.asList(ContentDisposition.values());
        }
    }

    /**
     * Provides the possibility to render a {@link ContentDisposition}.
     */
    private class ContentDispositionChoiceRenderer extends ChoiceRenderer<ContentDisposition> {
        private static final long serialVersionUID = 4326286870467144975L;

        /**
         * @see org.apache.wicket.markup.html.form.ChoiceRenderer#getIdValue(java.lang.Object, int)
         */
        @Override
        public String getIdValue(ContentDisposition object, int index) {
            return object.toString();
        }

        /**
         * @see org.apache.wicket.markup.html.form.ChoiceRenderer#getDisplayValue(java.lang.Object)
         */
        @Override
        public Object getDisplayValue(ContentDisposition object) {
            final StringResourceModel resourceModel = new StringResourceModel(object.toString(), FormatDecorationFormPanel.this, new Model<>());
            return resourceModel.getString();
        }
    }

    /**
     * This IBehavior re-renders the player container when the Component this IBehavior is added
     * to is changed.
     */
    private class ContentDispositionChoiceOnChangeBehavior extends AjaxFormComponentUpdatingBehavior {
        private static final long serialVersionUID = -8373896837872031191L;

        /**
         * Constructs a new {@link ContentDispositionChoiceOnChangeBehavior}.
         */
        private ContentDispositionChoiceOnChangeBehavior() {
            super("onchange");
        }

        /**
         * @see org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior#onUpdate(org.apache.wicket.ajax.AjaxRequestTarget)
         */
        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            if (target != null) {
                target.addComponent(playerContainer);
            }
        }
    }
}
