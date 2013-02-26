spool init-development.log
set define off

PROMPT schema.sql
@@schema.sql

PROMPT schema-ext.sql
@@schema-ext.sql

PROMPT basic-data.sql
@@basic-data.sql

PROMPT rename-constraints.sql
@@rename-constraints.sql

PROMPT development-data.sql
@@development-data.sql

spool off
exit;

