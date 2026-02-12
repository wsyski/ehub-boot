package com.axiell.ehub.local.provider;

public interface ICommand<D extends ICommandData> {

    void next(ICommand command);

    void forward(ICommandResult result, D data);

    void run(D data);

    ICommand on(ICommandResult result, ICommand next);
}
