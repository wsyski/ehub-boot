spool init-development.log
set define off

PROMPT schema.sql
@@schema.sql

PROMPT schema-ext.sql
@@schema-ext.sql

PROMPT basic-data.sql
@@basic-data.sql

PROMPT insert-into-release.sql
@@insert-into-release.sql

PROMPT rename-constraints.sql
@@rename-constraints.sql

PROMPT create-fk-indexes.sql
@@create-fk-indexes.sql

PROMPT development-data.sql
@@development-data.sql

spool off
exit;

