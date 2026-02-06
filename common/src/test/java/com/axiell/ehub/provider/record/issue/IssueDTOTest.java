package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.controller.external.v5_0.provider.dto.IssueDTO;

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
