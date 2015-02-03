package com.axiell.ehub;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import com.axiell.ehub.language.ILanguageAdminController;
import com.axiell.ehub.language.Language;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.ContentDisposition;
import com.axiell.ehub.provider.record.format.IFormatAdminController;

public class DevelopmentData {
    public static final String EHUB_CONSUMER_SECRET_KEY = "secret1";
    public static final String ELIB_PRODUCT_URL = "http://localhost:16521/webservices/GetProduct.asmx/GetProduct";
    public static final String ELIB_CREATE_LOAN_URL = "http://localhost:16521/webservices/createloan.asmx/CreateLoan";
    public static final String ELIB_ORDER_LIST_URL = "http://localhost:16521/webservices/getlibraryuserorderlist.asmx/GetLibraryUserOrderList";
    public static final String ARENA_PALMA_URL = "http://localhost:16521/ehub.pa.palma";
    public static final String ARENA_AGENCY_M_IDENTIFIER = "MSE000001";
    public static final String ELIB_SERVICE_KEY = "hu81K8js";
    public static final String ELIB_SERVICE_ID = "926";
    public static final String CONTENT_PROVIDER_LOAN_ID = "5279700";
    public static final String ELIB_RECORD_0_ID = "9100128260";
    public static final String ELIB_RECORD_1_ID = "9127025500";
    public static final String LMS_RECORD_ID = "0_1";
    public static final String LMS_LOAN_ID_1 = "04479593";
    public static final String LMS_LOAN_ID_2 = "4";
    public static final String ELIB_FORMAT_0_ID = "58";
    public static final String ELIB_FORMAT_0_NAME = "Epub with Adobe DRM for PC, Mac and e-book readers";
    public static final String ELIB_FORMAT_1_ID = "71";
    public static final String ELIB_FORMAT_1_NAME = "Audio book, streaming";
    public static final String ELIB_FORMAT_2_ID = "elibFormat2";
    public static final String ELIB_FORMAT_2_NAME = "elibFormat2Name";
    public static final String ELIB_LIBRARY_CARD = "909265910";
    public static final String ELIB_LIBRARY_CARD_PIN = "4447";
    public static final String ELIB_RETAIL_ORDER_NUMBER = "4820127";
    public static final int ELIB_PLAYER_WIDTH = 600;
    public static final int ELIB_PLAYER_HEIGHT = 215;
    public static final String DEFAULT_LANGUAGE = Locale.ENGLISH.getLanguage();

    protected IContentProviderAdminController contentProviderAdminController;
    protected IFormatAdminController formatAdminController;
    protected IConsumerAdminController consumerAdminController;
    protected ILanguageAdminController languageAdminController;

    // Global data
    private Long ehubConsumerId;
    private EhubConsumer ehubConsumer;
    private ContentProvider elibProvider;

    public DevelopmentData(final IContentProviderAdminController contentProviderAdminController, final IFormatAdminController formatAdminController,
                           final IConsumerAdminController consumerAdminController, final ILanguageAdminController languageAdminController) {
        this.contentProviderAdminController = contentProviderAdminController;
        this.formatAdminController = formatAdminController;
        this.consumerAdminController = consumerAdminController;
        this.languageAdminController = languageAdminController;
    }

    public void init() throws Exception {
        initLanguage();
        elibProvider = initElibProvider();
        ehubConsumer = initEhubConsumer();
        initElibConsumer(getEhubConsumer(), getElibProvider());
    }

    public void destroy() throws Exception {
        destroy(null);
    }
    
