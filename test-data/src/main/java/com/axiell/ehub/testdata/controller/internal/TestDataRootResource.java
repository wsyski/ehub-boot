package com.axiell.ehub.testdata.controller.internal;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.local.consumer.IConsumerAdminController;
import com.axiell.ehub.local.language.ILanguageAdminController;
import com.axiell.ehub.common.language.Language;
import com.axiell.ehub.local.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.local.loan.EhubLoan;
import com.axiell.ehub.local.loan.IEhubLoanRepository;
import com.axiell.ehub.local.loan.LmsLoan;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.local.provider.IContentProviderAdminController;
import com.axiell.ehub.common.provider.alias.Alias;
import com.axiell.ehub.common.provider.alias.AliasMapping;
import com.axiell.ehub.local.provider.alias.IAliasAdminController;
import com.axiell.ehub.common.provider.platform.Platform;
import com.axiell.ehub.common.provider.record.format.ContentDisposition;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.provider.record.format.IFormatAdminController;
import com.axiell.ehub.local.provider.record.platform.IPlatformAdminController;
import com.axiell.ehub.testdata.TestDataConstants;
import com.axiell.ehub.testdata.controller.internal.dto.TestDataDTO;
import com.axiell.ehub.common.test.TestSecretKeyConstants;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class TestDataRootResource implements ITestDataRootResource {
    private static final Map<String, Boolean> HAS_ISSUES = ImmutableMap.<String, Boolean>builder()
            .put(TestDataConstants.CONTENT_PROVIDER_TEST_EP, false).build();
    private static final Map<String, List<String>> FORMAT_IDS = ImmutableMap.<String, List<String>>builder()
            .put(TestDataConstants.CONTENT_PROVIDER_TEST_EP,
                    Lists.newArrayList(TestDataConstants.TEST_EP_FORMAT_ID_0, TestDataConstants.TEST_EP_FORMAT_ID_1, TestDataConstants.TEST_EP_FORMAT_ID_2)).build();

    private static final Map<String, Map<ContentProviderConsumer.ContentProviderConsumerPropertyKey, String>> CONTENT_PROVIDER_CONSUMER_PROPERTIES =
            ImmutableMap.<String, Map<ContentProviderConsumer.ContentProviderConsumerPropertyKey, String>>builder()
                    .put(TestDataConstants.CONTENT_PROVIDER_TEST_EP, ImmutableMap.<ContentProviderConsumer.ContentProviderConsumerPropertyKey, String>builder()
                            .put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY, TestDataConstants.TEST_EP_SECRET_KEY)
                            .put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID, TestDataConstants.TEST_EP_SITE_ID)
                            .put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_TOKEN_EXPIRATION_TIME_IN_SECONDS, String.valueOf(TestDataConstants.TEST_EP_TOKEN_EXPIRATION_TIME_IN_SECONDS))
                            .put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_TOKEN_LEEWAY_IN_SECONDS, String.valueOf(TestDataConstants.TEST_EP_TOKEN_LEEWAY_IN_SECONDS)).build())

                    .build();
    private static final Map<String, Map<ContentProvider.ContentProviderPropertyKey, String>> CONTENT_PROVIDER_PROPERTIES =
            ImmutableMap.<String, Map<ContentProvider.ContentProviderPropertyKey, String>>builder()
                    .put(TestDataConstants.CONTENT_PROVIDER_TEST_EP, ImmutableMap.<ContentProvider.ContentProviderPropertyKey, String>builder()
                            .put(ContentProvider.ContentProviderPropertyKey.API_BASE_URL, TestDataConstants.TEST_EP_API_BASE_URL).build())
                    .build();
    private static final Map<EhubConsumer.EhubConsumerPropertyKey, String> EHUB_CONSUMER_PROPERTIES =
            ImmutableMap.<EhubConsumer.EhubConsumerPropertyKey, String>builder()
                    .put(EhubConsumer.EhubConsumerPropertyKey.ARENA_LOCAL_API_ENDPOINT, TestDataConstants.ARENA_LOCAL_API_ENDPOINT)
                    .put(EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, TestDataConstants.ARENA_AGENCY_M_IDENTIFIER).build();

    @Autowired
    private IContentProviderAdminController contentProviderAdminController;
    @Autowired
    private IFormatAdminController formatAdminController;
    @Autowired
    private IPlatformAdminController platformAdminController;
    @Autowired
    private IConsumerAdminController consumerAdminController;
    @Autowired
    private IEhubLoanRepository ehubLoanRepository;
    @Autowired
    private ILanguageAdminController languageAdminController;
    @Autowired
    private IAliasAdminController aliasAdminController;
    @Autowired
    private  DataSource dataSource;

    @Override
    public TestDataDTO init(final String contentProviderName, final boolean isLoanPerProduct) {
        log.info("Initializing testdata data for content provider: {}, isLoanPerProduct: {}", contentProviderName, isLoanPerProduct);
        log.info("contentProviderAdminController: {}, formatAdminController: {} platformAdminController: {}, consumerAdminController: {}, ehubLoanRepository: {}, languageAdminController: {}, aliasAdminController: {}", contentProviderAdminController, formatAdminController, platformAdminController, consumerAdminController, ehubLoanRepository, languageAdminController, aliasAdminController);
        saveAlias(contentProviderName, contentProviderName);
        saveAlias(TestDataConstants.CONTENT_PROVIDER_ALIAS_PREFIX + contentProviderName, contentProviderName);
        initLanguage();
        final EhubConsumer ehubConsumer = initEhubConsumer();
        final ContentProvider contentProvider = initContentProvider(contentProviderName, isLoanPerProduct);
        initContentProviderConsumer(ehubConsumer, contentProvider);
        final long ehubLoanId = initEhubLoan(ehubConsumer, contentProvider);
        return new TestDataDTO(ehubConsumer.getId(), TestSecretKeyConstants.SECRET_KEY, ehubLoanId, TestDataConstants.PATRON_ID,
                TestDataConstants.LIBRARY_CARD, TestDataConstants.PIN, TestDataConstants.EMAIL, TestDataConstants.NAME, TestDataConstants.BIRTH_DATE);
    }

    @Override
    public Response delete() {
        try (Connection connection = dataSource.getConnection()) {
            executeStatement(connection);
            return Response.ok().entity("Database cleared").build();
        } catch (SQLException e) {
            return Response.serverError().entity("Could not clear database: " + e.getMessage()).build();
        }
    }

    private void saveAlias(final String aliasValue, final String name) {
        final Alias alias = Alias.newInstance(aliasValue);
        final AliasMapping aliasMapping = new AliasMapping();
        aliasMapping.setAlias(alias);
        aliasMapping.setName(name);
        aliasAdminController.save(aliasMapping);
    }

    private void executeStatement(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
            connection.commit();
            connection.close();
        }
    }

    private void initLanguage() {
        languageAdminController.save(new Language(TestDataConstants.DEFAULT_LANGUAGE));
    }

    private Set<Platform> initPlatforms() {
        return Sets.newHashSet(platformAdminController.save(new Platform(TestDataConstants.PLATFORM_DESKTOP)),
                platformAdminController.save(new Platform(TestDataConstants.PLATFORM_IOS)),
                platformAdminController.save(new Platform(TestDataConstants.PLATFORM_ANDROID)));
    }

    private Map<String, FormatDecoration> initFormatDecorations(final ContentProvider contentProvider) {
        final Set<Platform> platforms = initPlatforms();
        final Map<String, FormatDecoration> formatDecorations = new HashMap<>();
        final String contentProviderName = contentProvider.getName();
        FORMAT_IDS.get(contentProviderName).forEach(formatId -> {
            FormatDecoration formatDecoration = formatAdminController.save(new FormatDecoration(contentProvider, formatId, ContentDisposition.DOWNLOADABLE,
                    platforms));
            formatDecorations.put(formatId, formatDecoration);
        });
        return formatDecorations;
    }

    private ContentProvider initContentProvider(final String contentProviderName, final boolean isLoanPerProduct) {
        final Map<ContentProvider.ContentProviderPropertyKey, String> contentProviderProperties = CONTENT_PROVIDER_PROPERTIES.get(contentProviderName);
        final ContentProvider contentProvider = contentProviderAdminController.save(new ContentProvider(contentProviderName,
                contentProviderProperties));
        final Map<String, FormatDecoration> formatDecorations = initFormatDecorations(contentProvider);
        contentProvider.setFormatDecorations(formatDecorations);
        contentProvider.setLoanPerProduct(isLoanPerProduct);
        return contentProviderAdminController.save(contentProvider);
    }

    private EhubConsumer initEhubConsumer() {
        return consumerAdminController
                .save(new EhubConsumer(TestDataConstants.EHUB_CONSUMER_DESCRIPTION, TestSecretKeyConstants.SECRET_KEY, EHUB_CONSUMER_PROPERTIES,
                        TestDataConstants.DEFAULT_LANGUAGE));
    }

    private ContentProviderConsumer initContentProviderConsumer(final EhubConsumer ehubConsumer, final ContentProvider contentProvider) {
        final String contentProviderName = contentProvider.getName();
        Map<ContentProviderConsumer.ContentProviderConsumerPropertyKey, String> contentProviderConsumerProperties =
                CONTENT_PROVIDER_CONSUMER_PROPERTIES.get(contentProviderName);
        final ContentProviderConsumer contentProviderConsumer =
                consumerAdminController.save(new ContentProviderConsumer(ehubConsumer, contentProvider, contentProviderConsumerProperties));
        ehubConsumer.getContentProviderConsumers().add(contentProviderConsumer);
        consumerAdminController.save(ehubConsumer);
        return contentProviderConsumer;
    }

    private Long initEhubLoan(final EhubConsumer ehubConsumer, final ContentProvider contentProvider) {
        final String contentProviderName = contentProvider.getName();
        final boolean hasIssues = HAS_ISSUES.get(contentProviderName);
        String contentProviderFormatId = FORMAT_IDS.get(contentProviderName).iterator().next();
        FormatDecoration formatDecoration1 = contentProvider.getFormatDecoration(contentProviderFormatId);
        ContentProviderLoanMetadata contentProviderLoanMetadata = new ContentProviderLoanMetadata.Builder(contentProvider, new Date(),
                TestDataConstants.RECORD_ID_1, formatDecoration1).issueId(hasIssues ? TestDataConstants.ISSUE_ID_0 : null)
                .issueTitle(hasIssues ? TestDataConstants.ISSUE_TITLE_0 : null).contentProviderLoanId(TestDataConstants.CONTENT_PROVIDER_LOAN_ID).build();
        LmsLoan lmsLoan = new LmsLoan(TestDataConstants.LMS_LOAN_ID);
        EhubLoan ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
        ehubLoan = ehubLoanRepository.save(ehubLoan);
        return ehubLoan.getId();
    }
}
