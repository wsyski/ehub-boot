
    create table CONTENT_PROVIDER (
        id number(19,0) not null,
        NAME varchar2(255 char) not null unique,
        primary key (id)
    );

    create table CONTENT_PROVIDER_CONSUMER (
        id number(19,0) not null,
        CONTENT_PROVIDER_ID number(19,0),
        EHUB_CONSUMER_ID number(19,0),
        primary key (id)
    );

    create table CONTENT_PROVIDER_PROPERTY (
        CONTENT_PROVIDER_ID number(19,0) not null,
        PROPERTY_VALUE varchar2(255 char),
        PROPERTY_KEY varchar2(255 char) not null,
        primary key (CONTENT_PROVIDER_ID, PROPERTY_KEY)
    );

    create table CONTENT_P_CONSUMER_PROPERTY (
        CONTENT_PROVIDER_CONSUMER_ID number(19,0) not null,
        PROPERTY_VALUE varchar2(255 char),
        PROPERTY_KEY varchar2(255 char) not null,
        primary key (CONTENT_PROVIDER_CONSUMER_ID, PROPERTY_KEY)
    );

    create table CONTENT_P_FORMAT_DECORATION (
        id number(19,0) not null,
        CONTENT_DISPOSITION varchar2(255 char) not null,
        FORMAT_ID varchar2(255 char) not null,
        PLAYER_HEIGHT number(10,0) not null,
        PLAYER_WIDTH number(10,0) not null,
        CONTENT_PROVIDER_ID number(19,0) not null,
        primary key (id),
        unique (CONTENT_PROVIDER_ID, FORMAT_ID)
    );

    create table CONTENT_P_FORMAT_LANGUAGE (
        LANGUAGE varchar2(255 char) not null unique,
        primary key (LANGUAGE)
    );

    create table CONTENT_P_FORMAT_TEXT_BUNDLE (
        id number(19,0) not null,
        DESCRIPTION varchar2(255 char) not null,
        LANGUAGE varchar2(255 char) not null,
        NAME varchar2(255 char) not null,
        CONTENT_P_FORMAT_DECORATION_ID number(19,0) not null,
        primary key (id),
        unique (CONTENT_P_FORMAT_DECORATION_ID, LANGUAGE)
    );

    create table EHUB_ADMIN_USER (
        id number(19,0) not null,
        PASSWORD varchar2(255 char) not null,
        NAME varchar2(255 char) not null unique,
        SALT varchar2(16 char) not null,
        primary key (id)
    );

    create table EHUB_CONSUMER (
        id number(19,0) not null,
        DESCRIPTION varchar2(255 char) not null unique,
        SECRET_KEY varchar2(255 char) not null,
        primary key (id)
    );

    create table EHUB_CONSUMER_PROPERTY (
        EHUB_CONSUMER_ID number(19,0) not null,
        PROPERTY_VALUE varchar2(255 char),
        PROPERTY_KEY varchar2(255 char) not null,
        primary key (EHUB_CONSUMER_ID, PROPERTY_KEY)
    );

    create table EHUB_LOAN (
        id number(19,0) not null,
        EXPIRATION_DATE date not null,
        CONTENT_PROVIDER_LOAN_ID varchar2(255 char) not null,
        LMS_LOAN_ID varchar2(255 char) not null,
        CONTENT_PROVIDER_ID number(19,0) not null,
        CONTENT_P_FORMAT_DECORATION_ID number(19,0) not null,
        EHUB_CONSUMER_ID number(19,0) not null,
        primary key (id),
        unique (LMS_LOAN_ID, EHUB_CONSUMER_ID)
    );

    alter table CONTENT_PROVIDER_CONSUMER 
        add constraint CONTENT_P_C_EHUB_C_FK 
        foreign key (EHUB_CONSUMER_ID) 
        references EHUB_CONSUMER;

    alter table CONTENT_PROVIDER_CONSUMER 
        add constraint CONTENT_P_C_CONTENT_P_FK 
        foreign key (CONTENT_PROVIDER_ID) 
        references CONTENT_PROVIDER;

    alter table CONTENT_PROVIDER_PROPERTY 
        add constraint CONTENT_P_P_CONTENT_P_FK 
        foreign key (CONTENT_PROVIDER_ID) 
        references CONTENT_PROVIDER;

    alter table CONTENT_P_CONSUMER_PROPERTY 
        add constraint CONTENT_P_C_P_CONTENT_P_C_FK 
        foreign key (CONTENT_PROVIDER_CONSUMER_ID) 
        references CONTENT_PROVIDER_CONSUMER;

    alter table CONTENT_P_FORMAT_DECORATION 
        add constraint CONTENT_P_F_D_CONTENT_P_FK 
        foreign key (CONTENT_PROVIDER_ID) 
        references CONTENT_PROVIDER;

    alter table CONTENT_P_FORMAT_TEXT_BUNDLE 
        add constraint CONTENT_P_F_T_CONTENT_P_F_D_FK 
        foreign key (CONTENT_P_FORMAT_DECORATION_ID) 
        references CONTENT_P_FORMAT_DECORATION;

    alter table EHUB_CONSUMER_PROPERTY 
        add constraint EHUB_CONSUMER_P_EHUB_C_FK 
        foreign key (EHUB_CONSUMER_ID) 
        references EHUB_CONSUMER;

    alter table EHUB_LOAN 
        add constraint EHUB_LOAN_EHUB_CONSUMER_FK 
        foreign key (EHUB_CONSUMER_ID) 
        references EHUB_CONSUMER;

    alter table EHUB_LOAN 
        add constraint EHUB_LOAN_CONTENT_PROVIDER_FK 
        foreign key (CONTENT_PROVIDER_ID) 
        references CONTENT_PROVIDER;

    alter table EHUB_LOAN 
        add constraint CONTENT_P_L_M_CONTENT_P_F_D_FK 
        foreign key (CONTENT_P_FORMAT_DECORATION_ID) 
        references CONTENT_P_FORMAT_DECORATION;

    create sequence hibernate_sequence;
