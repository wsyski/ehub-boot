package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.provider.borrowbox.BorrowBoxDataAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ZinioResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(BorrowBoxDataAccessor.class);

    private Status status;
    private String message;

    public ZinioResponse(final String response) {
        if (response != null) {
            String[] parts = response.split("\t");
            if (parts.length == 2) {
                status = Status.valueOf(parts[0]);
                message = parts[1];
                if (status == ZinioResponse.Status.FAIL) {
                    throw getInternalServerErrorException("Failure in content provider 'ZINIO' message: " + message);
                }
            }
            else {
                throw getInternalServerErrorException("Invalid content provider 'ZINIO' response: " + response);
            }
        }
        else {
            throw getInternalServerErrorException("Null content provider 'ZINIO' response: ");
        }
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public <T> T getObject(final Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(message, clazz);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw getInternalServerErrorException("Invalid content provider 'ZINIO' json message: " + message);
        }
    }

    private InternalServerErrorException getInternalServerErrorException(String message) {
        return new InternalServerErrorException(message, ErrorCause.INTERNAL_SERVER_ERROR);
    }


    public enum Status {
        OK, FAIL
    }
}
