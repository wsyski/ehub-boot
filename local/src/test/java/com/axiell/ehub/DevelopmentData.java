package com.axiell.ehub;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import com.axiell.ehub.provider.record.format.IFormatAdminController;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

public class DevelopmentData {
    public static final String EHUB_CONSUMER_SECRET_KEY = "secret1";

    public static final String ELIB_PRODUCT_URL = "https://www.elib.se/webservices/GetProduct.asmx/GetProduct";
    public static final String ELIB_CREATE_LOAN_URL = "https://www.elib.se/webservices/createloan.asmx/CreateLoan";
    public static final String ELIB_ORDER_LIST_URL = "https://www.elib.se/webservices/getlibraryuserorderlist.asmx/GetLibraryUserOrderList";
    public static final String EHUB_KEY = "ehubKey";
    public static final String ARENA_PALMA_URL = "http://localhost:16521/arena.pa.palma";
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

    public static final String ELIBU_CONSUME_LICENSE_URL = "https://webservices.elib.se/elibu/v1.0/licenses";
    public static final String ELIBU_PRODUCT_URL = "https://webservices.elib.se/elibu/v1.0/products";
    public static final String ELIBU_SERVICE_ID = "934";
    public static final String ELIBU_SERVICE_KEY = "xvfKOWHNeZF3";
    public static final String ELIBU_SERVICE_KEY_MD5 = "7b39e82019ca2365f2fff0e06533838e";
    public static final String ELIBU_SUBSCRIPTION_ID = "3";
    public static final String ELIBU_RECORD_ID = "1002443";
    public static final String ELIBU_FORMAT_0_ID = "1001";
    public static final String ELIBU_FORMAT_0_NAME = "Epub, streamed";
    public static final String ELIBU_FORMAT_0_DESCRIPTION = "Epub distributed as a stream.";
    public static final String ELIBU_FORMAT_1_ID = "1002";
    public static final String ELIBU_FORMAT_1_NAME = "Pdf, streamed";
    public static final String ELIBU_FORMAT_1_DESCRIPTION = "Pdf distributed as a stream.";
    public static final String ELIBU_FORMAT_2_ID = "901";
    public static final String ELIBU_FORMAT_2_NAME = "Flash player";
    public static final String ELIBU_FORMAT_2_DESCRIPTION = "Flash distributed as a stream.";
    public static final String ELIBU_FORMAT_3_ID = "902";
    public static final String ELIBU_FORMAT_3_NAME = "Html player";
    public static final String ELIBU_FORMAT_3_DESCRIPTION = "Html5 distributed as a stream.";
    public static final String ELIBU_LIBRARY_CARD = "12345";
    public static final String ELIBU_LIBRARY_CARD_PIN = "1111";
    public static final Integer ELIBU_LICENSE_ID = 6;
    public static final String ELIBU_LOAN_ID = ELIBU_LICENSE_ID + "|" + ELIBU_RECORD_ID;

    public static final int ELIBU_FLASH_PLAYER_WIDTH = 388;
    public static final int ELIBU_FLASH_PLAYER_HEIGHT = 164;
    public static final int ELIBU_HTML5_PLAYER_WIDTH = 320;
    public static final int ELIBU_HTML5_PLAYER_HEIGHT = 240;
    public static final int ELIBU_EBOOK_PLAYER_WIDTH_AND_HEIGHT = 0;
    public static final int ELIB_PLAYER_WIDTH = 600;
    public static final int ELIB_PLAYER_HEIGHT = 215;

    public static final String PUBLIT_URL = "http://beta.publit.se/";
    public static final String PUBLIT_FORMAT_0_ID = "E-bok";
    public static final String PUBLIT_FORMAT_0_NAME = "E-bok";
    public static final int PUBLIT_PLAYER_WIDTH = 320;
    public static final int PUBLIT_PLAYER_HEIGHT = 240;
    public static final String PUBLIT_USERNAME = "axiell";
    public static final String PUBLIT_PASSWORD = "4x1eLl_12";
    public static final String PUBLIT_RECORD_0_ID = "9789174376838";
    public static final String PUBLIT_LIBRARY_CARD = "12345";
    public static final String PUBLIT_LIBRARY_CARD_PIN = "1111";
    
    public static final String ASKEWS_FORMAT_0_ID = "Askews";
    public static final String ASKEWS_FORMAT_0_NAME = "Askews";
    public static final int ASKEWS_PLAYER_WIDTH = 320;
    public static final int ASKEWS_PLAYER_HEIGHT = 240;
    public static final String ASKEWS_BARCODE = "axiell";
    public static final String ASKEWS_AUTHID = "0";
    public static final String ASKEWS_TOKENKEY = "g94ngpts3ngmkeaqtz953dbmutyndw";
    public static final String ASKEWS_LOAN_DURATION = "1";
    public static final String ASKEWS_RECORD_0_ID = "9781407059815-6";
    public static final String ASKEWS_LIBRARY_CARD = "12345";
    public static final String ASKEWS_LIBRARY_CARD_PIN = "1111";

