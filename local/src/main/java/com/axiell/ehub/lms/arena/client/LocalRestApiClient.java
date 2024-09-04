package com.axiell.ehub.lms.arena.client;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.lms.arena.error.ErrorCause;
import com.axiell.ehub.lms.arena.exception.CheckedArenaException;
import com.axiell.ehub.lms.arena.exception.RestApiException;
import com.axiell.ehub.lms.arena.resources.IRootResource;
import com.axiell.ehub.lms.arena.resources.patrons.IPatronEmediaResource;
import com.axiell.ehub.lms.arena.resources.patrons.dto.CheckoutEmediaRequestDTO;
import com.axiell.ehub.lms.arena.resources.patrons.dto.CheckoutEmediaResponseDTO;
import com.axiell.ehub.lms.arena.resources.patrons.dto.CheckoutTestEmediaResponseDTO;
import com.axiell.ehub.lms.arena.resources.portalsites.IPortalSitesResource;
import com.axiell.ehub.lms.arena.resources.portalsites.dto.DecorationAdvice;
import com.axiell.ehub.lms.arena.resources.portalsites.dto.OpenEntityFieldCollection;
import com.axiell.ehub.lms.arena.resources.portalsites.dto.RecordsDTO;
import com.axiell.ehub.lms.arena.resources.portalsites.dto.SortAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class LocalRestApiClient implements ILocalRestApiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalRestApiClient.class);
    private static final String ORIGIN_E_HUB = "E_HUB";

    private IRootResourceFactory rootResourceFactory;

    @Override
    public CheckoutTestEmediaResponseDTO checkoutEmediaTest(final String localRestApiEndpoint, final Locale locale, final AuthInfo authInfo, final String recordId, final String contentProviderName, final String formatId, String issue, Boolean isLoanPerProduct) throws RestApiException {
        IPatronEmediaResource patronEmediaResource = getRootResource(localRestApiEndpoint, locale).getPatronsResource().getEmediaResource();
        try {
            return patronEmediaResource.checkoutEmediaTest(authInfo, recordId, contentProviderName, formatId, issue, isLoanPerProduct, ORIGIN_E_HUB);
        } catch (CheckedArenaException ex) {
            throw toRestApiException(ex);
        }
    }

    @Override
    public CheckoutEmediaResponseDTO checkoutEmedia(final String localRestApiEndpoint, final Locale locale, final AuthInfo authInfo, final String recordId, final String contentProviderName, final String formatId, final CheckoutEmediaRequestDTO checkoutEmediaRequestDTO) throws RestApiException {
        IPatronEmediaResource patronEmediaResource = getRootResource(localRestApiEndpoint, locale).getPatronsResource().getEmediaResource();
        try {
            return patronEmediaResource.checkoutEmedia(authInfo, recordId, contentProviderName, formatId, ORIGIN_E_HUB, checkoutEmediaRequestDTO);
        } catch (CheckedArenaException ex) {
            throw toRestApiException(ex);
        }
    }

    @Override
    public RecordsDTO search(final String localRestApiEndpoint, final Locale locale, final long arenaMemberId, final String query, final int start, final int count, final Set<OpenEntityFieldCollection> fieldCollections, final SortAdvice.Direction direction, final String sortField, final String patronId, final Set<DecorationAdvice.Type> decorationAdviceTypes) {
        IPortalSitesResource portalSitesResource = getRootResource(localRestApiEndpoint, locale).getPortalSitesResource();
        return portalSitesResource.search(arenaMemberId, query, start, count, fieldCollections, direction, sortField, patronId, decorationAdviceTypes);
    }

    private RestApiException toRestApiException(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return new RestApiException(ErrorCause.BAD_REQUEST.toError(Collections.singletonMap("throwableClassName", ex.getClass().getName())));
    }

    private IRootResource getRootResource(final String localRestApiEndpoint, final Locale locale) {
        return rootResourceFactory.createRootResource(localRestApiEndpoint, locale);
    }

    @Required
    public void setRootResourceFactory(IRootResourceFactory rootResourceFactory) {
        this.rootResourceFactory = rootResourceFactory;
    }
}
