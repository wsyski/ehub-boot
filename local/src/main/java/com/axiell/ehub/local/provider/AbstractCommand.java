package com.axiell.ehub.local.provider;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public abstract class AbstractCommand<D extends ICommandData> implements ICommand<D> {
    private ICommand next = null;
    private final Map<ICommandResult, ICommand> forwards = Maps.newHashMap();

    @Override
    public void next(final ICommand command) {
        if (!forwards.isEmpty())
            throw new IllegalArgumentException("Ambiguous command chain, cannot add command using next when on have been used");
        if (next != null)
            throw new IllegalArgumentException("Ambiguous command chain, cannot add command using next when next have been used");
        this.next = command;
    }

    @Override
    public void forward(final ICommandResult result, final D data) {
        logForwardToDebug(result, data);
        if (next != null) {
            next.run(data);
            return;
        }
        forwards.get(result).run(data);
    }

    private void logForwardToDebug(final ICommandResult result, final D data) {
        log.debug(toString());
        log.debug("Forwarding " + result + " with data: " + data);
    }

    @Override
    public ICommand on(final ICommandResult result, final ICommand next) {
        if (this.next != null)
            throw new IllegalArgumentException("Ambiguous command chain, cannot add command using on when next is set");
        if (forwards.containsKey(result))
            throw new IllegalArgumentException("Command already forwards to " + next.getClass().getName() + " on " + result.getClass().getName());
        forwards.put(result, next);
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{next=" + next + ", forwards=" + forwards + "}";
    }
}
