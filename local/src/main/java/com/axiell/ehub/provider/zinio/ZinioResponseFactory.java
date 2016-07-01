package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.IContentProviderExceptionFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ZinioResponseFactory implements IZinioResponseFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZinioResponseFactory.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Override
    public IZinioResponse create(final String response, final ContentProviderConsumer contentProviderConsumer, final String language) {
        return new ZinioResponse(response, contentProviderConsumer, language);
    }


    private class ZinioResponse implements IZinioResponse {

        private ZinioStatus status;
        private String message;

        private ZinioResponse(final String response, final ContentProviderConsumer contentProviderConsumer, final String language) {
            if (response != null) {
                String[] parts = response.split("\t");
                if (parts.length == 2) {
                    status = ZinioStatus.valueOf(parts[0]);
                    message = parts[1];
                    if (status == ZinioStatus.FAIL) {
                        IContentProviderExceptionFactory<String> contentProviderExceptionFactory =
                                new ZinioExceptionFactory(contentProviderConsumer, language, ehubExceptionFactory);
                        throw contentProviderExceptionFactory.create(message);
                    }
                } else {
                    throw createInternalServerErrorException("Invalid content provider 'ZINIO' response: " + response);
                }
            } else {
                throw createInternalServerErrorException("Null content provider 'ZINIO' response: ");
            }
        }

        public ZinioStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public <T> List<T> getAsList(final Class<T> clazz) {
            try {
                CollectionType collectionType=OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
                return OBJECT_MAPPER.readValue(message, collectionType);
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw createInternalServerErrorException("Invalid content provider 'ZINIO' json message: " + message);
            }
        }

        private InternalServerErrorException createInternalServerErrorException(String message) {
            return new InternalServerErrorException(message, ErrorCause.INTERNAL_SERVER_ERROR);
        }
    }
}
