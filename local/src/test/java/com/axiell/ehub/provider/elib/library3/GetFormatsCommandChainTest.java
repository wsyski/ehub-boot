package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetFormatsCommandChainTest {
    private GetFormatsCommandChain underTest;
    @Mock
    private IElibFacade elibFacade;
    @Mock
    private IEhubExceptionFactory exceptionFactory;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private ContentProvider contentProvider;

    @Before
    public void setUpUnderTest() {
        underTest = new GetFormatsCommandChain(elibFacade, exceptionFactory);
    }

    @Test
    public void execute() {

    }
}
