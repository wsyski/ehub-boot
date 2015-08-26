package com.axiell.ehub.support.request;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.IRootResource;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.provider.record.RecordDTO;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.util.EhubUrlCodec;
import com.axiell.ehub.util.XjcSupport;
import com.axiell.ehub.v1.loan.ILoansResource_v1;
import com.axiell.ehub.v1.loan.PendingLoan_v1;
import com.axiell.ehub.v1.provider.IContentProvidersResource_v1;
import com.axiell.ehub.v1.provider.record.IRecordsResource_v1;
import com.axiell.ehub.v1.provider.record.format.Formats_v1;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.spring.RestClientProxyFactoryBean;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class SupportRequestAdminController implements ISupportRequestAdminController {
    private static final String STATUS_OK = "200";
    private static final String STATUS_NOT_AVAILABLE = "N/A";
    private DefaultHttpClient sslHttpClient;

    @Override
    public DefaultSupportResponse getRecord(RequestArguments arguments) {
        final String baseUri = arguments.getBaseUri();
        final String contentProviderName = arguments.getContentProviderName();
        final String contentProviderRecordId = arguments.getContentProviderRecordId();
        final String language = arguments.getLanguage();
        final SupportRequest supportRequest = new SupportRequest();
        try {
            final AuthInfo authInfo = makeAuthInfo(arguments);
            supportRequest.setAuthInfo(authInfo);
            IRootResource rootResource = initRootResource(baseUri, supportRequest);
            RecordDTO recordDTO = rootResource.contentProviders().records(contentProviderName).getRecord(authInfo, contentProviderRecordId, language);
            String body = toJson(recordDTO, RecordDTO.class);
            return new DefaultSupportResponse(supportRequest, STATUS_OK, body);
        } catch (ClientResponseFailure crf) {
            return makeSupportResponse(supportRequest, crf);
        } catch (EhubException e) {
            final EhubError ehubError = e.getEhubError();
            String body = toJson(ehubError, EhubError.class);
            return new DefaultSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, body);
        }
    }

    @Override
    public DefaultSupportResponse getFormats(final RequestArguments arguments) {
        final String baseUri = arguments.getBaseUri();
        final String contentProviderName = arguments.getContentProviderName();
        final String contentProviderRecordId = arguments.getContentProviderRecordId();
        final String language = arguments.getLanguage();
        final SupportRequest supportRequest = new SupportRequest();
        try {
            final AuthInfo authInfo = makeAuthInfo(arguments);
            supportRequest.setAuthInfo(authInfo);
            final IContentProvidersResource_v1 contentProvidersResource = makeContentProvidersResource(baseUri, supportRequest);
            final IRecordsResource_v1 recordsResource = contentProvidersResource.getRecords(EhubUrlCodec.encode(contentProviderName));
            final Formats_v1 formats = recordsResource.getFormats(authInfo, contentProviderRecordId, language);
            return makeSupportResponse(supportRequest, STATUS_OK, formats);
        } catch (ClientResponseFailure crf) {
            return makeSupportResponse(supportRequest, crf);
        } catch (EhubException e) {
            final EhubError ehubError = e.getEhubError();
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, ehubError);
        }
    }

    @Override
    public DefaultSupportResponse createLoan(final RequestArguments arguments) {
        final String baseUri = arguments.getBaseUri();
        final PendingLoan_v1 pendingLoan = makePendingLoan(arguments);
        final String language = arguments.getLanguage();
        final SupportRequest supportRequest = new SupportRequest();
        final String requestBody = XjcSupport.marshal(pendingLoan);
        supportRequest.setBody(requestBody);
        try {
            final AuthInfo authInfo = makeAuthInfo(arguments);
            supportRequest.setAuthInfo(authInfo);
            final ILoansResource_v1 loansResource = makeLoansResource(baseUri, supportRequest);
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
    public DefaultSupportResponse getLoan(final RequestArguments arguments) {
        final String baseUri = arguments.getBaseUri();
        final String lmsLoanId = arguments.getLmsLoanId();
        final String language = arguments.getLanguage();
        final SupportRequest supportRequest = new SupportRequest();
        try {
            final AuthInfo authInfo = makeAuthInfo(arguments);
            supportRequest.setAuthInfo(authInfo);
            final ILoansResource_v1 loansResource = makeLoansResource(baseUri, supportRequest);
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

    private IRootResource initRootResource(String baseUri, SupportRequest supportRequest) {
        final SupportClientExecutor clientExecutor = new SupportClientExecutor(sslHttpClient, supportRequest);
        RestClientProxyFactoryBean<IRootResource> proxyFactoryBean = new RestClientProxyFactoryBean();
        proxyFactoryBean.setServiceInterface(IRootResource.class);
        proxyFactoryBean.setClientExecutor(clientExecutor);
        try {
            proxyFactoryBean.setBaseUri(new URI(baseUri + "/v2"));
            proxyFactoryBean.afterPropertiesSet();
            return proxyFactoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize the root resource");
        }
    }

    private String toJson(Object dto, Class<?> dtoClass) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ResteasyJacksonProvider provider = new ResteasyJacksonProvider();
        try {
            provider.writeTo(dto, dtoClass, null, null, MediaType.APPLICATION_JSON_TYPE, null, baos);
            return new String(baos.toByteArray(), "UTF-8");
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private IContentProvidersResource_v1 makeContentProvidersResource(final String baseUri, final SupportRequest supportRequest) {
        final ClientExecutor clientExecutor = new SupportClientExecutor(sslHttpClient, supportRequest);
        return ProxyFactory.create(IContentProvidersResource_v1.class, baseUri, clientExecutor);
    }

    private ILoansResource_v1 makeLoansResource(final String baseUri, final SupportRequest supportRequest) {
        final NonExecutableClientExecutor clientExecutor = new NonExecutableClientExecutor(supportRequest);
        return ProxyFactory.create(ILoansResource_v1.class, baseUri, clientExecutor);
    }

    private PendingLoan_v1 makePendingLoan(final RequestArguments arguments) {
        final String lmsRecordId = arguments.getLmsRecordId();
        final String formatId = arguments.getFormatId();
        final String contentProviderName = arguments.getContentProviderName();
        final String contentProviderRecordId = arguments.getContentProviderRecordId();
        return new PendingLoan_v1(lmsRecordId, contentProviderName, contentProviderRecordId, formatId);
    }

    private AuthInfo makeAuthInfo(final RequestArguments arguments) throws EhubException {
        final EhubConsumer ehubConsumer = arguments.getEhubConsumer();
        final String patronId = arguments.getPatronId();
        final String libraryCard = arguments.getLibraryCard();
        final String pin = arguments.getPin();
        return new AuthInfo.Builder(ehubConsumer.getId(), ehubConsumer.getSecretKey()).patronId(patronId).libraryCard(libraryCard).pin(pin).build();
    }

    private DefaultSupportResponse makeSupportResponse(final SupportRequest supportRequest, final String status, final Object dto) {
        final String body = dto == null ? null : XjcSupport.marshal(dto);
        return new DefaultSupportResponse(supportRequest, status, body);
    }

    private DefaultSupportResponse makeSupportResponse(final SupportRequest supportRequest, final ClientResponseFailure crf) {
        final ClientResponse<?> response = crf.getResponse();
        final String body = response == null ? null : response.getEntity(String.class);
        final String status = response == null ? null : String.valueOf(response.getStatus());
        return new DefaultSupportResponse(supportRequest, status, body);
    }

}
