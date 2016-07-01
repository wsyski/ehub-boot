package com.axiell.ehub.provider.zinio;

public interface IZinioResponse {
    ZinioStatus getStatus();

    String getMessage();

    <T> T getObject(Class<T> clazz);
}
