package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OCD_LIBRARY_ID;

@Component
class OcdFacade implements IOcdFacade {

    @Override
    public List<MediaDTO> getAllMedia(ContentProviderConsumer contentProviderConsumer) {
        final BasicToken basicToken = new BasicToken(contentProviderConsumer);
        final String libraryId = contentProviderConsumer.getProperty(OCD_LIBRARY_ID);
        final IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        return ocdResource.getAllMedia(basicToken, libraryId);
    }

    @Override
    public List<PatronDTO> getAllPatrons(final ContentProviderConsumer contentProviderConsumer) {
        final BasicToken basicToken = new BasicToken(contentProviderConsumer);
        final String libraryId = contentProviderConsumer.getProperty(OCD_LIBRARY_ID);
        final IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        return ocdResource.getAllPatrons(basicToken, libraryId);
    }

    @Override
    public String getOrCreatePatron(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        final BasicToken basicToken = new BasicToken(contentProviderConsumer);
        final String libraryId = contentProviderConsumer.getProperty(OCD_LIBRARY_ID);
        final IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        final PatronDTO queryPatronDTO = new PatronDTO(patron, libraryId);
        PatronDTO patronDTO;
        try {
            patronDTO = ocdResource.getPatronByLibraryCard(basicToken, libraryId, queryPatronDTO.getLibraryCardNumber());
        } catch (NotFoundException ex1) {
            try {
                patronDTO = ocdResource.getPatronByEmail(basicToken, libraryId, queryPatronDTO.getEmail());
            } catch (NotFoundException ex2) {
                patronDTO = ocdResource.addPatron(basicToken, libraryId, queryPatronDTO);
            }
        }
        return patronDTO.getPatronId();
    }

    @Override
    public CheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final String patronId, final String contentProviderRecordId) {
        final BasicToken basicToken = new BasicToken(contentProviderConsumer);
        final String libraryId = contentProviderConsumer.getProperty(OCD_LIBRARY_ID);
        final IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        final CheckoutSummaryDTO checkoutSummaryDTO = ocdResource.checkout(basicToken, libraryId, patronId, contentProviderRecordId);
        if (checkoutSummaryDTO.isSuccessful()) {
            return getCheckout(contentProviderConsumer, patronId, checkoutSummaryDTO.getTransactionId());
        } else {
            return null;
        }
    }

    @Override
    public void checkin(final ContentProviderConsumer contentProviderConsumer, final String patronId, final String contentProviderRecordId) {
        final BasicToken basicToken = new BasicToken(contentProviderConsumer);
        final String libraryId = contentProviderConsumer.getProperty(OCD_LIBRARY_ID);
        final IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        ocdResource.checkin(basicToken, libraryId, patronId, contentProviderRecordId);
    }

    @Override
    public List<CheckoutDTO> getCheckouts(final ContentProviderConsumer contentProviderConsumer, final String patronId) {
        final BasicToken basicToken = new BasicToken(contentProviderConsumer);
        final String libraryId = contentProviderConsumer.getProperty(OCD_LIBRARY_ID);
        final IOcdResource ocdResource = OcdResourceFactory.create(contentProviderConsumer);
        return ocdResource.getCheckouts(basicToken, libraryId, patronId);
    }

    @Override
    public CheckoutDTO getCheckout(final ContentProviderConsumer contentProviderConsumer, final String patronId, final String transactionId) {
        List<CheckoutDTO> checkoutDTOs = getCheckouts(contentProviderConsumer, patronId);
        Optional<CheckoutDTO> optionalCheckoutDTO =
                checkoutDTOs.stream().filter(checkoutDTO -> transactionId.equals(checkoutDTO.getTransactionId())).findFirst();
        return optionalCheckoutDTO.isPresent() ? optionalCheckoutDTO.get() : null;
    }
}