    /**
     * All table IDENTITY sequences and all SEQUENCE objects in the schema are reset to their start values, see <a
     * href="http://hsqldb.org/doc/2.0/guide/dataaccess-chapt.html#dac_truncate_statement">HSQL Documentation<a>.
     * 
     * @param dataSource
     * @throws Exception
     */
    public void destroy(DataSource dataSource) throws Exception {
        if (dataSource == null) {
            dataSource = getDataSource();
        }
        
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                executeStatement(connection);
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } finally {
            closeConnection(connection);
        }
    }

    private DataSource getDataSource() {
	@SuppressWarnings("resource")
	ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[]{"/com/axiell/ehub/data-source-context.xml"});
	return (DataSource) springContext.getBean("dataSource");
    }

    private void executeStatement(Connection connection) throws SQLException {
	Statement stmt = connection.createStatement();
	try {
	    stmt.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
	    connection.commit();
	} finally {
	    stmt.close();
	}
    }

    private void closeConnection(Connection connection) throws SQLException {
	if (connection != null) {
	    connection.close();
	}
    }
    
    public Long getEhubConsumerId() {
        return ehubConsumerId;
    }

    public EhubConsumer getEhubConsumer() {
        return ehubConsumer;
    }

    public ContentProvider getElibProvider() {
        return elibProvider;
    }

    private void initLanguage() {
        languageAdminController.save(new Language(DEFAULT_LANGUAGE));
    }
    
    private ContentProvider initElibProvider() {
        Map<ContentProvider.ContentProviderPropertyKey, String> contentProviderProperties = new HashMap<>();
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.PRODUCT_URL, ELIB_PRODUCT_URL);
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.CREATE_LOAN_URL, ELIB_CREATE_LOAN_URL);
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.ORDER_LIST_URL, ELIB_ORDER_LIST_URL);
        ContentProvider elibProvider = new ContentProvider(ContentProviderName.ELIB, contentProviderProperties);
        elibProvider = contentProviderAdminController.save(elibProvider);

        Map<String, FormatDecoration> formatDecorations = new HashMap<>();
        FormatDecoration formatDecoration0 = new FormatDecoration(elibProvider, ELIB_FORMAT_0_ID, ContentDisposition.DOWNLOADABLE, ELIB_PLAYER_WIDTH,
                ELIB_PLAYER_HEIGHT);
        FormatDecoration formatDecoration1 = new FormatDecoration(elibProvider, ELIB_FORMAT_1_ID, ContentDisposition.STREAMING, ELIB_PLAYER_WIDTH,
                ELIB_PLAYER_HEIGHT);
        FormatDecoration formatDecoration2 = new FormatDecoration(elibProvider, ELIB_FORMAT_2_ID, ContentDisposition.STREAMING, ELIB_PLAYER_WIDTH,
                ELIB_PLAYER_HEIGHT);

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
        ehubConsumerId = ehubConsumer.getId();
        ehubConsumer.setContentProviderConsumers(new HashSet<ContentProviderConsumer>());
        return ehubConsumer;
    }
    
    private ContentProviderConsumer initElibConsumer(EhubConsumer ehubConsumer, ContentProvider elibProvider) {
        Map<ContentProviderConsumer.ContentProviderConsumerPropertyKey, String> elibConsumerProperties = new HashMap<>();
        elibConsumerProperties.put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_KEY, ELIB_SERVICE_KEY);
        elibConsumerProperties.put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_ID, ELIB_SERVICE_ID);
        ContentProviderConsumer elibConsumer = new ContentProviderConsumer(ehubConsumer, elibProvider, elibConsumerProperties);
        elibConsumer = consumerAdminController.save(elibConsumer);
        ehubConsumer.getContentProviderConsumers().add(elibConsumer);
        consumerAdminController.save(ehubConsumer);
        return elibConsumer;
    }

    public static EhubConsumer createEhubConsumer() {
        Map<EhubConsumer.EhubConsumerPropertyKey, String> ehubConsumerProperties = new HashMap<>();
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, ARENA_PALMA_URL);
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, ARENA_AGENCY_M_IDENTIFIER);
        EhubConsumer ehubConsumer = new EhubConsumer("Ehub Consumer Description", EHUB_CONSUMER_SECRET_KEY, ehubConsumerProperties, DEFAULT_LANGUAGE);
        return ehubConsumer;
    }
}
