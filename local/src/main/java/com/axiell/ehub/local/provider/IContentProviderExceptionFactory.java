package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.InternalServerErrorException;
import jakarta.ws.rs.core.Response;

public interface IContentProviderExceptionFactory<E> {

    InternalServerErrorException create(Response response);

    InternalServerErrorException create(E entity);
}
