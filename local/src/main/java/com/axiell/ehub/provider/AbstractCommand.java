package com.axiell.ehub.provider;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractCommand<D extends ICommandData> implements ICommand<D> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommand.class);
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
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(toString());
            LOGGER.debug("Forwarding " + result + " with data: " + data);
        }
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
