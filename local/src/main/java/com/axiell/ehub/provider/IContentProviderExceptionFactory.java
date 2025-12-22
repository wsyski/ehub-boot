package com.axiell.ehub.provider;

import com.axiell.ehub.InternalServerErrorException;

import jakarta.ws.rs.core.Response;

public interface IContentProviderExceptionFactory<E> {

    InternalServerErrorException create(Response response);

    InternalServerErrorException create(E entity);
}
