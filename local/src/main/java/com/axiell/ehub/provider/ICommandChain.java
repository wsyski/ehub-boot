package com.axiell.ehub.provider;

public interface ICommandChain<T, D> {

    T execute(D data);
}
