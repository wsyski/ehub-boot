package com.axiell.ehub.provider;

public class EndCommand implements ICommand<CommandData> {

    @Override
    public void next(final ICommand command) {
        throw new IllegalArgumentException("You are not allowed to configure next on an end command");
    }

    @Override
    public void forward(final ICommandResult result, final CommandData data) {
        //Chain ends, does nothing
    }

    @Override
    public void run(final CommandData credentials) {
        //Chain ends, does nothing
    }

    @Override
    public ICommand on(final ICommandResult result, final ICommand next) {
        throw new IllegalArgumentException("You are not allowed to configure on on an end command");
    }
}
