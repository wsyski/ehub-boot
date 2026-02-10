package com.axiell.ehub.test;

import java.time.LocalDate;
import java.util.Locale;

public class TestDataConstants  {
    public static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    public static final String EHUB_CONSUMER_DESCRIPTION = "Ehub Consumer Description";
    public static final String CONTENT_PROVIDER_ALIAS_PREFIX = "Distribut\u00f6r: ";

    public static final String LMS_RECORD_ID = "lmsRecordId";
    public static final String LMS_LOAN_ID = "lmsLoanId";
    public static final String CONTENT_PROVIDER_LOAN_ID = "contentProviderLoanId";
    public static final String LIBRARY_CARD = "libraryCard";
    public static final String PIN = "pin";
    public static final String EMAIL = "wos@axiell.com";
    public static final String ARENA_LOCAL_API_ENDPOINT = "http://localhost:16520/local-rest/api";
    public static final String ARENA_AGENCY_M_IDENTIFIER = "30006";
    public static final String PATRON_ID = "patronId";
    public static final String NAME = "name";
    public static final LocalDate BIRTH_DATE = LocalDate.parse("2001-01-01");
    public static final String DEFAULT_LANGUAGE = Locale.ENGLISH.getLanguage();
    public static final String PLATFORM_DESKTOP = "DESKTOP";
    public static final String PLATFORM_ANDROID = "ANDROID";
    public static final String PLATFORM_IOS = "IOS";
    public static final String RECORD_ID_0 = "recordId_0";
    public static final String RECORD_ID_1 = "recordId_1";
    public static final String ISSUE_ID_0 = "issueId_0";
    public static final String ISSUE_TITLE_0 = "issueTitle_0";

    public static final String TEST_EP_FORMAT_ID_0 = "ebook";
    public static final String TEST_EP_FORMAT_ID_1 = "audio-stream";
    public static final String TEST_EP_FORMAT_ID_2 = "audio-downloadable";
    public static final String TEST_EP_API_BASE_URL = "http://localhost:16520/ep/api";
    public static final String TEST_EP_SITE_ID = "siteId";
    public static final String TEST_EP_SECRET_KEY = "c2VjcmV0S2V5MTM5NDA=";
    public static final long TEST_EP_TOKEN_EXPIRATION_TIME_IN_SECONDS = 0L;
    public static final long TEST_EP_TOKEN_LEEWAY_IN_SECONDS = 120L;
}
