package com.axiell.ehub.test;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.language.ILanguageAdminController;
import com.axiell.ehub.language.Language;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.loan.IEhubLoanRepository;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.alias.Alias;
import com.axiell.ehub.provider.alias.AliasMapping;
import com.axiell.ehub.provider.alias.IAliasAdminController;
import com.axiell.ehub.provider.ep.EpUserIdValue;
import com.axiell.ehub.provider.platform.Platform;
import com.axiell.ehub.provider.record.format.ContentDisposition;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import com.axiell.ehub.provider.record.platform.IPlatformAdminController;
import com.axiell.ehub.provider.zinio.ZinioDataAccessor;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Component
public class TestDataResource implements ITestDataResource {
    private static final Map<String, Boolean> HAS_ISSUES = ImmutableMap.<String, Boolean>builder()
            .put(TestDataConstants.CONTENT_PROVIDER_TEST_EP, false)
            .put(ContentProvider.CONTENT_PROVIDER_ZINIO, true).build();
    private static final Map<String, List<String>> FORMAT_IDS = ImmutableMap.<String, List<String>>builder()
            .put(TestDataConstants.CONTENT_PROVIDER_TEST_EP,
                    Lists.newArrayList(TestDataConstants.TEST_EP_FORMAT_0_ID, TestDataConstants.TEST_EP_FORMAT_1_ID, TestDataConstants.TEST_EP_FORMAT_2_ID))
            .put(ContentProvider.CONTENT_PROVIDER_ZINIO, Lists.newArrayList(ZinioDataAccessor.ZINIO_FORMAT_0_ID)).build();

    private static final Map<String, Map<ContentProviderConsumer.ContentProviderConsumerPropertyKey, String>> CONTENT_PROVIDER_CONSUMER_PROPERTIES =
            ImmutableMap.<String, Map<ContentProviderConsumer.ContentProviderConsumerPropertyKey, String>>builder()
                    .put(TestDataConstants.CONTENT_PROVIDER_TEST_EP, ImmutableMap.<ContentProviderConsumer.ContentProviderConsumerPropertyKey, String>builder()
                            .put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY, TestDataConstants.TEST_EP_SECRET_KEY)
                            .put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID, TestDataConstants.TEST_EP_SITE_ID)
                            .put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_USER_ID_VALUE, EpUserIdValue.PATRON_ID.name()).build())
                    .put(ContentProvider.CONTENT_PROVIDER_ZINIO, ImmutableMap.<ContentProviderConsumer.ContentProviderConsumerPropertyKey, String>builder()
                            .put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_LIB_ID, TestDataConstants.ZINIO_LIB_ID)
                            .put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ZINIO_TOKEN, TestDataConstants.ZINIO_TOKEN).build())
                    .build();
    private static final Map<String, Map<ContentProvider.ContentProviderPropertyKey, String>> CONTENT_PROVIDER_PROPERTIES =
            ImmutableMap.<String, Map<ContentProvider.ContentProviderPropertyKey, String>>builder()
                    .put(TestDataConstants.CONTENT_PROVIDER_TEST_EP, ImmutableMap.<ContentProvider.ContentProviderPropertyKey, String>builder()
                            .put(ContentProvider.ContentProviderPropertyKey.API_BASE_URL, TestDataConstants.TEST_EP_API_BASE_URL).build())
                    .put(ContentProvider.CONTENT_PROVIDER_ZINIO, ImmutableMap.<ContentProvider.ContentProviderPropertyKey, String>builder()
                            .put(ContentProvider.ContentProviderPropertyKey.API_BASE_URL, TestDataConstants.ZINIO_API_BASE_URL)
                            .put(ContentProvider.ContentProviderPropertyKey.LOAN_EXPIRATION_DAYS, String.valueOf(TestDataConstants.ZINIO_LOAN_EXPIRATION_DAYS))
                            .build())
                    .build();
    private static final Map<EhubConsumer.EhubConsumerPropertyKey, String> EHUB_CONSUMER_PROPERTIES =
            ImmutableMap.<EhubConsumer.EhubConsumerPropertyKey, String>builder()
                    .put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, TestDataConstants.ARENA_PALMA_URL)
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

    @Override
    public TestData init(final String contentProviderName, final boolean isLoanPerProduct) {
        saveAlias(contentProviderName, contentProviderName);
        saveAlias(TestDataConstants.CONTENT_PROVIDER_ALIAS_PREFIX + contentProviderName, contentProviderName);
        initLanguage();
        final EhubConsumer ehubConsumer = initEhubConsumer();
        final ContentProvider contentProvider = initContentProvider(contentProviderName, isLoanPerProduct);
        initContentProviderConsumer(ehubConsumer, contentProvider);
        final long ehubLoanId = initEhubLoan(ehubConsumer, contentProvider);
        return new TestData(ehubConsumer.getId(), TestDataConstants.EHUB_CONSUMER_SECRET_KEY, ehubLoanId, TestDataConstants.PATRON_ID,
                TestDataConstants.LIBRARY_CARD, TestDataConstants.PIN, TestDataConstants.EMAIL);
    }

    @Override
    public Response delete() {
        final DataSource dataSource = getDataSource();

        try (Connection connection = dataSource.getConnection()) {
            executeStatement(connection);
            return Response.ok().entity("Database successfully restarted").build();
        } catch (SQLException e) {
            return Response.serverError().entity("Could not restart database: " + e.getMessage()).build();
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

    private DataSource getDataSource() {
        @SuppressWarnings("resource")
        ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[]{"/com/axiell/ehub/data-source-context.xml"});
        return (DataSource) springContext.getBean("dataSource");
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
                .save(new EhubConsumer(TestDataConstants.EHUB_CONSUMER_DESCRIPTION, TestDataConstants.EHUB_CONSUMER_SECRET_KEY, EHUB_CONSUMER_PROPERTIES,
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
                TestDataConstants.RECORD_1_ID, formatDecoration1).contentProviderIssueId(hasIssues ? TestDataConstants.ISSUE_0_ID : null)
                .contentProviderLoanId(TestDataConstants.CONTENT_PROVIDER_LOAN_ID).build();
        LmsLoan lmsLoan = new LmsLoan(TestDataConstants.LMS_LOAN_ID);
        EhubLoan ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
        ehubLoan = ehubLoanRepository.save(ehubLoan);
        return ehubLoan.getId();
    }
}
