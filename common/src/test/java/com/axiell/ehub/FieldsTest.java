package com.axiell.ehub;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
        Assertions.assertEquals(VALUE, actValue);
    }

    @Test
    public void getRequiredValue_notFound() {
        givenFieldsInstanceWithProvidedDTO();
        try {
            whenGetRequiredValue();
            Assertions.fail("A BadRequestException should have been thrown");
        } catch (BadRequestException e) {
            EhubError ehubError = e.getEhubError();
            ErrorCause errorCause = ehubError.getCause();
            Assertions.assertEquals(ErrorCause.MISSING_FIELD, errorCause);
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
        Assertions.assertNull(actValue);
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
        verify(fieldsMap).put(eq(KEY), eq(VALUE));
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
        Assertions.assertEquals(fieldsDTO, actFieldsDTO);
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
        Assertions.assertNotNull(actFieldsDTO);
    }
}
