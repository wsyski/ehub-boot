package com.axiell.ehub.common;

import com.axiell.ehub.common.EhubError;
import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.common.ErrorCauseArgument.Type;
import com.axiell.ehub.common.controller.provider.json.JsonProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class EhubErrorTest {
    private String exceptionAsJson;
    private EhubError expEhubError;
    private EhubError actEhubError;

    @BeforeEach
    public void setUp() {
        final ErrorCauseArgument argument = new ErrorCauseArgument(Type.LMS_LOAN_ID, "unknownLmsLoanId1");
        expEhubError = ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND.toEhubError(argument);
    }

    @Test
    public void ehubError() throws JsonProcessingException {
        givenEhubErrorAsJson();
        whenDeserializeEhubError();
        thenActualEhubErrorEqualsExpectedEhubError();
    }

    private void givenEhubErrorAsJson() throws JsonProcessingException {
        exceptionAsJson = JsonProvider.OBJECT_MAPPER.writeValueAsString(expEhubError);
        log.debug(exceptionAsJson);
    }

    private void whenDeserializeEhubError() throws JsonProcessingException {
        actEhubError = JsonProvider.OBJECT_MAPPER.readValue(exceptionAsJson, EhubError.class);
    }

    private void thenActualEhubErrorEqualsExpectedEhubError() {
        Assertions.assertEquals(expEhubError.getCause(), actEhubError.getCause());
        Assertions.assertEquals(expEhubError.getMessage(), actEhubError.getMessage());
        Assertions.assertNotNull(actEhubError.getArguments());
        Assertions.assertEquals(expEhubError.getArguments().size(), actEhubError.getArguments().size());
    }
}
