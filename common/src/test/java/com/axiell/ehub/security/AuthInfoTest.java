/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import static com.axiell.ehub.util.SHA512Function.sha512Hex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.MessageFormat;

import com.axiell.ehub.util.SHA512Function;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.security.AuthInfo;

/**
 * 
 */
public class AuthInfoTest {
    private long expEhubConsumerId1 = 1L;
    private String expEhubConsumerSecretKey1 = "secret1";
    private long expEhubConsumerId2 = 2L;
    private String expEhubConsumerSecretKey2 = "secret2";
    private String expPatronId = "abc123";
    private String expLibraryCard1 = "12345";
    private String expPin1 = "1111";
    private String expSignature1 = "w7DHxJ11oSuSH9u8%2BY3GY6iebrw%3D";
    private String expSignature2 = "PUkG2RlaMQkr3jtPKKJf5Sm2oJo%3D";
    private String expSignature3 = "w7DHxJ11oSuSH9u8%2BY3GY6iebrw%3D";
    private String expInfoValue1;
    private String expInfoValue2;
    private String expInfoValue3;

    @Before
    public void setUp() {
        expInfoValue1 = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_patron_id=\"{1}\", ehub_library_card=\"{2}\", ehub_pin=\"{3}\", ehub_signature=\"{4}\"",
                expEhubConsumerId1, sha512Hex(expLibraryCard1), expLibraryCard1, expPin1, expSignature1);
        expInfoValue2 = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_signature=\"{1}\"", expEhubConsumerId2, expSignature2);
        expInfoValue3 = MessageFormat.format("eHUB ehub_consumer_id=\"{0}\", ehub_patron_id=\"{1}\", ehub_library_card=\"{2}\", ehub_pin=\"{3}\", ehub_signature=\"{4}\"",
                expEhubConsumerId1, expPatronId, expLibraryCard1, expPin1, expSignature3);
    }

    /**
     * Test method for {@link com.axiell.ehub.security.AuthInfo#toString()}.
     * @throws EhubException 
     */
    @Test
    public void testToString() throws EhubException {
        AuthInfo expInfo1 = new AuthInfo.Builder(expEhubConsumerId1, expEhubConsumerSecretKey1).libraryCard(expLibraryCard1).pin(expPin1).build();
        String actInfoValue1 = expInfo1.toString();
        Assert.assertEquals(expInfoValue1, actInfoValue1);

        AuthInfo expInfo2 = new AuthInfo.Builder(expEhubConsumerId2, expEhubConsumerSecretKey2).build();
        String actInfoValue2 = expInfo2.toString();
        Assert.assertEquals(expInfoValue2, actInfoValue2);

        AuthInfo expInfo3 = new AuthInfo.Builder(expEhubConsumerId1, expEhubConsumerSecretKey1).patronId(expPatronId).libraryCard(expLibraryCard1).pin(expPin1).build();
        String actInfoValue3 = expInfo3.toString();
        Assert.assertEquals(expInfoValue3, actInfoValue3);
    }
    
    /**
     * 
     */
    @Test
    public void testMissingEhubConsumerId() {
        try  {
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
        try  {
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
