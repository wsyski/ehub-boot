spool default.log
set define off

PROMPT insert-into-release.sql
@@../basic/insert-into-release.sql

spool off
exit;