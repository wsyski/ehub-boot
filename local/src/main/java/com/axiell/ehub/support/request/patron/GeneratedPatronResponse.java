package com.axiell.ehub.support.request.patron;

import com.axiell.auth.Patron;
import com.axiell.ehub.support.request.ISupportResponse;
import com.axiell.ehub.support.request.RequestArguments;

class GeneratedPatronResponse implements ISupportResponse {
    private final String patronId;

    GeneratedPatronResponse(RequestArguments arguments) {
        String libraryCard = arguments.getLibraryCard();
        Patron patron = new Patron.Builder().libraryCard(libraryCard).build();
        patronId = patron.getId();
    }

    String getPatronId() {
        return patronId;
    }
}
