package com.axiell.ehub;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.language.ILanguageAdminController;
import com.axiell.ehub.language.Language;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.platform.Platform;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import com.axiell.ehub.provider.record.platform.IPlatformAdminController;
import com.google.common.collect.Sets;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static com.axiell.ehub.provider.record.format.ContentDisposition.DOWNLOADABLE;
import static com.axiell.ehub.provider.record.format.ContentDisposition.STREAMING;

public class DevelopmentData {
    public static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";

    public static final String LMS_RECORD_ID = "lmsRecordId";
    public static final String LMS_LOAN_ID = "lmsLoanId";
    public static final String CONTENT_PROVIDER_LOAN_ID = "contentProviderLoanId";
    public static final String LIBRARY_CARD = "libraryCard";
    public static final String PIN = "pin";
    public static final String EHUB_CONSUMER_SECRET_KEY = "ehubConsumerSecretKey";
    public static final String ARENA_LOCAL_API_ENDPOINT = "http://localhost:16521/arena.pa.palma";
    public static final String ARENA_AGENCY_M_IDENTIFIER = "MSE000001";
    public static final String PATRON_ID = "patronId";
    public static final String DEFAULT_LANGUAGE = Locale.ENGLISH.getLanguage();

    public static final String TEST_EP_RECORD_0_ID = "recordId_0";
    public static final String TEST_EP_RECORD_1_ID = "recordId_1";
    public static final String TEST_EP_FORMAT_0_ID = "ebook";
    public static final String TEST_EP_FORMAT_0_NAME = "eBook format";
    public static final String TEST_EP_FORMAT_1_ID = "audio-stream";
    public static final String TEST_EP_FORMAT_1_NAME = "eAudio stream format";
    public static final String TEST_EP_FORMAT_2_ID = "audio-downloadable";
    public static final String TEST_EP_FORMAT_2_NAME = "eAudio downloadable format";
    public static final String TEST_EP_API_BASE_URL = "http://localhost:16521/ep/api";
    public static final String TEST_EP_SITE_ID = "siteId";
    public static final String TEST_EP_SECRET_KEY = "testEpSecretKey";
    public static final String PLATFORM_DESKTOP = "DESKTOP";
    public static final String PLATFORM_ANDROID = "ANDROID";
    public static final String PLATFORM_IOS = "IOS";

    protected IContentProviderAdminController contentProviderAdminController;
    protected IFormatAdminController formatAdminController;
    protected IConsumerAdminController consumerAdminController;
    protected ILanguageAdminController languageAdminController;
    protected IPlatformAdminController platformAdminController;

    // Global data
    private Long ehubConsumerId;
    private EhubConsumer ehubConsumer;
    private ContentProvider contentProvider;
    private Platform platformDesktop;
    private Platform platformAndroid;
    private Platform platformIos;

    public DevelopmentData(final IContentProviderAdminController contentProviderAdminController, final IFormatAdminController formatAdminController,
                           final IConsumerAdminController consumerAdminController, final ILanguageAdminController languageAdminController,
                           final IPlatformAdminController platformAdminController) {
        this.contentProviderAdminController = contentProviderAdminController;
        this.formatAdminController = formatAdminController;
        this.consumerAdminController = consumerAdminController;
        this.languageAdminController = languageAdminController;
        this.platformAdminController = platformAdminController;
    }

    public static EhubConsumer createEhubConsumer() {
        Map<EhubConsumer.EhubConsumerPropertyKey, String> ehubConsumerProperties = new HashMap<>();
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_LOCAL_API_ENDPOINT, ARENA_LOCAL_API_ENDPOINT);
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, ARENA_AGENCY_M_IDENTIFIER);
        EhubConsumer ehubConsumer = new EhubConsumer("Ehub Consumer Description", EHUB_CONSUMER_SECRET_KEY, ehubConsumerProperties, DEFAULT_LANGUAGE);
        return ehubConsumer;
    }

    public void init() throws Exception {
        initPlatforms();
        initLanguage();
        contentProvider = initContentProvider();
        ehubConsumer = initEhubConsumer();
        initContentConsumer(getEhubConsumer(), getContentProvider());
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

    public ContentProvider getContentProvider() {
        return contentProvider;
    }

    private void initLanguage() {
        languageAdminController.save(new Language(DEFAULT_LANGUAGE));
    }

    private void initPlatforms() {
        platformDesktop = platformAdminController.save(new Platform(PLATFORM_DESKTOP));
        platformIos = platformAdminController.save(new Platform(PLATFORM_IOS));
        platformAndroid = platformAdminController.save(new Platform(PLATFORM_ANDROID));
    }

    private ContentProvider initContentProvider() {
        Map<ContentProvider.ContentProviderPropertyKey, String> contentProviderProperties = new HashMap<>();
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.API_BASE_URL, TEST_EP_API_BASE_URL);
        ContentProvider contentProvider = new ContentProvider(CONTENT_PROVIDER_TEST_EP, contentProviderProperties);
        contentProvider = contentProviderAdminController.save(contentProvider);

        Map<String, FormatDecoration> formatDecorations = new HashMap<>();
        FormatDecoration formatDecoration0 = new FormatDecoration(contentProvider, TEST_EP_FORMAT_0_ID, DOWNLOADABLE,
                Collections.singleton(platformAndroid));
        FormatDecoration formatDecoration1 = new FormatDecoration(contentProvider, TEST_EP_FORMAT_1_ID, STREAMING, Collections.singleton(platformIos));
        FormatDecoration formatDecoration2 = new FormatDecoration(contentProvider, TEST_EP_FORMAT_2_ID, DOWNLOADABLE,
                Sets.newHashSet(platformDesktop, platformIos, platformAndroid));

        formatDecorations.put(TEST_EP_FORMAT_0_ID, formatDecoration0);
        formatDecorations.put(TEST_EP_FORMAT_1_ID, formatDecoration1);
        formatDecorations.put(TEST_EP_FORMAT_2_ID, formatDecoration2);

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
        ehubConsumerId = ehubConsumer.getId();
        ehubConsumer.setContentProviderConsumers(new HashSet<ContentProviderConsumer>());
        return ehubConsumer;
    }

    private ContentProviderConsumer initContentConsumer(EhubConsumer ehubConsumer, ContentProvider contentProvider) {
        Map<ContentProviderConsumer.ContentProviderConsumerPropertyKey, String> properties = new HashMap<>();
        properties.put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY, TEST_EP_SECRET_KEY);
        properties.put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID, TEST_EP_SITE_ID);
        ContentProviderConsumer contentProviderConsumer = new ContentProviderConsumer(ehubConsumer, contentProvider, properties);
        contentProviderConsumer = consumerAdminController.save(contentProviderConsumer);
        ehubConsumer.getContentProviderConsumers().add(contentProviderConsumer);
        consumerAdminController.save(ehubConsumer);
        return contentProviderConsumer;
    }
}
