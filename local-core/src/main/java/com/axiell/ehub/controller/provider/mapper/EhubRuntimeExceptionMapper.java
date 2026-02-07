package com.axiell.ehub.controller.provider.mapper;

import com.axiell.ehub.EhubRuntimeException;
import lombok.extern.slf4j.Slf4j;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

/**
 * An {@link ExceptionMapper} that maps all {@link EhubRuntimeException}s to the {@link Response} of the {@link EhubRuntimeException}.
 */
@Slf4j
@Provider
@Component("ehubRuntimeExceptionMapper")
public final class EhubRuntimeExceptionMapper extends AbstractEhubExceptionMapper<EhubRuntimeException> implements ExceptionMapper<EhubRuntimeException> {

    /**
     * @see jakarta.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse(EhubRuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return exception.getResponse(getMediaType());
    }
}
