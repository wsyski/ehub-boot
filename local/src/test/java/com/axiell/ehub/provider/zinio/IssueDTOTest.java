package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.DTOTestFixture;

public class IssueDTOTest extends DTOTestFixture<IssueDTO> {

    @Override
    protected IssueDTO getTestInstance() {
        return new IssueDTO("id", "title", "imageUrl");
    }

    @Override
    protected Class<IssueDTO> getTestClass() {
        return IssueDTO.class;
    }
}
