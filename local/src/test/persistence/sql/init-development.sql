spool init-development.log
set define off

PROMPT drop.sql
@@drop.sql

PROMPT schema.sql
@@schema.sql

PROMPT schema-ext.sql
@@schema-ext.sql

PROMPT basic-data.sql
@@basic-data.sql

PROMPT development-data.sql
@@development-data.sql

COMMIT;
--ROLLBACK;

spool off
exit;