    protected IContentProviderAdminController contentProviderAdminController;
    protected IFormatAdminController formatAdminController;
    protected IConsumerAdminController consumerAdminController;

    // Global data
    private Long ehubConsumerId;
    private EhubConsumer ehubConsumer;
    private ContentProvider elibProvider;
    private ContentProvider publitProvider;

    /**
     * 
     * @param contentProviderAdminController
     * @param formatAdminController
     * @param consumerAdminController
     */
    public DevelopmentData(final IContentProviderAdminController contentProviderAdminController,
            final IFormatAdminController formatAdminController, final IConsumerAdminController consumerAdminController) {
        this.contentProviderAdminController = contentProviderAdminController;
        this.formatAdminController = formatAdminController;
        this.consumerAdminController = consumerAdminController;
    }

    /**
     * 
     * @throws Exception
     */
    public void init() throws Exception {
        elibProvider = initElibProvider();
        initElibUProvider();
        publitProvider = initPublitProvider();
        ehubConsumer = initEhubConsumer();
        initElibConsumer(getEhubConsumer(), getElibProvider());
        initPublitConsumer(getEhubConsumer(), getPublitProvider());
    }

    /**
     * 
     * @throws Exception
     * @see #destroy(DataSource)
     */
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
            ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[]{"/com/axiell/ehub/data-source-context.xml"});
            dataSource = (DataSource) springContext.getBean("dataSource");
        }
        
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                Statement stmt = connection.createStatement();
                try {
                    stmt.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
                    connection.commit();
                } finally {
                    stmt.close();
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new Exception(e);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public Long getEhubConsumerId() {
        return ehubConsumerId;
    }

    /**
     * Returns the ehubConsumer.
     * 
     * @return the ehubConsumer
     */
    public EhubConsumer getEhubConsumer() {
        return ehubConsumer;
    }

    /**
     * Returns the elibProvider.
     * 
     * @return the elibProvider
     */
    public ContentProvider getElibProvider() {
        return elibProvider;
    }
    
    public ContentProvider getPublitProvider() {
        return publitProvider;
    }
    
    /**
     * 
     * @return
     */
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

    /**
     * 
     * @return
     */
    private ContentProvider initElibUProvider() {
        Map<ContentProvider.ContentProviderPropertyKey, String> contentProviderProperties = new HashMap<>();
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.PRODUCT_URL, ELIBU_PRODUCT_URL);
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.CONSUME_LICENSE_URL, ELIBU_CONSUME_LICENSE_URL);
        ContentProvider elibUProvider = new ContentProvider(ContentProviderName.ELIBU, contentProviderProperties);
        elibUProvider = contentProviderAdminController.save(elibUProvider);

        FormatDecoration formatDecoration0 = new FormatDecoration(elibUProvider, DevelopmentData.ELIBU_FORMAT_0_ID, ContentDisposition.STREAMING,
                ELIBU_EBOOK_PLAYER_WIDTH_AND_HEIGHT, ELIBU_EBOOK_PLAYER_WIDTH_AND_HEIGHT);
        FormatDecoration formatDecoration1 = new FormatDecoration(elibUProvider, DevelopmentData.ELIBU_FORMAT_1_ID, ContentDisposition.STREAMING,
                ELIBU_EBOOK_PLAYER_WIDTH_AND_HEIGHT, ELIBU_EBOOK_PLAYER_WIDTH_AND_HEIGHT);
        FormatDecoration formatDecoration2 = new FormatDecoration(elibUProvider, DevelopmentData.ELIBU_FORMAT_2_ID, ContentDisposition.STREAMING,
                ELIBU_FLASH_PLAYER_WIDTH, ELIBU_FLASH_PLAYER_HEIGHT);
        FormatDecoration formatDecoration3 = new FormatDecoration(elibUProvider, DevelopmentData.ELIBU_FORMAT_3_ID, ContentDisposition.STREAMING,
                ELIBU_HTML5_PLAYER_WIDTH, ELIBU_HTML5_PLAYER_HEIGHT);

        final String language = Locale.ENGLISH.getLanguage();

        FormatTextBundle textBundle0 = new FormatTextBundle(formatDecoration0, language, DevelopmentData.ELIBU_FORMAT_0_NAME,
                DevelopmentData.ELIBU_FORMAT_0_DESCRIPTION);
        Map<String, FormatTextBundle> textBundles0 = new HashMap<>();
        textBundles0.put(language, textBundle0);
        formatDecoration0.setTextBundles(textBundles0);

        FormatTextBundle textBundle1 = new FormatTextBundle(formatDecoration1, language, DevelopmentData.ELIBU_FORMAT_1_NAME,
                DevelopmentData.ELIBU_FORMAT_1_DESCRIPTION);
        Map<String, FormatTextBundle> textBundles1 = new HashMap<>();
        textBundles1.put(language, textBundle1);
        formatDecoration1.setTextBundles(textBundles1);
        FormatTextBundle textBundle2 = new FormatTextBundle(formatDecoration2, language, DevelopmentData.ELIBU_FORMAT_2_NAME,
                DevelopmentData.ELIBU_FORMAT_2_DESCRIPTION);
        Map<String, FormatTextBundle> textBundles2 = new HashMap<>();
        textBundles2.put(language, textBundle2);
        formatDecoration2.setTextBundles(textBundles2);
        FormatTextBundle textBundle3 = new FormatTextBundle(formatDecoration3, language, DevelopmentData.ELIBU_FORMAT_3_NAME,
                DevelopmentData.ELIBU_FORMAT_3_DESCRIPTION);
        Map<String, FormatTextBundle> textBundles3 = new HashMap<>();
        textBundles3.put(language, textBundle3);
        formatDecoration3.setTextBundles(textBundles3);

        Map<String, FormatDecoration> formatDecorations = new HashMap<>();
        formatDecorations.put(DevelopmentData.ELIBU_FORMAT_0_ID, formatDecoration0);
        formatDecorations.put(DevelopmentData.ELIBU_FORMAT_1_ID, formatDecoration1);
        formatDecorations.put(DevelopmentData.ELIBU_FORMAT_2_ID, formatDecoration2);
        formatDecorations.put(DevelopmentData.ELIBU_FORMAT_3_ID, formatDecoration3);

        for (Map.Entry<String, FormatDecoration> entry : formatDecorations.entrySet()) {
            FormatDecoration value = formatAdminController.save(entry.getValue());
            formatDecorations.put(entry.getKey(), value);
        }

        formatAdminController.save(textBundle0);
        formatAdminController.save(textBundle1);
        formatAdminController.save(textBundle2);
        formatAdminController.save(textBundle3);

        elibUProvider.setFormatDecorations(formatDecorations);
        return contentProviderAdminController.save(elibUProvider);
    }

    private ContentProvider initPublitProvider() {
        Map<ContentProvider.ContentProviderPropertyKey, String> contentProviderProperties = new HashMap<>();
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.PRODUCT_URL, PUBLIT_URL);
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.CREATE_LOAN_URL, PUBLIT_URL);
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.ORDER_LIST_URL, PUBLIT_URL);
        ContentProvider publitProvider = new ContentProvider(ContentProviderName.PUBLIT, contentProviderProperties);
        publitProvider = contentProviderAdminController.save(publitProvider);

        Map<String, FormatDecoration> formatDecorations = new HashMap<>();
        FormatDecoration formatDecoration0 = new FormatDecoration(publitProvider, PUBLIT_FORMAT_0_ID, ContentDisposition.DOWNLOADABLE, PUBLIT_PLAYER_WIDTH,
                PUBLIT_PLAYER_HEIGHT);

        formatDecorations.put(PUBLIT_FORMAT_0_ID, formatDecoration0);
        for (Map.Entry<String, FormatDecoration> entry : formatDecorations.entrySet()) {
            FormatDecoration value = formatAdminController.save(entry.getValue());
            formatDecorations.put(entry.getKey(), value);
        }
        publitProvider.setFormatDecorations(formatDecorations);
        publitProvider = contentProviderAdminController.save(publitProvider);
        return publitProvider;
    }
    
    /**
     * 
     * @return
     */
    private EhubConsumer initEhubConsumer() {
        EhubConsumer ehubConsumer = createEhubConsumer();
        ehubConsumer = consumerAdminController.save(ehubConsumer);
        ehubConsumerId = ehubConsumer.getId();
        ehubConsumer.setContentProviderConsumers(new HashSet<ContentProviderConsumer>());
        return ehubConsumer;
    }

    /**
     * 
     * @param ehubConsumer
     * @param elibProvider
     * @return
     */
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
    
    private ContentProviderConsumer initPublitConsumer(EhubConsumer ehubConsumer, ContentProvider publitProvider) {
        Map<ContentProviderConsumer.ContentProviderConsumerPropertyKey, String> publitConsumerProperties = new HashMap<>();
        publitConsumerProperties.put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.PUBLIT_USERNAME, PUBLIT_USERNAME);
        publitConsumerProperties.put(ContentProviderConsumer.ContentProviderConsumerPropertyKey.PUBLIT_PASSWORD, PUBLIT_PASSWORD);
        ContentProviderConsumer publitConsumer = new ContentProviderConsumer(ehubConsumer, publitProvider, publitConsumerProperties);
        publitConsumer = consumerAdminController.save(publitConsumer);
        ehubConsumer.getContentProviderConsumers().add(publitConsumer);
        consumerAdminController.save(ehubConsumer);
        return publitConsumer;
    }


    public static EhubConsumer createEhubConsumer() {
        Map<EhubConsumer.EhubConsumerPropertyKey, String> ehubConsumerProperties = new HashMap<>();
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, ARENA_PALMA_URL);
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, ARENA_AGENCY_M_IDENTIFIER);
        EhubConsumer ehubConsumer = new EhubConsumer("Ehub Consumer Description", EHUB_CONSUMER_SECRET_KEY, ehubConsumerProperties);
        return ehubConsumer;
    }
}
