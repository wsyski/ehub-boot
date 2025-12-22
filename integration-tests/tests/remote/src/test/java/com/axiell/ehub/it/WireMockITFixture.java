package com.axiell.ehub.it;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.test.TestDataConstants;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ServeEventListener;
import com.github.tomakehurst.wiremock.http.LoggedResponse;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@Slf4j
public abstract class WireMockITFixture {
    private static final int STUB_PORT_NO = 16520;
    private static final String LF = System.getProperty("line.separator");

    @RegisterExtension
    private static final WireMockExtension wireMock = WireMockExtension.newInstance().options(wireMockConfig()
                    .port(STUB_PORT_NO)
                    // .notifier(new ConsoleNotifier(true))
                    .extensions(new ServeEventListener() {
                        @Override
                        public String getName() {
                            return "wireMockListener";
                        }

                        @Override
                        public void afterComplete(ServeEvent serveEvent, Parameters parameters) {
                            logRequests(serveEvent.getRequest(), serveEvent.getResponse());
                            validateRequests(serveEvent.getRequest(), serveEvent.getResponse());
                        }
                    }))
            .build();

    private static void logRequests(final Request request, final LoggedResponse response) {
        RequestMethod requestMethod = request.getMethod();
        String requestBodyAsString = requestMethod.equals(RequestMethod.POST) || requestMethod.equals(RequestMethod.PUT) ? LF + request.getBodyAsString() : StringUtils.EMPTY;
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String authorizationHeaderAsString = authorizationHeader == null ? StringUtils.EMPTY : LF + HttpHeaders.AUTHORIZATION + ": " + authorizationHeader;
        log.info("wireMock: " + request.getMethod() + " " + request.getAbsoluteUrl() + authorizationHeaderAsString + requestBodyAsString + LF + response.getBodyAsString() + LF + response.getStatus());
    }

    private static void validateRequests(final Request request, final LoggedResponse response) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (request.getUrl().startsWith("/ep/api/v5.0/")) {
            Assertions.assertNotNull(HttpHeaders.AUTHORIZATION + " header can not be null", authorizationHeader);
        }
    }

    protected void givenLmsCheckoutTestActiveLoanResponse(final boolean isLoanPerProduct, final String lmsRecordId, final String format) {
        String url = "/local-rest/api/v5.0/patrons/emedia/records/" + lmsRecordId + "/providers/Distribut%C3%B6r:%20TEST_EP/formats/" + format + "/checkouts/test?isLoanPerProduct=" + isLoanPerProduct + "&origin=E_HUB";
        wireMock.stubFor(get(urlEqualTo(url))
                .willReturn(aResponse().withBodyFile("lms/CheckOutTestResponse_activeLoan.json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    protected void givenLmsCheckoutTestNewLoanResponse(final boolean isLoanPerProduct, final String lmsRecordId, final String format) {
        String url = "/local-rest/api/v5.0/patrons/emedia/records/" + lmsRecordId + "/providers/Distribut%C3%B6r:%20TEST_EP/formats/" + format + "/checkouts/test?isLoanPerProduct=" + isLoanPerProduct + "&origin=E_HUB";
        wireMock.stubFor(get(urlEqualTo(url))
                .willReturn(aResponse().withBodyFile("lms/CheckOutTestResponse_newLoan.json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    protected void givenLmsCheckoutTestErrorResponse(final boolean isLoanPerProduct, final String lmsRecordId, final String format, final String errorCause) {
        String url = "/local-rest/api/v5.0/patrons/emedia/records/" + lmsRecordId + "/providers/Distribut%C3%B6r:%20TEST_EP/formats/" + format + "/checkouts/test?isLoanPerProduct=" + isLoanPerProduct + "&origin=E_HUB";
        wireMock.stubFor(get(urlEqualTo(url))
                .willReturn(aResponse().withBodyFile("lms/error/error_" + errorCause + ".json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_BAD_REQUEST)));
    }

    protected void givenLmsCheckoutResponse(final String lmsRecordId, final String format) {
        String url = "/local-rest/api/v5.0/patrons/emedia/records/" + lmsRecordId + "/providers/Distribut%C3%B6r:%20TEST_EP/formats/" + format + "/checkouts?origin=E_HUB";
        wireMock.stubFor(post(urlEqualTo(url))
                .willReturn(aResponse().withBodyFile("lms/CheckOutResponse.json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    protected void givenContentProviderCheckoutErrorResponse(final ErrorCauseArgumentType errorCauseArgumentType) {
        wireMock.stubFor(post(urlEqualTo("/ep/api/v5.0/checkouts")).willReturn(
                aResponse().withBodyFile(getContentProviderName() + "/error/error_" + errorCauseArgumentType.name() + ".json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)));
    }

    protected void givenContentProviderCheckoutResponse() {
        wireMock.stubFor(post(urlEqualTo("/ep/api/v5.0/checkouts")).willReturn(
                aResponse().withBodyFile(getResponseFilePrefix() + "checkoutResponse_newLoan.json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_CREATED)));
    }

    protected void givenContentProviderGetCheckoutResponse() {
        wireMock.stubFor(get(urlEqualTo("/ep/api/v5.0/checkouts/" + TestDataConstants.CONTENT_PROVIDER_LOAN_ID)).willReturn(
                aResponse().withBodyFile(getResponseFilePrefix() + "checkoutResponse_activeLoan.json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    protected void givenContentProviderGetRecordResponse() {
        wireMock.stubFor(get(urlEqualTo("/ep/api/v5.0/records/" + TestDataConstants.RECORD_ID_0))
                .willReturn(aResponse().withBodyFile(getContentProviderName() + "/getRecordResponse.json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    protected void resetWireMock() {
        wireMock.resetAll();
    }

    protected abstract boolean isLoanPerProduct();

    protected abstract String getContentProviderName();

    private String getResponseFilePrefix() {
        return getContentProviderName() + (isLoanPerProduct() ? "/lpp/" : "/lpf/");
    }
}
