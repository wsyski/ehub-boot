package com.axiell.ehub.logging;

import java.io.IOException;
import java.io.OutputStream;

class StringBufferOutputStream extends OutputStream {
    private StringBuffer textBuffer = new StringBuffer();

    public StringBufferOutputStream() {
	super();
    }

    @Override
    public void write(int b) throws IOException {
	char a = (char) b;
	textBuffer.append(a);
    }

    public String toString() {
	return textBuffer.toString();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void clear() {
	textBuffer.delete(0, textBuffer.length());
    }
}