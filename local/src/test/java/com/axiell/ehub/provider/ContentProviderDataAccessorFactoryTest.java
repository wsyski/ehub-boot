package com.axiell.ehub.provider;

import com.axiell.ehub.provider.askews.AskewsDataAccessor;
import com.axiell.ehub.provider.borrowbox.BorrowBoxDataAccessor;
import com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor;
import com.axiell.ehub.provider.elib.library3.Elib3DataAccessor;
import com.axiell.ehub.provider.ep.lpf.LpfEpDataAccessor;
import com.axiell.ehub.provider.ep.lpp.LppEpDataAccessor;
import com.axiell.ehub.provider.f1.F1DataAccessor;
import com.axiell.ehub.provider.ocd.OcdDataAccessor;
import com.axiell.ehub.provider.overdrive.OverDriveDataAccessor;
import com.axiell.ehub.provider.zinio.ZinioDataAccessor;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static com.axiell.ehub.provider.ContentProvider.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ContentProviderDataAccessorFactoryTest {
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";

    private IContentProviderDataAccessorFactory underTest;
    @Mock
    private LpfEpDataAccessor lpfEpDataAccessor;
    @Mock
    private LppEpDataAccessor lppEpDataAccessor;
    @Mock
    private ElibUDataAccessor elibUDataAccessor;
    @Mock
    private BorrowBoxDataAccessor borrowBoxDataAccessor;
    @Mock
    private ZinioDataAccessor zinioDataAccessor;
    @Mock
    private AskewsDataAccessor askewsDataAccessor;
    @Mock
    private OverDriveDataAccessor overDriveDataAccessor;
    @Mock
    private Elib3DataAccessor elib3DataAccessor;
    @Mock
    private F1DataAccessor f1DataAccessor;
    @Mock
    private OcdDataAccessor ocdDataAccessor;
    @Mock
    private ContentProvider contentProvider;

    private IContentProviderDataAccessor actualContentProviderDataAccessor;

    @Before
    public void setUp() {
        underTest = new ContentProviderDataAccessorFactory();
        ReflectionTestUtils.setField(underTest, "lpfEpDataAccessor", lpfEpDataAccessor);
        ReflectionTestUtils.setField(underTest, "lppEpDataAccessor", lppEpDataAccessor);
        ReflectionTestUtils.setField(underTest, "elibUDataAccessor", elibUDataAccessor);
        ReflectionTestUtils.setField(underTest, "borrowBoxDataAccessor", borrowBoxDataAccessor);
        ReflectionTestUtils.setField(underTest, "zinioDataAccessor", zinioDataAccessor);
        ReflectionTestUtils.setField(underTest, "askewsDataAccessor", askewsDataAccessor);
        ReflectionTestUtils.setField(underTest, "overDriveDataAccessor", overDriveDataAccessor);
        ReflectionTestUtils.setField(underTest, "elib3DataAccessor", elib3DataAccessor);
        ReflectionTestUtils.setField(underTest, "f1DataAccessor", f1DataAccessor);
        ReflectionTestUtils.setField(underTest, "ocdDataAccessor", ocdDataAccessor);
    }

    @Test
    public void lpfEpDataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_TEST_EP, false);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(LpfEpDataAccessor.class);
    }

    @Test
    public void lppEpDataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_TEST_EP, true);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(LppEpDataAccessor.class);
    }

    @Test
    public void elibUDataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_ELIBU, true);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(ElibUDataAccessor.class);

    }

    @Test
    public void elib3DataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_ELIB3, true);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(Elib3DataAccessor.class);

    }

    @Test
    public void borrowboxDataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_BORROWBOX, false);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(BorrowBoxDataAccessor.class);
    }

    @Test
    public void zinioDataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_ZINIO, false);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(ZinioDataAccessor.class);
    }

    @Test
    public void askewsDataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_ASKEWS, false);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(AskewsDataAccessor.class);
    }

    @Test
    public void overdriveDataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_OVERDRIVE, false);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(OverDriveDataAccessor.class);
    }

    @Test
    public void f1DataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_F1, false);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(F1DataAccessor.class);
    }

    @Test
    public void ocdDataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_OCD, false);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(OcdDataAccessor.class);
    }

    private void whenGetContentProviderDataAccessor() {
        actualContentProviderDataAccessor = underTest.getInstance(contentProvider);
    }

    private void thenExpectedDataAccessorIsReturned(final Class clazz) {
        Assert.assertThat(actualContentProviderDataAccessor, Matchers.instanceOf(clazz));
    }

    private void givenContentProvider(final String name, final boolean isLoanPerProduct) {
        given(contentProvider.getName()).willReturn(name);
        given(contentProvider.isLoanPerProduct()).willReturn(isLoanPerProduct);
    }
}
