package com.axiell.ehub.provider;

import com.axiell.ehub.InternalServerErrorException;

import javax.ws.rs.core.Response;

public interface IContentProviderExceptionFactory<E> {

    InternalServerErrorException create(Response response);
}
