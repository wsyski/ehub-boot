package com.axiell.ehub.logging;

import java.io.IOException;
import java.io.OutputStream;

class StringBuilderOutputStream extends OutputStream {
    private StringBuilder stringBuilder = new StringBuilder();

    public StringBuilderOutputStream() {
        super();
    }

    @Override
    public void write(int b) throws IOException {
        char a = (char) b;
        stringBuilder.append(a);
    }

    public String toString() {
        return stringBuilder.toString();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void clear() {
        stringBuilder.delete(0, stringBuilder.length());
    }
}