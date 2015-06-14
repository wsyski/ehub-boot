package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.util.IMatcher;

class LibraryCardPatronMatcher implements IMatcher<PatronDTO> {
    private final String libraryCard;

    LibraryCardPatronMatcher(String libraryCard) {
        this.libraryCard = libraryCard;
    }

    @Override
    public boolean matches(PatronDTO patronDTO) {
        final String isbn = patronDTO.getLibraryCardNumber();
        return libraryCard.equals(isbn);
    }
}
