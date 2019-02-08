package com.axiell.ehub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public abstract class DTOTestFixture<T> {
    private T underTest = getTestInstance();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void serializeAndDeserialize() throws IOException {
        String json = givenSerializeObject();
        T actualObject = whenDeserializeObject(json);
        thenExpectedActualObject(actualObject);
    }

    protected T whenDeserializeObject(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, getTestClass());
    }

    private String givenSerializeObject() throws JsonProcessingException {
        String json = OBJECT_MAPPER.writeValueAsString(underTest);
        System.out.println(json);
        return json;
    }

    private void thenExpectedActualObject(T actualObject) {
        Assert.assertThat("Objects are not equal", actualObject, Matchers.is(underTest));
        customAssertion(underTest, actualObject);
    }

    protected void customAssertion(final T underTest, final T actualObject) {
    }

    protected abstract T getTestInstance();

    protected abstract Class<T> getTestClass();
}
