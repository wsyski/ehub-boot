package com.axiell.ehub.it26;

import javax.servlet.http.HttpServletResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public abstract class PalmaITFixture {

    protected void givenPalmaLoansWsdl() {
        stubFor(get(urlEqualTo("/arena.pa.palma/loans?wsdl")).willReturn(aResponse()
                .withHeader("Content-Type", "text/xml")
                .withHeader("Connection", "close")
                .withBodyFile("loans.wsdl")));
    }

    protected void givenPalmaCatalogueWsdl() {
        stubFor(get(urlEqualTo("/arena.pa.palma/v267/catalogue?wsdl")).willReturn(aResponse()
                .withHeader("Content-Type", "text/xml")
                .withHeader("Connection", "close")
                .withBodyFile("catalogue.wsdl")));
    }

    protected void givenPalmaCheckoutTestActiveLoanResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutTestResponse_activeLoan.xml")
                        .withHeader("Content-Type", "text/xml")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    protected void givenPalmaCheckoutTestNewLoanResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutTestResponse_newLoan.xml")
                        .withHeader("Content-Type", "text/xml")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    protected void givenCheckoutTestErrorResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns")).willReturn(aResponse().withBodyFile(
                "CheckOutTestResponse_error.xml")
                .withHeader("Content-Type", "text/xml")
                .withHeader("Connection", "close")
                .withStatus(HttpServletResponse.SC_OK)));
    }

    protected void givenPalmaCheckoutResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOut xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutResponse.xml")
                        .withHeader("Content-Type", "text/xml")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }

    protected void givenPalmaSearchResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/v267/catalogue")).withRequestBody(containing(":Search xmlns"))
                .willReturn(aResponse().withBodyFile("SearchResultResponse.xml")
                        .withHeader("Content-Type", "text/xml")
                        .withHeader("Connection", "close")
                        .withStatus(HttpServletResponse.SC_OK)));
    }
}
