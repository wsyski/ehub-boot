package com.axiell.ehub.provider;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.NotFoundException;

public class ContentProviderNameTest {    
    private String name;
    private ContentProviderName actualContentProviderName;

    @Test
    public void noName() {
	givenNoName();
	try {
	    whenFromString();
	    fail("A BadRequestException should have been thrown");
	} catch (NullPointerException e) {
	    fail("A NullPointerException should not be thrown");
	} catch (BadRequestException e) {
	    assertNotNull(e);
	}
    }
    
    private void givenNoName() {
	name = null;
    }

    private void whenFromString() {
	actualContentProviderName = ContentProviderName.fromString(name);
    }

    @Test
    public void unknownName() {
	givenUnknownName();
	try {	   
	    whenFromString();
	    fail("A BadRequestException should have been thrown");
	} catch (NotFoundException e) {
	    assertNotNull(e);
	}
    }
    
    private void givenUnknownName() {
	name = "unknownName";
    }

    @Test
    public void lowerCaseName() {
	givenLowerCaseName();
	whenFromString();
	thenActualContentProviderNameEqualsExpectedContentProviderName();
    }
    
    private void givenLowerCaseName() {
	name = "elib";
    }
    
    private void thenActualContentProviderNameEqualsExpectedContentProviderName() {
	assertEquals(ContentProviderName.ELIB, actualContentProviderName);
    }
    
    @Test
    public void camelCaseName() {
	givenCamelCaseName();
	whenFromString();
	thenActualContentProviderNameEqualsExpectedContentProviderName();
    }
    
    private void givenCamelCaseName() {
	name = "eLiB";
    }
    
    @Test
    public void upperCaseName() {
	givenUpperCaseName();
	whenFromString();
	thenActualContentProviderNameEqualsExpectedContentProviderName();
    }
    
    private void givenUpperCaseName() {
	name = "ELIB";
    }
}
