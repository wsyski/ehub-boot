 drop sequence hibernate_sequence;

 create sequence hibernate_sequence start with 10000;

 create table CONTENT_PROVIDER_NAME (
        NAME varchar2(255 char) not null,
        primary key (NAME)
    );

 create table RELEASE (
        VERSION varchar2(255 char) not null,
        CREATE_DATETIME timestamp not null,
        MODIFY_DATETIME timestamp not null,
        primary key (VERSION)
    );

 alter table CONTENT_PROVIDER
        add constraint FK_CONTENT_P_CONTENT_P_N
        foreign key (NAME)
        references CONTENT_PROVIDER_NAME;

 create table CONTENT_DISPOSITION (
        CONTENT_DISPOSITION varchar2(255 char) not null,
        primary key (CONTENT_DISPOSITION)
    );

 alter table CONTENT_P_FORMAT_DECORATION
        add constraint FK_CONTENT_P_F_D_CONTENT_D
        foreign key (CONTENT_DISPOSITION)
        references CONTENT_DISPOSITION;

 create table EHUB_CONSUMER_PROPERTY_KEY (
        PROPERTY_KEY varchar2(255 char) not null,
        primary key (PROPERTY_KEY)
    );

 alter table EHUB_CONSUMER_PROPERTY
        add constraint FK_EHUB_C_P_EHUB_C_P_K
        foreign key (PROPERTY_KEY)
        references EHUB_CONSUMER_PROPERTY_KEY;

 create table CONTENT_P_CONSUMER_P_KEY (
        PROPERTY_KEY varchar2(255 char) not null,
        primary key (PROPERTY_KEY)
    );

 alter table CONTENT_P_CONSUMER_PROPERTY
        add constraint FK_CONTENT_P_C_P_CNT_P_C_P_K
        foreign key (PROPERTY_KEY)
        references CONTENT_P_CONSUMER_P_KEY;

 create table CONTENT_PROVIDER_PROPERTY_KEY (
        PROPERTY_KEY varchar2(255 char) not null,
        primary key (PROPERTY_KEY)
    );

 alter table CONTENT_PROVIDER_PROPERTY
        add constraint FK_CONTENT_P_P_CONTENT_P_P_K
        foreign key (PROPERTY_KEY)
        references CONTENT_PROVIDER_PROPERTY_KEY;

 alter table CONTENT_P_FORMAT_TEXT_BUNDLE
        add constraint FK_CONT_P_F_T_B_CONT_P_F_L
        foreign key (LANGUAGE)
        references CONTENT_P_FORMAT_LANGUAGE;
