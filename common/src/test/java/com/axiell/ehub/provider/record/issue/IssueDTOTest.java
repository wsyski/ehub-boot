package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.provider.record.format.FormatBuilder;
import com.axiell.ehub.provider.record.format.FormatDTO;

public class IssueDTOTest extends DTOTestFixture<IssueDTO> {

    @Override
    protected IssueDTO getTestInstance() {
        return IssueBuilder.issue().toDTO();
    }

    @Override
    protected Class<IssueDTO> getTestClass() {
        return IssueDTO.class;
    }
}
