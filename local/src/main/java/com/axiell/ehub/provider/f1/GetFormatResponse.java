package com.axiell.ehub.provider.f1;

class GetFormatResponse extends AbstractF1Response{
    static final String NO_SUCH_FORMAT = "inget sådant mediaId";

    GetFormatResponse(final String value) {
        super(value);
    }

    boolean isValidFormat() {
        return !NO_SUCH_FORMAT.equals(value);
    }
}
