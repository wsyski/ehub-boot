package com.axiell.ehub;

import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.provider.record.RecordDTO;
import com.axiell.authinfo.AuthInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class EhubClientTest {
    private EhubClient underTest;
    private static final String CONTENT_PROVIDER_ALIAS = "content provider alias";
    private static final String LANGUAGE = "en";
    private static final String CONTENT_PROVIDER_RECORD_ID = "contentProviderRecordId";

    @Mock
    private AuthInfo authInfo;

    @Mock
    private IRootResource rootResource;

    @Mock
    private IRecordsResource recordsResource;

    @Mock
    private IContentProvidersResource contentProvidersResource;

    @Mock
    private RecordDTO recordDTO;

    @Before
    public void setUp() {
        underTest = new EhubClient();
        underTest.setRootResource(rootResource);
    }

    @Test
    public void getRecord() throws EhubException {
        givenRootResourceReturnsContentProviderResource();
        givenContentProviderResourceReturnsRecordsResource();
        givenRecordsResourceReturnsRecord();
        Record record = whenGetRecordExecuted();
        thenContentProvidersResourceIsCalledWithEncodedAlias(record);
    }

    private void thenContentProvidersResourceIsCalledWithEncodedAlias(Record record) {
        assertSame(recordDTO, record.toDTO());
        verify(contentProvidersResource, times(1)).records(CONTENT_PROVIDER_ALIAS);
    }

    private Record whenGetRecordExecuted() throws EhubException {
        return underTest.getRecord(authInfo, CONTENT_PROVIDER_ALIAS, CONTENT_PROVIDER_RECORD_ID, LANGUAGE);
    }

    private void givenRecordsResourceReturnsRecord() {
        given(recordsResource.getRecord(authInfo, CONTENT_PROVIDER_RECORD_ID, LANGUAGE)).willReturn(recordDTO);
    }

    private void givenContentProviderResourceReturnsRecordsResource() {
        given(contentProvidersResource.records(CONTENT_PROVIDER_ALIAS)).willReturn(recordsResource);
    }

    private void givenRootResourceReturnsContentProviderResource() {
        given(rootResource.contentProviders()).willReturn(contentProvidersResource);
    }
}
