package com.axiell.ehub.util.strings;

import java.io.IOException;
import java.io.OutputStream;

class StringBufferOutputStream extends OutputStream {
        private StringBuffer textBuffer = new StringBuffer();

        public StringBufferOutputStream() {
            super();
        }

        /*
         * @see java.io.OutputStream#write(int)
         */
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