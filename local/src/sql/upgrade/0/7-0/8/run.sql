spool upgrade-0.7-0.8.log
set define off

PROMPT insert-into-release.sql
@@../../basic/insert-into-release.sql

WHENEVER SQLERROR EXIT FAILURE

alter table EHUB_LOAN modify content_provider_loan_id null;


commit;

spool off