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
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import static com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;
import static com.axiell.ehub.provider.record.format.ContentDisposition.DOWNLOADABLE;
import static com.axiell.ehub.provider.record.format.ContentDisposition.STREAMING;

@Component
public class TestDataResource implements ITestDataResource {

    @Autowired
    private IContentProviderAdminController contentProviderAdminController;
    @Autowired
    private IFormatAdminController formatAdminController;
    @Autowired
    private IConsumerAdminController consumerAdminController;
    @Autowired
    private IEhubLoanRepository ehubLoanRepository;
    @Autowired
    private ILanguageAdminController languageAdminController;
    @Autowired
    private IAliasAdminController aliasAdminController;


    @Override
    public TestData init() {
        saveAlias(TestDataConstants.CONTENT_PROVIDER_TEST_EP, TestDataConstants.CONTENT_PROVIDER_TEST_EP);
        saveAlias("Distribut\u00f6r: "+TestDataConstants.CONTENT_PROVIDER_TEST_EP, TestDataConstants.CONTENT_PROVIDER_TEST_EP);
        initLanguage();
        final ContentProvider contentProvider = initContentProvider();
        final EhubConsumer ehubConsumer = initEhubConsumer();
        initContentProviderConsumer(ehubConsumer, contentProvider);
        final Long ehubLoanId = initEhubLoan(ehubConsumer, contentProvider);
        return new TestData(ehubConsumer.getId(), TestDataConstants.EHUB_CONSUMER_SECRET_KEY, ehubLoanId, TestDataConstants.PATRON_ID,
                TestDataConstants.LIBRARY_CARD, TestDataConstants.PIN);
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

    private ContentProvider initContentProvider() {
        Map<ContentProvider.ContentProviderPropertyKey, String> contentProviderProperties = new HashMap<>();
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.API_BASE_URL, TestDataConstants.TEST_EP_API_BASE_URL);
        ContentProvider contentProvider = new ContentProvider(TestDataConstants.CONTENT_PROVIDER_TEST_EP, contentProviderProperties);
        contentProvider = contentProviderAdminController.save(contentProvider);

        Map<String, FormatDecoration> formatDecorations = new HashMap<>();
        FormatDecoration formatDecoration0 =
                new FormatDecoration(contentProvider, TestDataConstants.TEST_EP_FORMAT_0_ID, DOWNLOADABLE, TestDataConstants.TEST_EP_PLAYER_WIDTH,
                        TestDataConstants.TEST_EP_PLAYER_HEIGHT);
        FormatDecoration formatDecoration1 =
                new FormatDecoration(contentProvider, TestDataConstants.TEST_EP_FORMAT_1_ID, STREAMING, TestDataConstants.TEST_EP_PLAYER_WIDTH,
                        TestDataConstants.TEST_EP_PLAYER_HEIGHT);
        FormatDecoration formatDecoration2 =
                new FormatDecoration(contentProvider, TestDataConstants.TEST_EP_FORMAT_2_ID, DOWNLOADABLE, TestDataConstants.TEST_EP_PLAYER_WIDTH,
                        TestDataConstants.TEST_EP_PLAYER_HEIGHT);

        formatDecorations.put(TestDataConstants.TEST_EP_FORMAT_0_ID, formatDecoration0);
        formatDecorations.put(TestDataConstants.TEST_EP_FORMAT_1_ID, formatDecoration1);
        formatDecorations.put(TestDataConstants.TEST_EP_FORMAT_2_ID, formatDecoration2);
        for (Map.Entry<String, FormatDecoration> entry : formatDecorations.entrySet()) {
            FormatDecoration value = formatAdminController.save(entry.getValue());
            formatDecorations.put(entry.getKey(), value);
        }
        contentProvider.setFormatDecorations(formatDecorations);
        contentProvider = contentProviderAdminController.save(contentProvider);
        return contentProvider;
    }

    private EhubConsumer initEhubConsumer() {
        EhubConsumer ehubConsumer = createEhubConsumer();
        ehubConsumer = consumerAdminController.save(ehubConsumer);
        ehubConsumer.setContentProviderConsumers(new HashSet<ContentProviderConsumer>());
        return ehubConsumer;
    }

    private EhubConsumer createEhubConsumer() {
        Map<EhubConsumerPropertyKey, String> properties = new HashMap<>();
        properties.put(EhubConsumerPropertyKey.ARENA_PALMA_URL, TestDataConstants.ARENA_PALMA_URL);
        properties.put(EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, TestDataConstants.ARENA_AGENCY_M_IDENTIFIER);
        return new EhubConsumer("Ehub Consumer Description", TestDataConstants.EHUB_CONSUMER_SECRET_KEY, properties, TestDataConstants.DEFAULT_LANGUAGE);
    }

    private ContentProviderConsumer initContentProviderConsumer(EhubConsumer ehubConsumer, ContentProvider contentProvider) {
        Map<ContentProviderConsumerPropertyKey, String> properties = new HashMap<>();
        properties.put(ContentProviderConsumerPropertyKey.EP_SECRET_KEY, TestDataConstants.TEST_EP_SECRET_KEY);
        properties.put(ContentProviderConsumerPropertyKey.EP_SITE_ID, TestDataConstants.TEST_EP_SITE_ID);
        ContentProviderConsumer contentProviderConsumer = new ContentProviderConsumer(ehubConsumer, contentProvider, properties);
        contentProviderConsumer = consumerAdminController.save(contentProviderConsumer);
        ehubConsumer.getContentProviderConsumers().add(contentProviderConsumer);
        consumerAdminController.save(ehubConsumer);
        return contentProviderConsumer;
    }

    private Long initEhubLoan(EhubConsumer ehubConsumer, ContentProvider contentProvider) {
        FormatDecoration formatDecoration1 = contentProvider.getFormatDecoration(TestDataConstants.TEST_EP_FORMAT_1_ID);
        ContentProviderLoanMetadata contentProviderLoanMetadata = new ContentProviderLoanMetadata.Builder(contentProvider, new Date(),
                TestDataConstants.TEST_EP_RECORD_1_ID, formatDecoration1).contentProviderLoanId(TestDataConstants.CONTENT_PROVIDER_LOAN_ID).build();
        LmsLoan lmsLoan = new LmsLoan(TestDataConstants.LMS_LOAN_ID);
        EhubLoan ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
        ehubLoan = ehubLoanRepository.save(ehubLoan);
        return ehubLoan.getId();
    }
}
