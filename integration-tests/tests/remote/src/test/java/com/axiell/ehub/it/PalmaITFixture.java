package com.axiell.ehub.it;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public abstract class PalmaITFixture {

    protected void givenPalmaLoansWsdl() {
        stubFor(get(urlEqualTo("/arena.pa.palma/loans?wsdl")).willReturn(aResponse().withHeader("Content-Type", "text/xml").withBodyFile("loans.wsdl")));
    }

    protected void givenPalmaCatalogueWsdl() {
        stubFor(get(urlEqualTo("/arena.pa.palma/v267/catalogue?wsdl")).willReturn(aResponse().withHeader("Content-Type", "text/xml").withBodyFile("catalogue.wsdl")));
    }

    protected void givenPalmaCheckoutTestActiveLoanResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutTestResponse_activeLoan.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }

    protected void givenPalmaCheckoutTestNewLoanResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutTestResponse_newLoan.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }

    protected void givenCheckoutTestErrorResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns")).willReturn(aResponse().withBodyFile(
                "CheckOutTestResponse_error.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }

    protected void givenPalmaCheckoutResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOut xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutResponse.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }

    protected void givenPalmaSearchResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/v267/catalogue")).withRequestBody(containing(":Search xmlns"))
                .willReturn(aResponse().withBodyFile("SearchResultResponse.xml").withHeader("Content-Type", "text/xml").withStatus(200)));
    }
}
