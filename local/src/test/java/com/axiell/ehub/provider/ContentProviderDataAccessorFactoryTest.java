package com.axiell.ehub.provider;

import com.axiell.ehub.provider.elib.library3.Elib3DataAccessor;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.provider.askews.AskewsDataAccessor;
import com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor;
import com.axiell.ehub.provider.elib.library.ElibDataAccessor;
import com.axiell.ehub.provider.overdrive.OverDriveDataAccessor;
import com.axiell.ehub.provider.publit.PublitDataAccessor;

@RunWith(MockitoJUnitRunner.class)
public class ContentProviderDataAccessorFactoryTest {
    private IContentProviderDataAccessorFactory underTest;

    @Mock
    private ElibDataAccessor elibDataAccessor;

    @Mock
    private ElibUDataAccessor elibUDataAccessor;

    @Mock
    private PublitDataAccessor publitDataAccessor;

    @Mock
    private AskewsDataAccessor askewsDataAccessor;

    @Mock
    private OverDriveDataAccessor overDriveDataAccessor;

    @Mock
    private Elib3DataAccessor elib3DataAccessor;

    private ContentProviderName contentProviderName;
    private IContentProviderDataAccessor actualContentProviderDataAccessor;

    @Before
    public void setUp() {
        underTest = new ContentProviderDataAccessorFactory();
        ReflectionTestUtils.setField(underTest, "elibDataAccessor", elibDataAccessor);
        ReflectionTestUtils.setField(underTest, "elibUDataAccessor", elibUDataAccessor);
        ReflectionTestUtils.setField(underTest, "publitDataAccessor", publitDataAccessor);
        ReflectionTestUtils.setField(underTest, "askewsDataAccessor", askewsDataAccessor);
        ReflectionTestUtils.setField(underTest, "overDriveDataAccessor", overDriveDataAccessor);
        ReflectionTestUtils.setField(underTest, "elib3DataAccessor", elib3DataAccessor);
    }

    @Test
    public void getElibDataAccessor() {
        givenElibAsContentProviderName();
        whenGetContentProviderDataAccessor();
        thenElibDataAccessorIsReturned();
    }

    private void givenElibAsContentProviderName() {
        contentProviderName = ContentProviderName.ELIB;
    }

    private void whenGetContentProviderDataAccessor() {
        actualContentProviderDataAccessor = underTest.getInstance(contentProviderName);
    }

    private void thenElibDataAccessorIsReturned() {
        Assert.assertTrue(actualContentProviderDataAccessor instanceof ElibDataAccessor);
    }

    @Test
    public void getElibUDataAccessor() {
        givenElibUAsContentProviderName();
        whenGetContentProviderDataAccessor();
        thenElibUDataAccessorIsReturned();
    }

    private void givenElibUAsContentProviderName() {
        contentProviderName = ContentProviderName.ELIBU;
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
        contentProviderName = ContentProviderName.PUBLIT;
    }

    private void thenPublitDataAccessorIsReturned() {
        Assert.assertTrue(actualContentProviderDataAccessor instanceof PublitDataAccessor);
    }

    @Test
    public void getAskewsDataAccessor() {
        givenAskewsAsContentProviderName();
        whenGetContentProviderDataAccessor();
        thenAskewsDataAccessorIsReturned();
    }

    private void givenAskewsAsContentProviderName() {
        contentProviderName = ContentProviderName.ASKEWS;
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
        contentProviderName = ContentProviderName.OVERDRIVE;
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
        contentProviderName = ContentProviderName.ELIB3;
    }

    private void thenElib3DataAccessorIsReturned() {
        Assert.assertTrue(actualContentProviderDataAccessor instanceof Elib3DataAccessor);
    }
}
