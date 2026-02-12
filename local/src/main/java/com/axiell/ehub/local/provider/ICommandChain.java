package com.axiell.ehub.local.provider;

public interface ICommandChain<T, D> {

    T execute(D data);
}
