package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import org.apache.wicket.model.IModel;

/**
 * The {@link IModel} for the description of an {@link ContentProvider}.
 */
class ContentProviderIsLoanPerProductModel implements IModel<Boolean> {
    private final IModel<ContentProvider> formModel;

    ContentProviderIsLoanPerProductModel(final IModel<ContentProvider> formModel) {
        this.formModel = formModel;
    }

    @Override
    public Boolean getObject() {
        final ContentProvider contentProvider = formModel.getObject();
        return contentProvider != null && contentProvider.isLoanPerProduct();
    }

    @Override
    public void setObject(final Boolean isLoanPerProduct) {
        final ContentProvider contentProvider = formModel.getObject();
        contentProvider.setLoanPerProduct(isLoanPerProduct);
    }

    @Override
    public void detach() {
        formModel.detach();
    }
}
