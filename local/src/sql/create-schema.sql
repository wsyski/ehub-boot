column 1 new_value 1 noprint
SELECT NULL "1" FROM dual WHERE 1 = 0;
select decode('&1', null, 'ehub1', '&1') "1"
from dual;
define user_name='&1'
col password new_value password
select substr('&user_name',1,1)||'ehub'||substr('&user_name',-1,1) password from dual;

ALTER PROFILE DEFAULT LIMIT FAILED_LOGIN_ATTEMPTS UNLIMITED PASSWORD_LIFE_TIME UNLIMITED;

CREATE USER &&user_name IDENTIFIED BY &&password;
alter user &&user_name default tablespace data temporary tablespace temp
quota unlimited on data;
GRANT connect TO &&user_name;
GRANT resource TO &&user_name;
grant execute on  dbms_aq to &&user_name;
grant create view to &&user_name;

exit;


