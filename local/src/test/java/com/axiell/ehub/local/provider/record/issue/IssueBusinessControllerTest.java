/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.provider.record.issue;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.core.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.local.consumer.IConsumerBusinessController;
import com.axiell.ehub.local.provider.IContentProviderDataAccessorFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class IssueBusinessControllerTest {
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    private IIssueBusinessController underTest;
    @Mock
    private IConsumerBusinessController consumerBusinessController;
    @Mock
    private IContentProviderDataAccessorFacade contentProviderDataAccessorFacade;
    @Mock
    private EhubConsumer ehubConsumer;
    private AuthInfo authInfo;

    @BeforeEach
    public void setUpCommonArguments() throws Exception {
        authInfo = new AuthInfo.Builder().ehubConsumerId(0L).patron(new Patron.Builder().libraryCard("libraryCard").pin("pin").build()).build();
    }

    @BeforeEach
    public void setUpFormatBusinessController() {
        underTest = new IssueBusinessController();
        ReflectionTestUtils.setField(underTest, "consumerBusinessController", consumerBusinessController);
        ReflectionTestUtils.setField(underTest, "contentProviderDataAccessorFacade", contentProviderDataAccessorFacade);
    }

    @BeforeEach
    public void setUpEhubConsumer() {
        given(consumerBusinessController.getEhubConsumer(any(Long.class))).willReturn(ehubConsumer);
        given(consumerBusinessController.getEhubConsumer(authInfo)).willReturn(ehubConsumer);
    }

    @Test
    public void testGetFormats() {
        whenGetFormats();
        thenFormatsAreRetrievedFromContentProvider();
    }

    private void whenGetFormats() {
        underTest.getIssues(authInfo, CONTENT_PROVIDER_TEST_EP, "contentProviderRecordId", "language");
    }

    private void thenFormatsAreRetrievedFromContentProvider() {
        InOrder inOrder = inOrder(consumerBusinessController, contentProviderDataAccessorFacade);
        inOrder.verify(consumerBusinessController).getEhubConsumer(any(AuthInfo.class));
        inOrder.verify(contentProviderDataAccessorFacade).getIssues(any(EhubConsumer.class), any(String.class), any(Patron.class), any(String.class), any(String.class));
    }
}
