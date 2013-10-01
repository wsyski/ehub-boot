package com.axiell.ehub.logging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StringBufferOutputStreamTest {
    private StringBufferOutputStream stream = new StringBufferOutputStream();

    @Test
    public void writeAndRead() throws IOException {

        whenStreamIsWrittenTo();
        thenStramContainsCorrectData();

        whenStreamIsCleared();
        thenStreamIsEmpty();
    }

    private void thenStreamIsEmpty() {
        assertEquals("", stream.toString());
    }

    private void whenStreamIsCleared() {
        stream.clear();
    }

    private void thenStramContainsCorrectData() {
        assertEquals("JAVA > CHAI", stream.toString());
    }

    private void whenStreamIsWrittenTo() throws IOException {
        stream.write(74);
        stream.write(65);
        stream.write(86);
        stream.write(65);
        stream.write(32);
        stream.write(62);
        stream.write(32);
        stream.write(67);
        stream.write(72);
        stream.write(65);
        stream.write(73);
    }
}
