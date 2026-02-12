package com.axiell.ehub.local.it;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.language.Language;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.platform.Platform;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.consumer.IConsumerAdminController;
import com.axiell.ehub.local.language.ILanguageAdminController;
import com.axiell.ehub.local.provider.IContentProviderAdminController;
import com.axiell.ehub.local.provider.record.format.IFormatAdminController;
import com.axiell.ehub.local.provider.record.platform.IPlatformAdminController;
import com.google.common.collect.Sets;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import static com.axiell.ehub.common.provider.record.format.ContentDisposition.DOWNLOADABLE;
import static com.axiell.ehub.common.provider.record.format.ContentDisposition.STREAMING;

public class DevelopmentData {
    public static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";

    public static final String LMS_RECORD_ID = "lmsRecordId";
    public static final String LMS_LOAN_ID = "lmsLoanId";
    public static final String CONTENT_PROVIDER_LOAN_ID = "contentProviderLoanId";
    public static final String LIBRARY_CARD = "libraryCard";
    public static final String PIN = "pin";
    public static final String EHUB_CONSUMER_SECRET_KEY = "ehubConsumerSecretKey";
    public static final String ARENA_LOCAL_API_ENDPOINT = "http://localhost:16520/local-rest/api";
    public static final String ARENA_AGENCY_M_IDENTIFIER = "30006";
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
    public static final String TEST_EP_API_BASE_URL = "http://localhost:16520/ep/api";
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
    protected DataSource dataSource;

    // Global data
    private Long ehubConsumerId;
    private EhubConsumer ehubConsumer;
    private ContentProvider contentProvider;
    private Platform platformDesktop;
    private Platform platformAndroid;
    private Platform platformIos;

    public DevelopmentData(
            final IContentProviderAdminController contentProviderAdminController,
            final IFormatAdminController formatAdminController,
            final IConsumerAdminController consumerAdminController,
            final ILanguageAdminController languageAdminController,
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
