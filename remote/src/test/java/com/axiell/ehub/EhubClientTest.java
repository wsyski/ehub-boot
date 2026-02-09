package com.axiell.ehub;

import com.axiell.ehub.controller.external.IRootResource;
import com.axiell.ehub.controller.external.v5_0.IV5_0_Resource;
import com.axiell.ehub.controller.external.v5_0.provider.IContentProvidersResource;
import com.axiell.ehub.controller.external.v5_0.provider.IRecordsResource;
import com.axiell.ehub.provider.record.Record;
import com.axiell.ehub.controller.external.v5_0.provider.dto.RecordDTO;
import com.axiell.authinfo.AuthInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    private IV5_0_Resource iV5_0_Resource;

    @Mock
    private IRecordsResource recordsResource;

    @Mock
    private IContentProvidersResource contentProvidersResource;

    @Mock
    private RecordDTO recordDTO;

    @BeforeEach
    public void setUp() {
        underTest = new EhubClient();
        underTest.setRootResource(rootResource);
    }

    @Test
    public void getRecord() throws EhubException {
        givenRootResourceReturnsV5_0_Resource();
        givenV5_0_ResourceReturnsContentProviderResource();
        givenContentProviderResourceReturnsRecordsResource();
        givenRecordsResourceReturnsRecord();
        Record record = whenGetRecordExecuted();
        thenContentProvidersResourceIsCalledWithEncodedAlias(record);
    }

    private void thenContentProvidersResourceIsCalledWithEncodedAlias(Record record) {
        Assertions.assertSame(recordDTO, record.toDTO());
        verify(contentProvidersResource, times(1)).getRecordsResource(CONTENT_PROVIDER_ALIAS);
    }

    private Record whenGetRecordExecuted() throws EhubException {
        return underTest.getRecord(authInfo, CONTENT_PROVIDER_ALIAS, CONTENT_PROVIDER_RECORD_ID, LANGUAGE);
    }

    private void givenRecordsResourceReturnsRecord() {
        given(recordsResource.getRecord(authInfo, CONTENT_PROVIDER_RECORD_ID, LANGUAGE)).willReturn(recordDTO);
    }

    private void givenContentProviderResourceReturnsRecordsResource() {
        given(contentProvidersResource.getRecordsResource(CONTENT_PROVIDER_ALIAS)).willReturn(recordsResource);
    }

    private void givenRootResourceReturnsV5_0_Resource() {
        given(rootResource.getIV5_0_Resource()).willReturn(iV5_0_Resource);
    }

    private void givenV5_0_ResourceReturnsContentProviderResource() {
        given(iV5_0_Resource.contentProviders()).willReturn(contentProvidersResource);
    }
}
