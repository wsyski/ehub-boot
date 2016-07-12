package com.axiell.ehub.provider.record.issue;

import com.axiell.ehub.DTOTestFixture;

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
