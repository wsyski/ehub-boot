spool init-production.log
set define off

PROMPT schema.sql
@@schema.sql

PROMPT schema-ext.sql
@@schema-ext.sql

PROMPT basic-data.sql
@@basic-data.sql

PROMPT rename-constraints.sql
@@rename-constraints.sql

PROMPT create-fk-indexes.sql
@@create-fk-indexes.sql

spool off
exit;
