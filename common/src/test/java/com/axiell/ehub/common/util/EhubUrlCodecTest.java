package com.axiell.ehub.common.util;


import com.axiell.ehub.common.util.EhubUrlCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EhubUrlCodecTest {

    private static final String URL = "http://testurl.com/face?hi=2+1&i*i=i2&a!!=10&~ ";

    @Test
    public void testEncode() throws Exception {
        final String encoded = EhubUrlCodec.authInfoEncode(URL);
        Assertions.assertEquals("http%3A%2F%2Ftesturl.com%2Fface%3Fhi%3D2%2B1%26i%2Ai%3Di2%26a%21%21%3D10%26~%20", encoded);
    }

    @Test
    public void testDecode() throws Exception {
        final String decoded = EhubUrlCodec.decode("http%3A%2F%2Ftesturl.com%2Fface%3Fhi%3D2%2B1%26i%2Ai%3Di2%26a%21%21%3D10%26~%20");
        Assertions.assertEquals(URL, decoded);
    }
}
