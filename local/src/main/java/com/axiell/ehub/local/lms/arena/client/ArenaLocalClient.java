package com.axiell.ehub.local.lms.arena.client;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.local.lms.arena.controller.IArenaLocalRootResource;
import com.axiell.ehub.local.lms.arena.controller.patrons.IPatronEmediaResource;
import com.axiell.ehub.local.lms.arena.controller.patrons.dto.CheckoutEmediaRequestDTO;
import com.axiell.ehub.local.lms.arena.controller.patrons.dto.CheckoutEmediaResponseDTO;
import com.axiell.ehub.local.lms.arena.controller.patrons.dto.CheckoutTestEmediaResponseDTO;
import com.axiell.ehub.local.lms.arena.controller.portalsites.IPortalSitesResource;
import com.axiell.ehub.local.lms.arena.controller.portalsites.dto.DecorationAdvice;
import com.axiell.ehub.local.lms.arena.controller.portalsites.dto.OpenEntityFieldCollection;
import com.axiell.ehub.local.lms.arena.controller.portalsites.dto.RecordsDTO;
import com.axiell.ehub.local.lms.arena.controller.portalsites.dto.SortAdvice;
import com.axiell.ehub.local.lms.arena.error.ArenaLocalErrorCause;
import com.axiell.ehub.local.lms.arena.exception.CheckedArenaException;
import com.axiell.ehub.local.lms.arena.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

@Slf4j
@Component
public class ArenaLocalClient implements IArenaLocalService {
    private static final String ORIGIN_E_HUB = "E_HUB";

    @Autowired
    private IArenaLocalRootResourceFactory arenaLocalRootResourceFactory;

    @Override
    public CheckoutTestEmediaResponseDTO checkoutEmediaTest(final String localRestApiEndpoint, final Locale locale, final AuthInfo authInfo, final String recordId, final String contentProviderName, final String formatId, String issue, Boolean isLoanPerProduct) throws RestApiException {
        IPatronEmediaResource patronEmediaResource = getArenaRootResource(localRestApiEndpoint, locale).getPatronsResource().getEmediaResource();
        try {
            return patronEmediaResource.checkoutEmediaTest(authInfo, recordId, contentProviderName, formatId, issue, isLoanPerProduct, ORIGIN_E_HUB);
        } catch (CheckedArenaException ex) {
            throw toRestApiException(ex);
        }
    }

    @Override
    public CheckoutEmediaResponseDTO checkoutEmedia(final String localRestApiEndpoint, final Locale locale, final AuthInfo authInfo, final String recordId, final String contentProviderName, final String formatId, final CheckoutEmediaRequestDTO checkoutEmediaRequestDTO) throws RestApiException {
        IPatronEmediaResource patronEmediaResource = getArenaRootResource(localRestApiEndpoint, locale).getPatronsResource().getEmediaResource();
        try {
            return patronEmediaResource.checkoutEmedia(authInfo, recordId, contentProviderName, formatId, ORIGIN_E_HUB, checkoutEmediaRequestDTO);
        } catch (CheckedArenaException ex) {
            throw toRestApiException(ex);
        }
    }

    @Override
    public RecordsDTO search(final String localRestApiEndpoint, final Locale locale, final long arenaMemberId, final String query, final int start, final int count, final Set<OpenEntityFieldCollection> fieldCollections, final SortAdvice.Direction direction, final String sortField, final String patronId, final Set<DecorationAdvice.Type> decorationAdviceTypes) {
        IPortalSitesResource portalSitesResource = getArenaRootResource(localRestApiEndpoint, locale).getPortalSitesResource();
        return portalSitesResource.search(arenaMemberId, query, start, count, fieldCollections, direction, sortField, patronId, decorationAdviceTypes);
    }

    private RestApiException toRestApiException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new RestApiException(ArenaLocalErrorCause.BAD_REQUEST.toError(Collections.singletonMap("throwableClassName", ex.getClass().getName())));
    }

    private IArenaLocalRootResource getArenaRootResource(final String localRestApiEndpoint, final Locale locale) {
        return arenaLocalRootResourceFactory.create(localRestApiEndpoint, locale);
    }

}
