package com.axiell.ehub.support.request;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.util.EhubUrlCodec;
import com.axiell.ehub.util.XjcSupport;
import org.jboss.resteasy.client.*;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

class SupportRequestExecutor {
    private static final String STATUS_OK = "200";
    private static final String STATUS_NOT_AVAILABLE = "N/A";

    private SupportRequestExecutor() {
    }

    static SupportResponse getFormats(final RequestArguments arguments) {
        final String baseUri = arguments.getBaseUri();
        final String contentProviderName = arguments.getContentProviderName();
        final String contentProviderRecordId = arguments.getContentProviderRecordId();
        final String language = arguments.getLanguage();
        final SupportRequest supportRequest = new SupportRequest();
        try {
            AuthInfo authInfo = makeAuthInfo(arguments);
            supportRequest.setAuthInfo(authInfo);
            final ClientExecutor clientExecutor = new SupportClientExecutor(supportRequest);
            final IContentProvidersResource contentProvidersResource = ProxyFactory.create(IContentProvidersResource.class, baseUri, clientExecutor);
//            final IRecordsResource recordsResource = contentProvidersResource.getRecords(contentProviderName);
            final IRecordsResource recordsResource = contentProvidersResource.getRecords(EhubUrlCodec.encode(contentProviderName));
            final Formats formats = recordsResource.getFormats(authInfo, contentProviderRecordId, language);
            return makeSupportResponse(supportRequest, STATUS_OK, formats);
        } catch (ClientResponseFailure crf) {
            return handleClientResponseFailure(supportRequest, crf);
        } catch (EhubException e) {
            final EhubError ehubError = e.getEhubError();
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, ehubError);
        }
    }

    private static AuthInfo makeAuthInfo(final RequestArguments arguments) throws EhubException {
        final EhubConsumer ehubConsumer = arguments.getEhubConsumer();
        final String libraryCard = arguments.getLibraryCard();
        final String pin = arguments.getPin();
        return new AuthInfo.Builder(ehubConsumer.getId(), ehubConsumer.getSecretKey()).libraryCard(libraryCard).pin(pin).build();
    }

    private static SupportResponse makeSupportResponse(final SupportRequest supportRequest, final String status, final Object dto) {
        final String body = XjcSupport.marshal(dto);
        return new SupportResponse(supportRequest, status, body);
    }

    private static SupportResponse handleClientResponseFailure(final SupportRequest supportRequest, final ClientResponseFailure crf) {
        final ClientResponse<?> response = crf.getResponse();
        final String body = response.getEntity(String.class);
        final String status = String.valueOf(response.getStatus());
        return new SupportResponse(supportRequest, status, body);
    }

    private static class SupportClientExecutor extends ApacheHttpClient4Executor {
        private final SupportRequest supportRequest;

        private SupportClientExecutor(final SupportRequest supportRequest) {
            this.supportRequest = supportRequest;
        }

        @Override
        public ClientResponse execute(final ClientRequest request) throws Exception {
            final String httpMethod = request.getHttpMethod();
            final String uri = request.getUri();
            supportRequest.setHttpMethod(httpMethod);
            supportRequest.setUri(uri);
            return super.execute(request);
        }
    }
}
