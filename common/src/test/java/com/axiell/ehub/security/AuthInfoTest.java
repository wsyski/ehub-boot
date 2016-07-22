package com.axiell.ehub.security;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.MessageFormat;

import static com.axiell.ehub.util.EhubUrlCodec.authInfoEncode;
import static com.axiell.ehub.util.SHA512Function.sha512Hex;
import static org.junit.Assert.*;

public class AuthInfoTest {
    private static final long EHUB_CONSUMER_ID = 254405L;
    private static final String EHUB_CONSUMER_SECRET_KEY = "kmmJoZ8n0b";
    private String PATRON_ID = "2D74D1E30BD80E7FE05400144FF9C26F";
    private String LIBRARY_CARD = "5007113623";
    private String PIN = "5046";
    private String EMAIL = "arena@axiell.com";
    private String SIGNATURE_0 = "Ev11cpKaRgtyq0YYzFrcPayXMTw=";
    private String SIGNATURE_1 = "ffIEXvDRISE10TjF9pH6bYs34vU=";
    private String SIGNATURE_2 = "7WkXKogjGxgWgJ1hlkWp1w5ATIs=";
    private String SIGNATURE_3 = "Vg3Evp1nHRPgcdHskqRnHFQEeqk=";
    private String expInfoValue1;
    private String expInfoValue2;
    private String expInfoValue3;
    private String expInfoValue4;

    @Before
    public void setUp() {
        expInfoValue1 = MessageFormat
                .format("eHUB ehub_consumer_id=\"{0}\", ehub_patron_id=\"{1}\", ehub_library_card=\"{2}\", ehub_pin=\"{3}\", ehub_signature=\"{4}\"",
                        authInfoEncode(String.valueOf(EHUB_CONSUMER_ID)), authInfoEncode(sha512Hex(LIBRARY_CARD)), authInfoEncode(LIBRARY_CARD), authInfoEncode(PIN), authInfoEncode(SIGNATURE_0));
        expInfoValue2 = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_signature=\"{1}\"",
                authInfoEncode(String.valueOf(EHUB_CONSUMER_ID)), authInfoEncode(SIGNATURE_1));
        expInfoValue3 = MessageFormat
                .format("eHUB ehub_consumer_id=\"{0}\", ehub_patron_id=\"{1}\", ehub_library_card=\"{2}\", ehub_pin=\"{3}\", ehub_signature=\"{4}\"",
                        authInfoEncode(String.valueOf(EHUB_CONSUMER_ID)), authInfoEncode(PATRON_ID), authInfoEncode(LIBRARY_CARD), authInfoEncode(PIN), authInfoEncode(SIGNATURE_2));
        expInfoValue4 = MessageFormat
                .format("eHUB ehub_consumer_id=\"{0}\", ehub_patron_id=\"{1}\", ehub_library_card=\"{2}\", ehub_pin=\"{3}\", ehub_email=\"{4}\", ehub_signature=\"{5}\"",
                        authInfoEncode(String.valueOf(EHUB_CONSUMER_ID)), authInfoEncode(PATRON_ID), authInfoEncode(LIBRARY_CARD), authInfoEncode(PIN), authInfoEncode(EMAIL), authInfoEncode(SIGNATURE_3));
    }

    /**
     * Test method for {@link com.axiell.ehub.security.AuthInfo#toString()}.
     *
     * @throws EhubException
     */
    @Test
    public void testToString() throws EhubException {
        AuthInfo expInfo1 = new AuthInfo.Builder(EHUB_CONSUMER_ID, EHUB_CONSUMER_SECRET_KEY).libraryCard(LIBRARY_CARD).pin(PIN).build();
        String actInfoValue1 = expInfo1.toString();
        Assert.assertEquals(expInfoValue1, actInfoValue1);

        AuthInfo expInfo2 = new AuthInfo.Builder(EHUB_CONSUMER_ID, EHUB_CONSUMER_SECRET_KEY).build();
        String actInfoValue2 = expInfo2.toString();
        Assert.assertEquals(expInfoValue2, actInfoValue2);

        AuthInfo expInfo3 =
                new AuthInfo.Builder(EHUB_CONSUMER_ID, EHUB_CONSUMER_SECRET_KEY).patronId(PATRON_ID).libraryCard(LIBRARY_CARD).pin(PIN).build();
        String actInfoValue3 = expInfo3.toString();
        Assert.assertEquals(expInfoValue3, actInfoValue3);

        AuthInfo expInfo4 =
                new AuthInfo.Builder(EHUB_CONSUMER_ID, EHUB_CONSUMER_SECRET_KEY).patronId(PATRON_ID).libraryCard(LIBRARY_CARD).pin(PIN).email(EMAIL).build();
        String actInfoValue4 = expInfo4.toString();
        Assert.assertEquals(expInfoValue4, actInfoValue4);
    }

    /**
     *
     */
    @Test
    public void testMissingEhubConsumerId() {
        try {
            new AuthInfo.Builder(null, null).build();
            fail("An EhubException should have been thrown");
        } catch (EhubException e) {
            assertNotNull(e);
            EhubError ehubError = e.getEhubError();
            assertNotNull(ehubError);
            ErrorCause actCause = ehubError.getCause();
            assertEquals(ErrorCause.MISSING_EHUB_CONSUMER_ID, actCause);
        }
    }

    /**
     *
     */
    @Test
    public void testMissingSecretKey() {
        try {
            new AuthInfo.Builder(Long.MAX_VALUE, null).build();
            fail("An EhubException should have been thrown");
        } catch (EhubException e) {
            assertNotNull(e);
            EhubError ehubError = e.getEhubError();
            assertNotNull(ehubError);
            ErrorCause actCause = ehubError.getCause();
            assertEquals(ErrorCause.MISSING_SECRET_KEY, actCause);
        }
    }
}
