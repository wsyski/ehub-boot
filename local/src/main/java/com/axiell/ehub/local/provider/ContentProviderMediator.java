package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.provider.record.format.FormatDecorationCreateFormPanel;
import com.axiell.ehub.local.provider.record.format.FormatDecorationCreateLink;
import com.axiell.ehub.local.provider.record.format.FormatDecorationPanelFactory;
import com.axiell.ehub.local.provider.record.format.IContentDispositionChangedAwareMediator;
import com.axiell.ehub.local.provider.record.format.PlayerContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

import java.io.Serializable;
import java.util.Optional;

public class ContentProviderMediator implements Serializable, IContentDispositionChangedAwareMediator {
    private ContentProviderPanel contentProviderPanel;
    private FormatDecorationCreateFormPanel formatDecorationCreateFormPanel;
    private FormatDecorationCreateLink formatDecorationCreateLink;
    private PlayerContainer playerContainer;

    void registerContentProviderPanel(final ContentProviderPanel contentProviderPanel) {
        this.contentProviderPanel = contentProviderPanel;
    }

    void registerFormatDecorationCreateFormPanel(final FormatDecorationCreateFormPanel formatDecorationCreateFormPanel) {
        this.formatDecorationCreateFormPanel = formatDecorationCreateFormPanel;
    }

    void registerFormatDecorationCreateLink(final FormatDecorationCreateLink formatDecorationCreateLink) {
        this.formatDecorationCreateLink = formatDecorationCreateLink;
    }

    @Override
    public void registerPlayerContainer(PlayerContainer playerContainer) {
        this.playerContainer = playerContainer;
    }

    void afterEditContentProvider() {
        contentProviderPanel.activate(contentProviderPanel);
    }

    public void afterDeleteFormatDecoration() {
        contentProviderPanel.activate(contentProviderPanel);
    }

    public void afterClickOnFormatDecorationCreateLink(final Optional<AjaxRequestTarget> targetOptional) {
        formatDecorationCreateFormPanel.setVisible(true);
        targetOptional.ifPresent(
                target -> target.add(formatDecorationCreateFormPanel)
        );
    }

    public void afterNewFormatDecoration(final FormatDecoration formatDecoration) {
        final IBreadCrumbPanelFactory factory = new FormatDecorationPanelFactory(formatDecoration);
        contentProviderPanel.activate(factory);
    }

    public void afterCancelNewFormatDecoration(final Optional<AjaxRequestTarget> targetOptional) {
        formatDecorationCreateFormPanel.setVisible(false);
        formatDecorationCreateLink.setVisible(true);

        targetOptional.ifPresent(
                target -> {
                    target.add(formatDecorationCreateFormPanel);
                    target.add(formatDecorationCreateLink);
                }
        );
    }

    @Override
    public void afterContentDispositionChanged(AjaxRequestTarget target) {
        if (target != null) {
            target.add(playerContainer);
        }
    }
}
