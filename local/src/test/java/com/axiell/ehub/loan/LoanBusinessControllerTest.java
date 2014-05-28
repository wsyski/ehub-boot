/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis.Result;
import com.axiell.ehub.lms.palma.IPalmaDataAccessor;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderDataAccessorFacade;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.security.AuthInfo;

@RunWith(MockitoJUnitRunner.class)
public class LoanBusinessControllerTest {
    private static final Long READY_LOAN_ID = 0L;
    private static final String LANGUAGE = "en";
    private ILoanBusinessController underTest;
    @Mock
    private IConsumerBusinessController consumerBusinessController;
    @Mock
    private IPalmaDataAccessor palmaDataAccessor;
    @Mock
    private IEhubLoanRepositoryFacade ehubLoanRepositoryFacade;
    @Mock
    private IContentProviderDataAccessorFacade contentProviderDataAccessorFacade;
    @Mock
    private IReadyLoanFactory readyLoanFactory;
    private AuthInfo authInfo;
    private PendingLoan pendingLoan;
    @Mock
    private EhubConsumer ehubConsumer;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private FormatDecoration formatDecoration;
    @Mock
    private LmsLoan lmsLoan;
    @Mock
    private IContent content;
    @Mock
    private ContentProviderLoan contentProviderLoan;
    @Mock
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    @Mock
    private EhubLoan newEhubLoan;
    @Mock
    private EhubLoan existingEhubLoan;
    @Mock
    private ReadyLoan actualReadyLoan;

    @Before
    public void setUpCommonArguments() throws EhubException {
        authInfo = new AuthInfo.Builder(0L, "secret").libraryCard("libraryCard").pin("pin").build();
        pendingLoan = new PendingLoan("lmsRecordId", ContentProviderName.ELIB.toString(), "contentProviderRecordId", "contentProviderFormat");
    }

    @Before
    public void setUpLoanBusinessController() {
        underTest = new LoanBusinessController();
        ReflectionTestUtils.setField(underTest, "consumerBusinessController", consumerBusinessController);
        ReflectionTestUtils.setField(underTest, "palmaDataAccessor", palmaDataAccessor);
        ReflectionTestUtils.setField(underTest, "ehubLoanRepositoryFacade", ehubLoanRepositoryFacade);
        ReflectionTestUtils.setField(underTest, "contentProviderDataAccessorFacade", contentProviderDataAccessorFacade);
        ReflectionTestUtils.setField(underTest, "readyLoanFactory", readyLoanFactory);
    }

    @Before
    public void setUpEhubConsumer() {
        given(consumerBusinessController.getEhubConsumer(any(Long.class))).willReturn(ehubConsumer);
    }

    @Before
    public void setUpContentProviderNameFromExistingLoan() {
        given(existingEhubLoan.getContentProviderLoanMetadata()).willReturn(contentProviderLoanMetadata);
        given(contentProviderLoanMetadata.getContentProvider()).willReturn(contentProvider);
        given(contentProvider.getName()).willReturn(ContentProviderName.ELIB);
    }

    @Before
    public void setUpContentProviderLoan() {
        given(contentProviderDataAccessorFacade.createLoan(any(EhubConsumer.class), any(String.class), any(String.class), any(PendingLoan.class), any(String.class))).willReturn(
                contentProviderLoan);
    }

    @Before
    public void setUpSavedEhubLoan() {
        given(ehubLoanRepositoryFacade.saveEhubLoan(any(EhubConsumer.class), any(LmsLoan.class), any(ContentProviderLoan.class))).willReturn(newEhubLoan);
    }

    @Test
    public void createNewLoan() throws EhubException {
        givenNewLoanAsPreCheckoutAnalysisResult();
        whenCreateLoan();
        thenNewEhubLoanIsSavedInTheEhubDatabase();
    }

    private void givenNewLoanAsPreCheckoutAnalysisResult() {
        CheckoutTestAnalysis preCheckoutAnalysis = new CheckoutTestAnalysis(Result.NEW_LOAN, null);
        given(palmaDataAccessor.checkoutTest(any(EhubConsumer.class), any(PendingLoan.class), any(String.class), any(String.class))).willReturn(
                preCheckoutAnalysis);
    }

    private void whenCreateLoan() {
        actualReadyLoan = underTest.createLoan(authInfo, pendingLoan, LANGUAGE);
    }

