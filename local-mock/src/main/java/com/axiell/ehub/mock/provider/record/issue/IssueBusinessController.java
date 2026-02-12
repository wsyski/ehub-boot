package com.axiell.ehub.mock.provider.record.issue;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.ErrorCauseArgumentType;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.IssueDTO;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.RecordDTO;
import com.axiell.ehub.common.provider.record.issue.Issue;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.core.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.mock.AbstractBusinessController;
import com.axiell.ehub.mock.util.EhubMessageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IssueBusinessController extends AbstractBusinessController implements IIssueBusinessController {

    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Autowired
    private EhubMessageUtility ehubMessageUtility;

    @Override
    public List<Issue> getIssues(final AuthInfo authInfo, final String contentProviderName, final String contentProviderRecordId, final String language) {
        Patron patron = authInfo.getPatron();
        RecordDTO recordDTO = ehubMessageUtility.getEhubMessage(RecordDTO.class, "record", contentProviderName, contentProviderRecordId,
                patron.getLibraryCard());
        if (recordDTO == null) {
            ContentProviderConsumer contentProviderConsumer = getContentProviderConsumer(contentProviderName);
            throw ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer,
                    ErrorCauseArgumentType.PRODUCT_UNAVAILABLE, language);
        }
        return Arrays.stream(recordDTO.getIssues().toArray(new IssueDTO[0])).map(Issue::new).collect(Collectors.toList());
    }
}
