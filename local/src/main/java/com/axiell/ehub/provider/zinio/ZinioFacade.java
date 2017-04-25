package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.security.UnauthorizedException;
import com.axiell.ehub.util.EhubUrlCodec;
import com.axiell.ehub.util.PatronUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ZinioFacade implements IZinioFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZinioFacade.class);

    @Autowired
    private IZinioResponseFactory zinioResponseFactory;

    private final static int PASSWORD_LEN = 8;
    private static final String NA = "N/A";

    @Override
    public String login(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language) {
        final IZinioResource zinioResource = ZinioResourceFactory.create(contentProviderConsumer);
        final String email = PatronUtil.getMandatoryEmail(patron);
        final String libraryId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_LIB_ID);
        final String token = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_TOKEN);
        try {
            String response = zinioResource.patronExists(IZinioResource.CMD_P_EXISTS, libraryId, token, email);
            createZinioResponse(contentProviderConsumer, language, response);
        } catch (InternalServerErrorException ex) {
            final String password = getPassword();
            final String libraryCard = patron.getLibraryCard();
            String response = zinioResource.addPatron(IZinioResource.CMD_P_ACCOUNT_CREATE, libraryId, token, email, password, password, NA, NA, libraryCard);
            createZinioResponse(contentProviderConsumer, language, response);
        }
        String response = zinioResource.login(IZinioResource.CMD_P_LOGIN, libraryId, token, email);
        IZinioResponse zinioResponse = createZinioResponse(contentProviderConsumer, language, response);
        return zinioResponse.getMessage();
    }

    @Override
    public void checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String issueId,
                         final String language) {
        final IZinioResource zinioResource = ZinioResourceFactory.create(contentProviderConsumer);
        final String email = PatronUtil.getMandatoryEmail(patron);
        final String libraryId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_LIB_ID);
        final String token = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_TOKEN);
        String response = zinioResource.checkout(IZinioResource.CMD_ZINIO_CHECKOUT_ISSUE, libraryId, token, email, issueId);
        createZinioResponse(contentProviderConsumer, language, response);
    }

    @Override
    public List<IssueDTO> getIssues(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId, final String language) {
        final IZinioResource zinioResource = ZinioResourceFactory.create(contentProviderConsumer);
        final String libraryId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_LIB_ID);
        final String token = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_TOKEN);
        String response = zinioResource.getIssues(IZinioResource.CMD_ZINIO_ISSUES_BY_MAGAZINES_AND_LIBRARY, libraryId, token, contentProviderRecordId);
        IZinioResponse zinioResponse = createZinioResponse(contentProviderConsumer, language, response);
        return zinioResponse.getAsList(IssueDTO.class);
    }

    @Override
    public String getContentUrl(final String loginUrl, final String issueId) {
        if (StringUtils.isBlank(issueId)) {
           throw createInternalServerErrorException("Blank issueId");
        }
        return loginUrl + "&url=http://www.rbdigitaltest.com/zinio/proxy/?zinio_issue_id=" + EhubUrlCodec.authInfoEncode(issueId);
    }

    private IZinioResponse createZinioResponse(final ContentProviderConsumer contentProviderConsumer, final String language, final String response) {
        LOGGER.info(response);
        return zinioResponseFactory.create(response, contentProviderConsumer, language);
    }

    private String getPassword() {
        return "a" + RandomStringUtils.randomAlphanumeric(PASSWORD_LEN) + "0";
    }

    private InternalServerErrorException createInternalServerErrorException(String message) {
        return new InternalServerErrorException(message, ErrorCause.INTERNAL_SERVER_ERROR);
    }
}
