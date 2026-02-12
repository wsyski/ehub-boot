package com.axiell.ehub.common.util;


public interface IFinder<T, C> {
    T find(IMatcher<T> matcher, C collection) throws NotFoundException;


    public static final class NotFoundException extends RuntimeException {
    }

}
