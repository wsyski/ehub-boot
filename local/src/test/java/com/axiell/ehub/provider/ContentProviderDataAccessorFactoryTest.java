package com.axiell.ehub.provider;

import com.axiell.ehub.provider.elib.library3.Elib3DataAccessor;
import com.axiell.ehub.provider.ep.lpf.LpfEpDataAccessor;
import com.axiell.ehub.provider.ep.lpp.LppEpDataAccessor;
import com.axiell.ehub.provider.overdrive.OverDriveDataAccessor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import static com.axiell.ehub.provider.ContentProvider.CONTENT_PROVIDER_ELIB3;
import static com.axiell.ehub.provider.ContentProvider.CONTENT_PROVIDER_OVERDRIVE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ContentProviderDataAccessorFactoryTest {
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";

    private IContentProviderDataAccessorFactory underTest;
    @Mock
    private LpfEpDataAccessor lpfEpDataAccessor;
    @Mock
    private LppEpDataAccessor lppEpDataAccessor;
    @Mock
    private OverDriveDataAccessor overDriveDataAccessor;
    @Mock
    private Elib3DataAccessor elib3DataAccessor;
    @Mock
    private ContentProvider contentProvider;

    private IContentProviderDataAccessor actualContentProviderDataAccessor;

    @BeforeEach
    public void setUp() {
        underTest = new ContentProviderDataAccessorFactory();
        ReflectionTestUtils.setField(underTest, "lpfEpDataAccessor", lpfEpDataAccessor);
        ReflectionTestUtils.setField(underTest, "lppEpDataAccessor", lppEpDataAccessor);
        ReflectionTestUtils.setField(underTest, "overDriveDataAccessor", overDriveDataAccessor);
        ReflectionTestUtils.setField(underTest, "elib3DataAccessor", elib3DataAccessor);
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
    public void elib3DataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_ELIB3, true);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(Elib3DataAccessor.class);

    }

    @Test
    public void overdriveDataAccessor() {
        givenContentProvider(CONTENT_PROVIDER_OVERDRIVE, false);
        whenGetContentProviderDataAccessor();
        thenExpectedDataAccessorIsReturned(OverDriveDataAccessor.class);
    }

    private void whenGetContentProviderDataAccessor() {
        actualContentProviderDataAccessor = underTest.getInstance(contentProvider);
    }

    private void thenExpectedDataAccessorIsReturned(final Class clazz) {
        assertThat(actualContentProviderDataAccessor, Matchers.instanceOf(clazz));
    }

    private void givenContentProvider(final String name, final boolean isLoanPerProduct) {
        given(contentProvider.getName()).willReturn(name);
        given(contentProvider.isLoanPerProduct()).willReturn(isLoanPerProduct);
    }
}
