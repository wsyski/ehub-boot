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
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.alias.Alias;
import com.axiell.ehub.provider.alias.IAliasAdminController;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import com.axiell.ehub.provider.alias.AliasMapping;
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

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_KEY;
import static com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.*;
import static com.axiell.ehub.provider.ContentProviderName.ELIB;
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
        saveAlias("ELIB", ContentProviderName.ELIB);
        saveAlias("Distributör: Elib", ContentProviderName.ELIB);
        initLanguage();
        final ContentProvider elibProvider = initElibProvider();
        final EhubConsumer ehubConsumer = initEhubConsumer();
        initElibConsumer(ehubConsumer, elibProvider);
        final Long ehubLoanId = initELibEhubLoan(ehubConsumer, elibProvider);
        return new TestData(ehubConsumer.getId(), TestDataConstants.EHUB_CONSUMER_SECRET_KEY, ehubLoanId, TestDataConstants.PATRON_ID, TestDataConstants.ELIB_LIBRARY_CARD, TestDataConstants.ELIB_LIBRARY_CARD_PIN);
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

    private void saveAlias(final String aliasValue, final ContentProviderName name) {
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

    private ContentProvider initElibProvider() {
        Map<ContentProvider.ContentProviderPropertyKey, String> contentProviderProperties = new HashMap<>();
        contentProviderProperties.put(PRODUCT_URL, TestDataConstants.ELIB_PRODUCT_URL);
        contentProviderProperties.put(CREATE_LOAN_URL, TestDataConstants.ELIB_CREATE_LOAN_URL);
        contentProviderProperties.put(ORDER_LIST_URL, TestDataConstants.ELIB_ORDER_LIST_URL);
        ContentProvider elibProvider = new ContentProvider(ELIB, contentProviderProperties);
        elibProvider = contentProviderAdminController.save(elibProvider);

        Map<String, FormatDecoration> formatDecorations = new HashMap<>();
        FormatDecoration formatDecoration0 = new FormatDecoration(elibProvider, TestDataConstants.ELIB_FORMAT_0_ID, DOWNLOADABLE, TestDataConstants.ELIB_PLAYER_WIDTH, TestDataConstants.ELIB_PLAYER_HEIGHT);
        FormatDecoration formatDecoration1 = new FormatDecoration(elibProvider, TestDataConstants.ELIB_FORMAT_1_ID, STREAMING, TestDataConstants.ELIB_PLAYER_WIDTH, TestDataConstants.ELIB_PLAYER_HEIGHT);
        FormatDecoration formatDecoration2 = new FormatDecoration(elibProvider, TestDataConstants.ELIB_FORMAT_2_ID, STREAMING, TestDataConstants.ELIB_PLAYER_WIDTH, TestDataConstants.ELIB_PLAYER_HEIGHT);

        formatDecorations.put(TestDataConstants.ELIB_FORMAT_0_ID, formatDecoration0);
        formatDecorations.put(TestDataConstants.ELIB_FORMAT_1_ID, formatDecoration1);
        formatDecorations.put(TestDataConstants.ELIB_FORMAT_2_ID, formatDecoration2);
        for (Map.Entry<String, FormatDecoration> entry : formatDecorations.entrySet()) {
            FormatDecoration value = formatAdminController.save(entry.getValue());
            formatDecorations.put(entry.getKey(), value);
        }
        elibProvider.setFormatDecorations(formatDecorations);
        elibProvider = contentProviderAdminController.save(elibProvider);
        return elibProvider;
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

    private ContentProviderConsumer initElibConsumer(EhubConsumer ehubConsumer, ContentProvider elibProvider) {
        Map<ContentProviderConsumerPropertyKey, String> properties = new HashMap<>();
        properties.put(ELIB_RETAILER_KEY, TestDataConstants.ELIB_SERVICE_KEY);
        properties.put(ELIB_RETAILER_ID, TestDataConstants.ELIB_SERVICE_ID);
        ContentProviderConsumer elibConsumer = new ContentProviderConsumer(ehubConsumer, elibProvider, properties);
        elibConsumer = consumerAdminController.save(elibConsumer);
        ehubConsumer.getContentProviderConsumers().add(elibConsumer);
        consumerAdminController.save(ehubConsumer);
        return elibConsumer;
    }

    private Long initELibEhubLoan(EhubConsumer ehubConsumer, ContentProvider elibProvider) {
        FormatDecoration elibFormatDecoration1 = elibProvider.getFormatDecoration(TestDataConstants.ELIB_FORMAT_1_ID);
        ContentProviderLoanMetadata contentProviderLoanMetadata = new ContentProviderLoanMetadata.Builder(elibProvider, new Date(), TestDataConstants.ELIB_RECORD_1_ID, elibFormatDecoration1).contentProviderLoanId(
                TestDataConstants.CONTENT_PROVIDER_LOAN_ID).build();
        LmsLoan lmsLoan = new LmsLoan(TestDataConstants.LMS_LOAN_ID_1);
        EhubLoan ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
        ehubLoan = ehubLoanRepository.save(ehubLoan);
        return ehubLoan.getId();
    }
}
