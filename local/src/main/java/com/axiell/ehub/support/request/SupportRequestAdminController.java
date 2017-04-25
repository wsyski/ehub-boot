package com.axiell.ehub.support.request;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.IRootResource;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.record.RecordDTO;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.security.EhubParamConverterProvider;
import com.axiell.ehub.util.EhubUrlCodec;
import com.axiell.ehub.util.RestClientProxyFactoryBean;
import com.axiell.ehub.v1.XjcSupport;
import com.axiell.ehub.v1.loan.ILoansResource_v1;
import com.axiell.ehub.v1.loan.PendingLoan_v1;
import com.axiell.ehub.v1.provider.IContentProvidersResource_v1;
import com.axiell.ehub.v1.provider.record.IRecordsResource_v1;
import com.axiell.ehub.v1.provider.record.format.Formats_v1;
import com.axiell.ehub.v2.IRootResource_v2;
import com.axiell.ehub.v2.provider.record.RecordDTO_v2;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class SupportRequestAdminController implements ISupportRequestAdminController {
    private static final String STATUS_OK = "200";
    private static final String STATUS_NOT_AVAILABLE = "N/A";

    private ClientHttpEngine httpEngine;
    private EhubParamConverterProvider ehubParamConverterProvider;

    @Override
    public DefaultSupportResponse getRecord(final RequestArguments arguments) {
        final String baseUri = arguments.getBaseUri();
        final String contentProviderName = arguments.getContentProviderName();
        final String contentProviderRecordId = arguments.getContentProviderRecordId();
        final String language = arguments.getLanguage();
        final SupportRequest supportRequest = new SupportRequest();
        try {
            final AuthInfo authInfo = makeAuthInfo(arguments);
            supportRequest.setAuthInfo(authInfo);
            IRootResource rootResource = createResource(IRootResource.class, baseUri);
            RecordDTO recordDTO = rootResource.contentProviders().records(contentProviderName).getRecord(authInfo, contentProviderRecordId, language);
            String body = toJson(recordDTO, RecordDTO.class);
            return new DefaultSupportResponse(supportRequest, STATUS_OK, body);
        } catch (WebApplicationException ex) {
            return makeSupportResponse(supportRequest, ex);
        } catch (EhubException ex) {
            final EhubError ehubError = ex.getEhubError();
            String body = toJson(ehubError, EhubError.class);
            return new DefaultSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, body);
        }
    }

    @Override
    public DefaultSupportResponse getRecord_v2(final RequestArguments arguments) {
        final String baseUri = arguments.getBaseUri();
        final String contentProviderName = arguments.getContentProviderName();
        final String contentProviderRecordId = arguments.getContentProviderRecordId();
        final String language = arguments.getLanguage();
        final SupportRequest supportRequest = new SupportRequest();
        try {
            final AuthInfo authInfo = makeAuthInfo(arguments);
            supportRequest.setAuthInfo(authInfo);
            IRootResource_v2 rootResource = createResource(IRootResource_v2.class, baseUri);
            RecordDTO_v2 recordDTO = rootResource.contentProviders().records(contentProviderName).getRecord(authInfo, contentProviderRecordId, language);
            String body = toJson(recordDTO, RecordDTO.class);
            return new DefaultSupportResponse(supportRequest, STATUS_OK, body);
        } catch (WebApplicationException ex) {
            return makeSupportResponse(supportRequest, ex);
        } catch (EhubException ex) {
            final EhubError ehubError = ex.getEhubError();
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
            final IContentProvidersResource_v1 contentProvidersResource = createResource(IContentProvidersResource_v1.class, baseUri);
            final IRecordsResource_v1 recordsResource = contentProvidersResource.getRecords(EhubUrlCodec.authInfoEncode(contentProviderName));
            final Formats_v1 formats = recordsResource.getFormats(authInfo, contentProviderRecordId, language);
            return makeSupportResponse(supportRequest, STATUS_OK, formats);
        } catch (EhubException ex) {
            final EhubError ehubError = ex.getEhubError();
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, ehubError);
        } catch (WebApplicationException ex) {
            return makeSupportResponse(supportRequest, ex);
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
            final ILoansResource_v1 loansResource = createResource(ILoansResource_v1.class, baseUri);
            loansResource.createLoan(authInfo, language, pendingLoan);
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, null);
        } catch (EhubException ex) {
            final EhubError ehubError = ex.getEhubError();
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, ehubError);
        } catch (WebApplicationException ex) {
            return makeSupportResponse(supportRequest, ex);
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
            final ILoansResource_v1 loansResource = createResource(ILoansResource_v1.class, baseUri);
            loansResource.getLoan(authInfo, lmsLoanId, language);
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, null);
        } catch (EhubException ex) {
            final EhubError ehubError = ex.getEhubError();
            return makeSupportResponse(supportRequest, STATUS_NOT_AVAILABLE, ehubError);
        } catch (WebApplicationException ex) {
            return makeSupportResponse(supportRequest, ex);
        }
    }

    private String toJson(Object dto, Class<?> dtoClass) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ResteasyJackson2Provider provider = new ResteasyJackson2Provider();
        try {
            provider.writeTo(dto, dtoClass, null, null, MediaType.APPLICATION_JSON_TYPE, null, baos);
            return new String(baos.toByteArray(), "UTF-8");
        } catch (IOException e) {
            return e.getMessage();
        }
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
        return new AuthInfo(null, ehubConsumer.getId(), new Patron.Builder().id(patronId).libraryCard(libraryCard).pin(pin).build());
    }

    private DefaultSupportResponse makeSupportResponse(final SupportRequest supportRequest, final String status, final Object dto) {
        final String body = dto == null ? null : XjcSupport.marshal(dto);
        return new DefaultSupportResponse(supportRequest, status, body);
    }

    private DefaultSupportResponse makeSupportResponse(final SupportRequest supportRequest, final WebApplicationException ex) {
        final Response response = ex.getResponse();
        final String body = response == null ? null : response.readEntity(String.class);
        final String status = response == null ? null : String.valueOf(response.getStatus());
        return new DefaultSupportResponse(supportRequest, status, body);
    }

    private <T> T createResource(final Class<T> clazz, String baseUri) {
        RestClientProxyFactoryBean<T> proxyFactoryBean = new RestClientProxyFactoryBean<>();
        proxyFactoryBean.setServiceInterface(clazz);
        proxyFactoryBean.setHttpEngine(httpEngine);
        try {
            proxyFactoryBean.setBaseUri(new URI(baseUri));
            proxyFactoryBean.setEhubParamConverterProvider(ehubParamConverterProvider);
            proxyFactoryBean.afterPropertiesSet();
            return clazz.cast(proxyFactoryBean.getObject());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Required
    public void setHttpEngine(final ClientHttpEngine httpEngine) {
        this.httpEngine = httpEngine;
    }

    @Required
    public void setEhubParamConverterProvider(final EhubParamConverterProvider ehubParamConverterProvider) {
        this.ehubParamConverterProvider = ehubParamConverterProvider;
    }
}