    private void thenNewEhubLoanIsSavedInTheEhubDatabase() {
        InOrder inOrder = inOrder(consumerBusinessController, palmaDataAccessor, contentProviderDataAccessorFacade, palmaDataAccessor, ehubLoanRepositoryFacade);
        inOrder.verify(consumerBusinessController).getEhubConsumer(any(AuthInfo.class));
        inOrder.verify(palmaDataAccessor).checkoutTest(any(EhubConsumer.class), any(PendingLoan.class), any(String.class), any(String.class));
        inOrder.verify(contentProviderDataAccessorFacade).createLoan(any(EhubConsumer.class), any(String.class), any(String.class), any(PendingLoan.class), any(String.class));
        inOrder.verify(palmaDataAccessor).checkout(any(EhubConsumer.class), any(PendingLoan.class), any(Date.class), any(String.class), any(String.class));
        inOrder.verify(ehubLoanRepositoryFacade).saveEhubLoan(any(EhubConsumer.class), any(LmsLoan.class), any(ContentProviderLoan.class));
    }

    @Test
    public void createActiveLoan() {
        givenActiveLoanAsPreCheckoutAnalysisResult();
        givenEhubLoanCanBeFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId();

        whenCreateLoan();

        InOrder inOrder = thenEhubLoanIsFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId();
        thenContentIsRetrievedFromContentProvider(inOrder);
    }

    private void givenActiveLoanAsPreCheckoutAnalysisResult() {
        CheckoutTestAnalysis preCheckoutAnalysis = new CheckoutTestAnalysis(Result.ACTIVE_LOAN, "lmsLoanId");
        given(palmaDataAccessor.checkoutTest(any(EhubConsumer.class), any(PendingLoan.class), any(String.class), any(String.class))).willReturn(
                preCheckoutAnalysis);
    }

    private void givenEhubLoanCanBeFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId() {
        given(ehubLoanRepositoryFacade.findEhubLoan(any(EhubConsumer.class), any(String.class))).willReturn(existingEhubLoan);
    }

    private InOrder thenEhubLoanIsFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId() {
        InOrder inOrder = inOrder(consumerBusinessController, ehubLoanRepositoryFacade, contentProviderDataAccessorFacade);
        inOrder.verify(consumerBusinessController).getEhubConsumer(any(AuthInfo.class));
        inOrder.verify(ehubLoanRepositoryFacade).findEhubLoan(any(EhubConsumer.class), (any(String.class)));
        return inOrder;
    }

    private void thenContentIsRetrievedFromContentProvider(InOrder inOrder) {
        inOrder.verify(contentProviderDataAccessorFacade).getContent(any(EhubConsumer.class), any(EhubLoan.class), any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void getReadyLoanByReadyLoanId() {
        givenEhubLoanCanBeFoundInTheEhubDatabaseByReadyLoanLoanId();

        whenGetReadyLoanByReadLoanId();

        InOrder inOrder = thenEhubLoanIsFoundInTheEhubDatabaseByReadyLoanId();
        thenContentIsRetrievedFromContentProvider(inOrder);
    }

    private void givenEhubLoanCanBeFoundInTheEhubDatabaseByReadyLoanLoanId() {
        given(ehubLoanRepositoryFacade.findEhubLoan(any(EhubConsumer.class), any(Long.class))).willReturn(existingEhubLoan);
    }

    private void whenGetReadyLoanByReadLoanId() {
        actualReadyLoan = underTest.getReadyLoan(authInfo, READY_LOAN_ID, LANGUAGE);
    }

    private InOrder thenEhubLoanIsFoundInTheEhubDatabaseByReadyLoanId() {
        InOrder inOrder = inOrder(consumerBusinessController, ehubLoanRepositoryFacade, contentProviderDataAccessorFacade);
        inOrder.verify(consumerBusinessController).getEhubConsumer(any(AuthInfo.class));
        inOrder.verify(ehubLoanRepositoryFacade).findEhubLoan(any(EhubConsumer.class), any(Long.class));
        return inOrder;
    }

    private void whenGetReadyLoanByLmsLoanId() {
        actualReadyLoan = underTest.getReadyLoan(authInfo, "lmsLoanId", LANGUAGE);
    }

    @Test
    public void getReadyLoanByLmsLoanId() {
        givenEhubLoanCanBeFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId();

        whenGetReadyLoanByLmsLoanId();

        InOrder inOrder = thenEhubLoanIsFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId();
        thenContentIsRetrievedFromContentProvider(inOrder);
    }
}
