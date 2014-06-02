package com.axiell.ehub.provider;

public interface ICommandChain<T, D extends ICommandData> {

    T execute(D data);
}
