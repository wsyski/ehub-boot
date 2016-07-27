package com.axiell.ehub.it24;

import javax.servlet.http.HttpServletResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public abstract class PalmaITFixture {

    protected void givenPalmaLoansWsdl() {
        stubFor(get(urlEqualTo("/arena.pa.palma/loans?wsdl")).willReturn(aResponse().withHeader("Content-Type", "text/xml").withBodyFile("loans.wsdl")));
    }

    protected void givenPalmaCatalogueWsdl() {
        stubFor(get(urlEqualTo("/arena.pa.palma/v267/catalogue?wsdl")).willReturn(aResponse().withHeader("Content-Type", "text/xml").withBodyFile("catalogue.wsdl")));
    }

    protected void givenPalmaCheckoutTestOkResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutTestResponse_ok.xml").withHeader("Content-Type", "text/xml").withStatus(HttpServletResponse.SC_OK)));
    }

    protected void givenCheckoutTestErrorResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOutTest xmlns")).willReturn(aResponse().withBodyFile(
                "CheckOutTestResponse_error.xml").withHeader("Content-Type", "text/xml").withStatus(HttpServletResponse.SC_OK)));
    }

    protected void givenPalmaCheckoutResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/loans")).withRequestBody(containing(":CheckOut xmlns"))
                .willReturn(aResponse().withBodyFile("CheckOutResponse.xml").withHeader("Content-Type", "text/xml").withStatus(HttpServletResponse.SC_OK)));
    }

    protected void givenPalmaSearchResponse() {
        stubFor(post(urlEqualTo("/arena.pa.palma/v267/catalogue")).withRequestBody(containing(":Search xmlns"))
                .willReturn(aResponse().withBodyFile("SearchResultResponse.xml").withHeader("Content-Type", "text/xml").withStatus(HttpServletResponse.SC_OK)));
    }
}
