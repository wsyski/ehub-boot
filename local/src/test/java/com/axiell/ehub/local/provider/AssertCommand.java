package com.axiell.ehub.local.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AssertCommand extends AbstractCommand<CommandData> {
    private boolean invoked;

    @Override
    public void run(final CommandData data) {
        invoked = true;
    }

    public void verifyInvocation() {
        assertThat("Next command was never invoked", invoked, is(true));
    }
}
