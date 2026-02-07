package com.axiell.ehub.lms.arena.client;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.lms.arena.exception.RestApiException;
import com.axiell.ehub.lms.arena.controller.patrons.dto.CheckoutEmediaRequestDTO;
import com.axiell.ehub.lms.arena.controller.patrons.dto.CheckoutEmediaResponseDTO;
import com.axiell.ehub.lms.arena.controller.patrons.dto.CheckoutTestEmediaResponseDTO;
import com.axiell.ehub.lms.arena.controller.portalsites.dto.DecorationAdvice;
import com.axiell.ehub.lms.arena.controller.portalsites.dto.OpenEntityFieldCollection;
import com.axiell.ehub.lms.arena.controller.portalsites.dto.RecordsDTO;
import com.axiell.ehub.lms.arena.controller.portalsites.dto.SortAdvice;

import java.util.Locale;
import java.util.Set;

public interface IArenaLocalService {

    CheckoutTestEmediaResponseDTO checkoutEmediaTest(String localRestApiEndpoint, Locale locale, AuthInfo authInfo, String recordId, String contentProviderName, String formatId, String issue, Boolean isLoanPerProduct) throws RestApiException;

    CheckoutEmediaResponseDTO checkoutEmedia(String localRestApiEndpoint, Locale locale, AuthInfo authInfo, String recordId, String contentProviderName, String formatId, CheckoutEmediaRequestDTO checkoutEmediaRequestDTO) throws RestApiException;

    RecordsDTO search(
            String localRestApiEndpoint,
            Locale locale,
            long arenaMemberId, String query,
            int start,
            int count,
            Set<OpenEntityFieldCollection> fieldCollections,
            SortAdvice.Direction direction,
            String sortField,
            String patronId,
            Set<DecorationAdvice.Type> decorationAdviceTypes);
}
