package com.axiell.ehub.support.request;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.ILoansResource;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.util.EhubUrlCodec;
import com.axiell.ehub.util.XjcSupport;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;
import org.springframework.beans.factory.annotation.Required;

public class SupportRequestAdminController implements ISupportRequestAdminController {
    private static final String STATUS_OK = "200";
    private static final String STATUS_NOT_AVAILABLE = "N/A";
    private DefaultHttpClient sslHttpClient;

    @Override
    public SupportResponse getFormats(final RequestArguments arguments) {
        final String baseUri = arguments.getBaseUri();
        final String contentProviderName = arguments.getContentProviderName();
        final String contentProviderRecordId = arguments.getContentProviderRecordId();
        final String language = arguments.getLanguage();
        final SupportRequest supportRequest = new SupportRequest();
        try {
            final AuthInfo authInfo = makeAuthInfo(arguments);
            supportRequest.setAuthInfo(authInfo);
            final IContentProvidersResource contentProvidersResource = makeContentProvidersResource(baseUri, supportRequest);
            final IRecordsResource recordsResource = contentProvidersResource.getRecords(EhubUrlCodec.encode(contentProviderName));
            final Formats formats = recordsResource.getFormats(authInfo, contentProviderRecordId, language);
            return makeSupportResponse(supportRequest, STATUS_OK, formats);
        } catch (ClientResponseFailure crf) {
            return makeSupportResponse(supportRequest, crf);
        } catch (EhubException e) {
            final EhubError ehubError = e.getEhubError();
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, ehubError);
        }
    }

    @Override
    public SupportResponse createLoan(final RequestArguments arguments) {
        final String baseUri = arguments.getBaseUri();
        final PendingLoan pendingLoan = makePendingLoan(arguments);
        final String language = arguments.getLanguage();
        final SupportRequest supportRequest = new SupportRequest();
        final String requestBody = XjcSupport.marshal(pendingLoan);
        supportRequest.setBody(requestBody);
        try {
            final AuthInfo authInfo = makeAuthInfo(arguments);
            supportRequest.setAuthInfo(authInfo);
            final ILoansResource loansResource = makeLoansResource(baseUri, supportRequest);
            loansResource.createLoan(authInfo, language, pendingLoan);
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, null);
        } catch (EhubException e) {
            final EhubError ehubError = e.getEhubError();
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, ehubError);
        } catch (RuntimeException e) {
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, null);
        }
    }

    @Override
    public SupportResponse getLoan(final RequestArguments arguments) {
        final String baseUri = arguments.getBaseUri();
        final String lmsLoanId = arguments.getLmsLoanId();
        final String language = arguments.getLanguage();
        final SupportRequest supportRequest = new SupportRequest();
        try {
            final AuthInfo authInfo = makeAuthInfo(arguments);
            supportRequest.setAuthInfo(authInfo);
            final ILoansResource loansResource = makeLoansResource(baseUri, supportRequest);
            loansResource.getLoan(authInfo, lmsLoanId, language);
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, null);
        } catch (EhubException e) {
            final EhubError ehubError = e.getEhubError();
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, ehubError);
        } catch (RuntimeException e) {
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, null);
        }
    }

    @Required
    public void setSslHttpClient(DefaultHttpClient sslHttpClient) {
        this.sslHttpClient = sslHttpClient;
    }

    private IContentProvidersResource makeContentProvidersResource(final String baseUri, final SupportRequest supportRequest) {
        final ClientExecutor clientExecutor = new SupportClientExecutor(sslHttpClient, supportRequest);
        return ProxyFactory.create(IContentProvidersResource.class, baseUri, clientExecutor);
    }

    private ILoansResource makeLoansResource(final String baseUri, final SupportRequest supportRequest) {
        final NonExecutableClientExecutor clientExecutor = new NonExecutableClientExecutor(supportRequest);
        return ProxyFactory.create(ILoansResource.class, baseUri, clientExecutor);
    }

    private PendingLoan makePendingLoan(final RequestArguments arguments) {
        final String lmsRecordId = arguments.getLmsRecordId();
        final String formatId = arguments.getFormatId();
        final String contentProviderName = arguments.getContentProviderName();
        final String contentProviderRecordId = arguments.getContentProviderRecordId();
        return new PendingLoan(lmsRecordId, contentProviderName, contentProviderRecordId, formatId);
    }

    private AuthInfo makeAuthInfo(final RequestArguments arguments) throws EhubException {
        final EhubConsumer ehubConsumer = arguments.getEhubConsumer();
        final String libraryCard = arguments.getLibraryCard();
        final String pin = arguments.getPin();
        return new AuthInfo.Builder(ehubConsumer.getId(), ehubConsumer.getSecretKey()).libraryCard(libraryCard).pin(pin).build();
    }

    private SupportResponse makeSupportResponse(final SupportRequest supportRequest, final String status, final Object dto) {
        final String body = dto == null ? null : XjcSupport.marshal(dto);
        return new SupportResponse(supportRequest, status, body);
    }

    private SupportResponse makeSupportResponse(final SupportRequest supportRequest, final ClientResponseFailure crf) {
        final ClientResponse<?> response = crf.getResponse();
        final String body = response == null ? null : response.getEntity(String.class);
        final String status = response == null ? null : String.valueOf(response.getStatus());
        return new SupportResponse(supportRequest, status, body);
    }
}
