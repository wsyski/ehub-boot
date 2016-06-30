package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.util.EhubUrlCodec;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ZinioFacade implements IZinioFacade {
    private final static int PASSWORD_LEN = 8;
    private static final String NA = "N/A";

    @Override
    public String login(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        final IZinioResource zinioResource = ZinioResourceFactory.create(contentProviderConsumer);
        final String email = patron.getEmail();
        final String libraryId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_LIB_ID);
        final String token = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_TOKEN);
        try {
            new ZinioResponse(zinioResource.patronExists(IZinioResource.CMD_P_EXISTS, libraryId, token, email));
        } catch (InternalServerErrorException ex) {
            String password = getPassword();
            String libraryCard = patron.getLibraryCard();
            new ZinioResponse(zinioResource.addPatron(IZinioResource.CMD_P_ACCOUNT_CREATE, libraryId, token, email, password, password, NA, NA, libraryCard));

        }
        final ZinioResponse zinioResponse = new ZinioResponse(zinioResource.login(IZinioResource.CMD_P_LOGIN, libraryId, token, email));
        return zinioResponse.getMessage();
    }

    @Override
    public void checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderIssueId) {
        final IZinioResource zinioResource = ZinioResourceFactory.create(contentProviderConsumer);
        final String email = patron.getEmail();
        final String libraryId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_LIB_ID);
        final String token = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_TOKEN);
        new ZinioResponse(zinioResource.checkout(IZinioResource.CMD_ZINIO_CHECKOUT_ISSUE, libraryId, token, email, contentProviderIssueId));
    }

    @Override
    public List<IssueDTO> getIssues(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId) {
        final IZinioResource zinioResource = ZinioResourceFactory.create(contentProviderConsumer);
        final String libraryId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_LIB_ID);
        final String token = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_TOKEN);
        ZinioResponse zinioResponse =
                new ZinioResponse(zinioResource.getIssues(IZinioResource.CMD_ZINIO_ISSUES_BY_MAGAZINES_AND_LIBRARY, libraryId, token, contentProviderRecordId));
        return zinioResponse.getObject(List.class);
    }

    @Override
    public String getDownloadUrl(final String loginUrl, final String contentProviderIssueId) {
        return loginUrl + "&url=http://www.rbdigitaltest.com/zinio/proxy/?zinio_issue_id=" + EhubUrlCodec.encode(contentProviderIssueId);
    }

    private String getPassword() {
        return RandomStringUtils.random(PASSWORD_LEN, true, true);
    }
}
