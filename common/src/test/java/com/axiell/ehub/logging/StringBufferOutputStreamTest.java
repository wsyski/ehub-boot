package com.axiell.ehub.logging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.axiell.ehub.logging.StringBufferOutputStream;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StringBufferOutputStreamTest {

    @Test
    public void writeAndRead() throws IOException {
        StringBufferOutputStream stream = new StringBufferOutputStream();
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
        assertEquals("JAVA > CHAI", stream.toString());
        stream.clear();
        assertEquals("", stream.toString());
    }
}
