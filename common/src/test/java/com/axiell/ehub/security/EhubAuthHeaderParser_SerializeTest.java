package com.axiell.ehub.security;

import com.axiell.authinfo.*;
import com.axiell.ehub.EhubException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.MessageFormat;

import static com.axiell.ehub.util.EhubUrlCodec.authInfoEncode;

public class EhubAuthHeaderParser_SerializeTest extends EhubAuthHeaderParserFixture {
    private String SIGNATURE_0 = "xG96OslJLhuDXYSiiEadvxL0L3Q=";
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
                .format("ehub_consumer_id=\"{0}\", ehub_library_card=\"{1}\", ehub_pin=\"{2}\", ehub_signature=\"{3}\"",
                        authInfoEncode(String.valueOf(EHUB_CONSUMER_ID)), authInfoEncode(LIBRARY_CARD), authInfoEncode(PIN), authInfoEncode(SIGNATURE_0));
        expInfoValue2 = MessageFormat.format("ehub_consumer_id=\"{0}\", ehub_signature=\"{1}\"",
                authInfoEncode(String.valueOf(EHUB_CONSUMER_ID)), authInfoEncode(SIGNATURE_1));
        expInfoValue3 = MessageFormat
                .format("ehub_consumer_id=\"{0}\", ehub_patron_id=\"{1}\", ehub_library_card=\"{2}\", ehub_pin=\"{3}\", ehub_signature=\"{4}\"",
                        authInfoEncode(String.valueOf(EHUB_CONSUMER_ID)), authInfoEncode(PATRON_ID), authInfoEncode(LIBRARY_CARD), authInfoEncode(PIN),
                        authInfoEncode(SIGNATURE_2));
        expInfoValue4 = MessageFormat
                .format("ehub_consumer_id=\"{0}\", ehub_patron_id=\"{1}\", ehub_library_card=\"{2}\", ehub_pin=\"{3}\", ehub_email=\"{4}\", ehub_signature=\"{5}\"",
                        authInfoEncode(String.valueOf(EHUB_CONSUMER_ID)), authInfoEncode(PATRON_ID), authInfoEncode(LIBRARY_CARD), authInfoEncode(PIN),
                        authInfoEncode(EMAIL), authInfoEncode(SIGNATURE_3));
    }

    /**
     * Test method for {@link AuthInfo#toString()}.
     *
     * @throws EhubException
     */
    @Test
    public void testSerialize() throws EhubException {
        AuthInfo expInfo1 = new AuthInfo.Builder().ehubConsumerId(EHUB_CONSUMER_ID).patron(new Patron.Builder().libraryCard(LIBRARY_CARD).pin(PIN).build()).build();
        String actInfoValue1 = underTest.serialize(expInfo1);
        Assert.assertEquals(expInfoValue1, actInfoValue1);

        AuthInfo expInfo2 = new AuthInfo.Builder().ehubConsumerId(EHUB_CONSUMER_ID).patron(new Patron.Builder().build()).build();
        String actInfoValue2 = underTest.serialize(expInfo2);
        Assert.assertEquals(expInfoValue2, actInfoValue2);

        AuthInfo expInfo3 =
                new AuthInfo.Builder().ehubConsumerId(EHUB_CONSUMER_ID).patron(new Patron.Builder().id(PATRON_ID).libraryCard(LIBRARY_CARD).pin(PIN).build()).build();
        String actInfoValue3 = underTest.serialize(expInfo3);
        Assert.assertEquals(expInfoValue3, actInfoValue3);

        AuthInfo expInfo4 =
                new AuthInfo.Builder().ehubConsumerId(EHUB_CONSUMER_ID).patron(new Patron.Builder().id(PATRON_ID).libraryCard(LIBRARY_CARD).pin(PIN).email(EMAIL).build()).build();
        String actInfoValue4 = underTest.serialize(expInfo4);
        Assert.assertEquals(expInfoValue4, actInfoValue4);
    }

    /**
     *
     */
    @Test(expected = MissingSecretKeyRuntimeException.class)
    public void testMissingSecretKey() {
        AuthInfo authInfo = new AuthInfo.Builder().ehubConsumerId(Long.MAX_VALUE).build();
        underTest.setAuthHeaderSecretKeyResolver(new NullAuthHeaderSecretKeyResolver());
        underTest.serialize(authInfo);
    }

    protected static class NullAuthHeaderSecretKeyResolver extends AbstractAuthHeaderSecretKeyResolver implements IAuthHeaderSecretKeyResolver {
        @Override
        public String getSecretKey(final AuthInfo authInfo) {
            return null;
        }

        @Override
        public long getExpirationTimeInSeconds(final AuthInfo authInfo) {
            return 0;
        }

        @Override
        public long getLeewayInSeconds(final AuthInfo authInfo) {
            return 0;
        }
    }
}
