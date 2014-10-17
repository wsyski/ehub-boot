/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;

import com.axiell.ehub.patron.Patron;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderDataAccessorFacade;
import com.axiell.ehub.security.AuthInfo;

@RunWith(MockitoJUnitRunner.class)
public class FormatBusinessControllerTest {
    private IFormatBusinessController underTest;
    @Mock
    private IConsumerBusinessController consumerBusinessController;
    @Mock
    private IContentProviderDataAccessorFacade contentProviderDataAccessorFacade;
    @Mock
    private EhubConsumer ehubConsumer;
    private AuthInfo authInfo;

    @Before
    public void setUpCommonArguments() throws Exception {
        authInfo = new AuthInfo.Builder(0L, "secret").libraryCard("libraryCard").pin("pin").build();
    }

    @Before
    public void setUpFormatBusinessController() {
        underTest = new FormatBusinessController();
        ReflectionTestUtils.setField(underTest, "consumerBusinessController", consumerBusinessController);
        ReflectionTestUtils.setField(underTest, "contentProviderDataAccessorFacade", contentProviderDataAccessorFacade);
    }

    @Before
    public void setUpEhubConsumer() {
        given(consumerBusinessController.getEhubConsumer(any(Long.class))).willReturn(ehubConsumer);
    }

    @Test
    public void testGetFormats() {
        whenGetFormats();
        thenFormatsAreRetrievedFromContentProvider();
    }

    private void whenGetFormats() {
        underTest.getFormats(authInfo, ContentProviderName.ELIB.toString(), "contentProviderRecordId", "language");
    }

    private void thenFormatsAreRetrievedFromContentProvider() {
        InOrder inOrder = inOrder(consumerBusinessController, contentProviderDataAccessorFacade);
        inOrder.verify(consumerBusinessController).getEhubConsumer(any(AuthInfo.class));
        inOrder.verify(contentProviderDataAccessorFacade).getFormats(any(EhubConsumer.class), any(String.class), any(Patron.class), any(String.class), any(String.class));
    }
}
