package com.axiell.ehub.logging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StringBuilderOutputStreamTest {
    private StringBuilderOutputStream underTest = new StringBuilderOutputStream();

    @Test
    public void writeAndRead() throws IOException {

        whenStreamIsWrittenTo();
        thenStramContainsCorrectData();

        whenStreamIsCleared();
        thenStreamIsEmpty();
    }

    private void thenStreamIsEmpty() {
        assertEquals("", underTest.toString());
    }

    private void whenStreamIsCleared() {
        underTest.clear();
    }

    private void thenStramContainsCorrectData() {
        assertEquals("JAVA > CHAI", underTest.toString());
    }

    private void whenStreamIsWrittenTo() throws IOException {
        underTest.write(74);
        underTest.write(65);
        underTest.write(86);
        underTest.write(65);
        underTest.write(32);
        underTest.write(62);
        underTest.write(32);
        underTest.write(67);
        underTest.write(72);
        underTest.write(65);
        underTest.write(73);
    }
}
