package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ICommandResult;

import java.util.List;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.PRODUCT_INACTIVE;
import static com.axiell.ehub.provider.ContentProviderName.ELIB3;
import static com.axiell.ehub.provider.elib.library3.GetProductCommand.Result.ACTIVE_PRODUCT_RETRIEVED;

class GetProductCommand extends AbstractElib3Command<Elib3CommandData> {

    GetProductCommand(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
    }

    @Override
    public void run(final Elib3CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String language = data.getLanguage();
        final Product product = elibFacade.getProduct(contentProviderConsumer, contentProviderRecordId);

        if (product.isActive()) {
            final List<Product.AvailableFormat> availableFormats = product.getFormats();
            data.setAvailableFormats(availableFormats);
            forward(ACTIVE_PRODUCT_RETRIEVED, data);
        } else
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, PRODUCT_INACTIVE, language);
    }

    public static enum Result implements ICommandResult {
        ACTIVE_PRODUCT_RETRIEVED
    }
}
