package com.axiell.ehub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static junit.framework.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FieldsTest {
    private static final String KEY = "key1";
    private static final String VALUE = "value1";
    @Mock
    private FieldsDTO fieldsDTO;
    @Mock
    private Map<String, String> fieldsMap;
    private Fields underTest;
    private String actValue;
    private FieldsDTO actFieldsDTO;

    @Test
    public void getRequiredValue_found() {
        givenExpectedValueInFieldsDTOMap();
        givenFieldsInstanceWithProvidedDTO();
        whenGetRequiredValue();
        thenActualValueEqualsExpectedValue();
    }

    private void givenExpectedValueInFieldsDTOMap() {
        given(fieldsMap.get(KEY)).willReturn(VALUE);
    }

    private void givenFieldsInstanceWithProvidedDTO() {
        given(fieldsDTO.getFields()).willReturn(fieldsMap);
        underTest = new Fields(fieldsDTO);
    }

    private void whenGetRequiredValue() {
        actValue = underTest.getRequiredValue(KEY);
    }

    private void thenActualValueEqualsExpectedValue() {
        assertEquals(VALUE, actValue);
    }

    @Test
    public void getRequiredValue_notFound() {
        givenFieldsInstanceWithProvidedDTO();
        try {
            whenGetRequiredValue();
            fail("A BadRequestException should have been thrown");
        } catch (BadRequestException e) {
            EhubError ehubError = e.getEhubError();
            ErrorCause errorCause = ehubError.getCause();
            assertEquals(ErrorCause.MISSING_FIELD, errorCause);
        }
    }

    @Test
    public void getOptionalValue_found() {
        givenExpectedValueInFieldsDTOMap();
        givenFieldsInstanceWithProvidedDTO();
        whenGetOptionalValue();
        thenActualValueEqualsExpectedValue();
    }

    private void whenGetOptionalValue() {
        actValue = underTest.getOptionalValue(KEY);
    }

    @Test
    public void getOptionalValue_notFound() {
        givenFieldsInstanceWithProvidedDTO();
        whenGetOptionalValue();
        thenActualValueIsNull();
    }

    private void thenActualValueIsNull() {
        assertNull(actValue);
    }

    @Test
    public void addValue() {
        givenFieldsInstanceWithProvidedDTO();
        whenAddValue();
        thenValueIsPutToUnderlyingMap();
    }

    private void whenAddValue() {
        underTest.addValue(KEY, VALUE);
    }

    private void thenValueIsPutToUnderlyingMap() {
        verify(fieldsDTO).getFields();
        verify(fieldsMap).put(Matchers.eq(KEY), Matchers.eq(VALUE));
    }

    @Test
    public void toDTO() {
        givenFieldsInstanceWithProvidedDTO();
        whenToDTO();
        thenActualDTOEqualsExpectedDTO();
    }

    private void whenToDTO() {
        actFieldsDTO = underTest.toDTO();
    }

    private void thenActualDTOEqualsExpectedDTO() {
        assertEquals(fieldsDTO, actFieldsDTO);
    }

    @Test
    public void emptyConstructor() {
        givenFieldsInstanceWithEmptyConstructor();
        whenToDTO();
        thenActualDTOIsNotNull();
    }

    private void givenFieldsInstanceWithEmptyConstructor() {
        underTest = new Fields();
    }

    private void thenActualDTOIsNotNull() {
        assertNotNull(actFieldsDTO);
    }
}
