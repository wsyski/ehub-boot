package com.axiell.ehub.test;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.loan.IEhubLoanRepository;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentProviderAdminController;
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
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_KEY;
import static com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.*;
import static com.axiell.ehub.provider.ContentProviderName.ELIB;
import static com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition.DOWNLOADABLE;
import static com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition.STREAMING;

@Component
public class TestDataResource implements ITestDataResource {
    private static final String EHUB_CONSUMER_SECRET_KEY = "secret1";
    private static final String ELIB_PRODUCT_URL = "http://localhost:16521/webservices/GetProduct.asmx/GetProduct";
    private static final String ELIB_CREATE_LOAN_URL = "http://localhost:16521/webservices/createloan.asmx/CreateLoan";
    private static final String ELIB_ORDER_LIST_URL = "http://localhost:16521/webservices/getlibraryuserorderlist.asmx/GetLibraryUserOrderList";
    private static final String ARENA_PALMA_URL = "http://localhost:16521/arena.pa.palma";
    private static final String ARENA_AGENCY_M_IDENTIFIER = "MSE000001";
    private static final String ELIB_SERVICE_KEY = "hu81K8js";
    private static final String ELIB_SERVICE_ID = "926";
    private static final String CONTENT_PROVIDER_LOAN_ID = "5279700";
    private static final String ELIB_RECORD_1_ID = "9127025500";
    private static final String LMS_LOAN_ID_1 = "04479593";
    private static final String ELIB_FORMAT_0_ID = "58";
    private static final String ELIB_FORMAT_1_ID = "71";
    private static final String ELIB_FORMAT_2_ID = "elibFormat2";
    private static final int ELIB_PLAYER_WIDTH = 600;
    private static final int ELIB_PLAYER_HEIGHT = 215;
    private static final String ELIB_LIBRARY_CARD = "909265910";
    private static final String ELIB_LIBRARY_CARD_PIN = "4447";

    @Autowired
    private IContentProviderAdminController contentProviderAdminController;
    @Autowired
    private IFormatAdminController formatAdminController;
    @Autowired
    private IConsumerAdminController consumerAdminController;
    @Autowired
    private IEhubLoanRepository ehubLoanRepository;

    @Override
    public TestData init() {
        final ContentProvider elibProvider = initElibProvider();
        final EhubConsumer ehubConsumer = initEhubConsumer();
        initElibConsumer(ehubConsumer, elibProvider);
        final Long ehubLoanId = initELibEhubLoan(ehubConsumer, elibProvider);
        return new TestData(ehubConsumer.getId(), EHUB_CONSUMER_SECRET_KEY, ehubLoanId, ELIB_LIBRARY_CARD, ELIB_LIBRARY_CARD_PIN);
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

    private void executeStatement(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
            connection.commit();
        }
    }

    private DataSource getDataSource() {
        @SuppressWarnings("resource")
        ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[]{"/com/axiell/ehub/data-source-context.xml"});
        return (DataSource) springContext.getBean("dataSource");
    }

    private ContentProvider initElibProvider() {
        Map<ContentProvider.ContentProviderPropertyKey, String> contentProviderProperties = new HashMap<>();
        contentProviderProperties.put(PRODUCT_URL, ELIB_PRODUCT_URL);
        contentProviderProperties.put(CREATE_LOAN_URL, ELIB_CREATE_LOAN_URL);
        contentProviderProperties.put(ORDER_LIST_URL, ELIB_ORDER_LIST_URL);
        ContentProvider elibProvider = new ContentProvider(ELIB, contentProviderProperties);
        elibProvider = contentProviderAdminController.save(elibProvider);

        Map<String, FormatDecoration> formatDecorations = new HashMap<>();
        FormatDecoration formatDecoration0 = new FormatDecoration(elibProvider, ELIB_FORMAT_0_ID, DOWNLOADABLE, ELIB_PLAYER_WIDTH, ELIB_PLAYER_HEIGHT);
        FormatDecoration formatDecoration1 = new FormatDecoration(elibProvider, ELIB_FORMAT_1_ID, STREAMING, ELIB_PLAYER_WIDTH, ELIB_PLAYER_HEIGHT);
        FormatDecoration formatDecoration2 = new FormatDecoration(elibProvider, ELIB_FORMAT_2_ID, STREAMING, ELIB_PLAYER_WIDTH, ELIB_PLAYER_HEIGHT);

        formatDecorations.put(ELIB_FORMAT_0_ID, formatDecoration0);
        formatDecorations.put(ELIB_FORMAT_1_ID, formatDecoration1);
        formatDecorations.put(ELIB_FORMAT_2_ID, formatDecoration2);
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
        properties.put(EhubConsumerPropertyKey.ARENA_PALMA_URL, ARENA_PALMA_URL);
        properties.put(EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, ARENA_AGENCY_M_IDENTIFIER);
        return new EhubConsumer("Ehub Consumer Description", EHUB_CONSUMER_SECRET_KEY, properties);
    }

    private ContentProviderConsumer initElibConsumer(EhubConsumer ehubConsumer, ContentProvider elibProvider) {
        Map<ContentProviderConsumerPropertyKey, String> properties = new HashMap<>();
        properties.put(ELIB_RETAILER_KEY, ELIB_SERVICE_KEY);
        properties.put(ELIB_RETAILER_ID, ELIB_SERVICE_ID);
        ContentProviderConsumer elibConsumer = new ContentProviderConsumer(ehubConsumer, elibProvider, properties);
        elibConsumer = consumerAdminController.save(elibConsumer);
        ehubConsumer.getContentProviderConsumers().add(elibConsumer);
        consumerAdminController.save(ehubConsumer);
        return elibConsumer;
    }

    private Long initELibEhubLoan(EhubConsumer ehubConsumer, ContentProvider elibProvider) {
        FormatDecoration elibFormatDecoration1 = elibProvider.getFormatDecoration(ELIB_FORMAT_1_ID);
        ContentProviderLoanMetadata contentProviderLoanMetadata = new ContentProviderLoanMetadata.Builder(elibProvider, new Date(), ELIB_RECORD_1_ID, elibFormatDecoration1).contentProviderLoanId(CONTENT_PROVIDER_LOAN_ID).build();
        LmsLoan lmsLoan = new LmsLoan(LMS_LOAN_ID_1);
        EhubLoan ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
        ehubLoan = ehubLoanRepository.save(ehubLoan);
        return ehubLoan.getId();
    }
}
