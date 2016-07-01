package com.axiell.ehub.provider.zinio;

import java.util.List;

public interface IZinioResponse {
    ZinioStatus getStatus();

    String getMessage();

    <T> List<T> getAsList(Class<T> clazz);
}
