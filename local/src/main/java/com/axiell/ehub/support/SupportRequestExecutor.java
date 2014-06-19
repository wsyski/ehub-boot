package com.axiell.ehub.support;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.util.XjcSupport;
import org.jboss.resteasy.client.*;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

class SupportRequestExecutor {
    private static final String STATUS_OK = "200";

    SupportResponse getFormats(final RequestParameters regParams) {
        final EhubConsumer ehubConsumer = regParams.getEhubConsumer();
        final String baseUri = regParams.getBaseUri();
        final String libraryCard = regParams.getLibraryCard();
        final String pin = regParams.getPin();
        final String contentProviderName = regParams.getContentProviderName();
        final String contentProviderRecordId = regParams.getContentProviderRecordId();
        final String language = regParams.getLanguage();
        try {
            AuthInfo authInfo = new AuthInfo.Builder(ehubConsumer.getId(), ehubConsumer.getSecretKey()).libraryCard(libraryCard).pin(pin).build();
            final SupportRequest supportRequest = new SupportRequest();
            supportRequest.setAuthInfo(authInfo);
            try {
                final ClientExecutor clientExecutor = new SupportClientExecutor(supportRequest);
                final IContentProvidersResource contentProvidersResource = ProxyFactory.create(IContentProvidersResource.class, baseUri, clientExecutor);
                IRecordsResource recordsResource = contentProvidersResource.getRecords(contentProviderName);
                Formats formats = recordsResource.getFormats(authInfo, contentProviderRecordId, language);
                String body = XjcSupport.marshal(formats);
                return new SupportResponse(supportRequest, STATUS_OK, body);
            } catch (ClientResponseFailure crf) {
                return handleClientResponseFailure(supportRequest, crf);
            }
        } catch (EhubException e) {
            EhubError ehubError = e.getEhubError();
        }

        return null;
    }

    private SupportResponse handleClientResponseFailure(final SupportRequest supportRequest, final ClientResponseFailure crf) {
        ClientResponse<?> response = crf.getResponse();
        String body = response.getEntity(String.class);
        String status = String.valueOf(response.getStatus());
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
