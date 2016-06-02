package com.axiell.ehub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public abstract class DTOTestFixture<T> {
    private T underTest = getTestInstance();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void serializeAndDeserialize() throws IOException {
        String json = givenSerializeObject();
        T actualObject = whenDeserializeObject(json);
        thenExpectedActualObject(actualObject);
    }

    private T whenDeserializeObject(String json) throws IOException {
        return objectMapper.readValue(json, getTestClass());
    }

    private String givenSerializeObject() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(underTest);
        System.out.println(json);
        return json;
    }

    private void thenExpectedActualObject(T actualObject) {
        Assert.assertThat(actualObject, Matchers.is(underTest));
    }

    protected abstract T getTestInstance();

    protected abstract Class<T> getTestClass();
}
