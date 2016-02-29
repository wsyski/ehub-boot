package com.axiell.ehub.provider;

import com.axiell.ehub.provider.askews.AskewsDataAccessor;
import com.axiell.ehub.provider.borrowbox.BorrowBoxDataAccessor;
import com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor;
import com.axiell.ehub.provider.elib.library3.Elib3DataAccessor;
import com.axiell.ehub.provider.ep.EpDataAccessor;
import com.axiell.ehub.provider.f1.F1DataAccessor;
import com.axiell.ehub.provider.ocd.OcdDataAccessor;
import com.axiell.ehub.provider.overdrive.OverDriveDataAccessor;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ContentProviderDataAccessorFactoryTest {
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";

    private IContentProviderDataAccessorFactory underTest;
    @Mock
    private EpDataAccessor epDataAccessor;
    @Mock
    private ElibUDataAccessor elibUDataAccessor;
    @Mock
    private BorrowBoxDataAccessor borrowBoxDataAccessor;
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

    private String contentProviderName;
    private IContentProviderDataAccessor actualContentProviderDataAccessor;

    @Before
    public void setUp() {
        underTest = new ContentProviderDataAccessorFactory();
        ReflectionTestUtils.setField(underTest, "epDataAccessor", epDataAccessor);
        ReflectionTestUtils.setField(underTest, "elibUDataAccessor", elibUDataAccessor);
        ReflectionTestUtils.setField(underTest, "borrowBoxDataAccessor", borrowBoxDataAccessor);
        ReflectionTestUtils.setField(underTest, "askewsDataAccessor", askewsDataAccessor);
        ReflectionTestUtils.setField(underTest, "overDriveDataAccessor", overDriveDataAccessor);
        ReflectionTestUtils.setField(underTest, "elib3DataAccessor", elib3DataAccessor);
        ReflectionTestUtils.setField(underTest, "f1DataAccessor", f1DataAccessor);
        ReflectionTestUtils.setField(underTest, "ocdDataAccessor", ocdDataAccessor);
    }

    @Test
    public void getEpDataAccessor() {
        givenEpAsContentProviderName();
        whenGetContentProviderDataAccessor();
        thenEpDataAccessorIsReturned();
    }

    private void givenEpAsContentProviderName() {
        contentProviderName = CONTENT_PROVIDER_TEST_EP;
    }

    private void whenGetContentProviderDataAccessor() {
        actualContentProviderDataAccessor = underTest.getInstance(contentProviderName);
    }

    private void thenEpDataAccessorIsReturned() {
        Assert.assertTrue(actualContentProviderDataAccessor instanceof EpDataAccessor);
    }

    @Test
    public void getElibUDataAccessor() {
        givenElibUAsContentProviderName();
        whenGetContentProviderDataAccessor();
        thenElibUDataAccessorIsReturned();
    }

    private void givenElibUAsContentProviderName() {
        contentProviderName = ContentProvider.CONTENT_PROVIDER_ELIBU;
    }

    private void thenElibUDataAccessorIsReturned() {
        Assert.assertTrue(actualContentProviderDataAccessor instanceof ElibUDataAccessor);
    }

    @Test
    public void getPublitDataAccessor() {
        givenPublitAsContentProviderName();
        whenGetContentProviderDataAccessor();
        thenPublitDataAccessorIsReturned();
    }

    private void givenPublitAsContentProviderName() {
        contentProviderName = ContentProvider.CONTENT_PROVIDER_BORROWBOX;
    }

    private void thenPublitDataAccessorIsReturned() {
        Assert.assertTrue(actualContentProviderDataAccessor instanceof BorrowBoxDataAccessor);
    }

    @Test
    public void getAskewsDataAccessor() {
        givenAskewsAsContentProviderName();
        whenGetContentProviderDataAccessor();
        thenAskewsDataAccessorIsReturned();
    }

    private void givenAskewsAsContentProviderName() {
        contentProviderName = ContentProvider.CONTENT_PROVIDER_ASKEWS;
    }

    private void thenAskewsDataAccessorIsReturned() {
        Assert.assertTrue(actualContentProviderDataAccessor instanceof AskewsDataAccessor);
    }

    @Test
    public void getOverDriveDataAccessor() {
        givenOverDriveAsContentProviderName();
        whenGetContentProviderDataAccessor();
        thenOverDriveDataAccessorIsReturned();
    }

    private void givenOverDriveAsContentProviderName() {
        contentProviderName = ContentProvider.CONTENT_PROVIDER_OVERDRIVE;
    }

    private void thenOverDriveDataAccessorIsReturned() {
        Assert.assertTrue(actualContentProviderDataAccessor instanceof OverDriveDataAccessor);
    }

    @Test
    public void getElib3DataAccessor() {
        givenElib3AsContentProviderName();
        whenGetContentProviderDataAccessor();
        thenElib3DataAccessorIsReturned();
    }

    private void givenElib3AsContentProviderName() {
        contentProviderName = ContentProvider.CONTENT_PROVIDER_ELIB3;
    }

    private void thenElib3DataAccessorIsReturned() {
        Assert.assertTrue(actualContentProviderDataAccessor instanceof Elib3DataAccessor);
    }

    @Test
    public void getF1DataAccessor() {
        givenF1AsContentProviderName();
        whenGetContentProviderDataAccessor();
        thenF1DataAccessorIsReturned();
    }

    private void givenF1AsContentProviderName() {
        contentProviderName = ContentProvider.CONTENT_PROVIDER_F1;
    }

    private void thenF1DataAccessorIsReturned() {
        Assert.assertTrue(actualContentProviderDataAccessor instanceof F1DataAccessor);
    }

    @Test
    public void getOcdDataAccessor() {
        givenOcdAsContentProviderName();
        whenGetContentProviderDataAccessor();
        thenOcdDataAccessorIsReturned();
    }

    private void givenOcdAsContentProviderName() {
        contentProviderName = ContentProvider.CONTENT_PROVIDER_OCD;
    }

    private void thenOcdDataAccessorIsReturned() {
        Assert.assertTrue(actualContentProviderDataAccessor instanceof OcdDataAccessor);
    }
}
