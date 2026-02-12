package com.axiell.ehub.common.provider.record.issue;

import com.axiell.ehub.common.DTOTestFixture;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.IssueDTO;

public class PeriodicalIssueDTOTest extends DTOTestFixture<IssueDTO> {

    @Override
    protected IssueDTO getTestInstance() {
        return IssueBuilder.periodicalIssue().toDTO();
    }

    @Override
    protected Class<IssueDTO> getTestClass() {
        return IssueDTO.class;
    }
}
