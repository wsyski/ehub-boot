package com.axiell.ehub.controller.provider.converter;

import com.axiell.authinfo.AuthInfo;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class AuthInfoParamConverterProvider implements ParamConverterProvider {

    private final ParamConverter<AuthInfo> authInfoConverter;

    public AuthInfoParamConverterProvider(final ParamConverter<AuthInfo> authInfoConverter) {
        this.authInfoConverter = authInfoConverter;
    }
    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
        if (rawType.getName().equals(AuthInfo.class.getName())) {
            return (ParamConverter<T>) authInfoConverter;
        }
        return null;
    }
}
