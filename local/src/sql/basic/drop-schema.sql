column 1 new_value 1 noprint
SELECT NULL "1" FROM dual WHERE 1 = 0;
select decode('&1', null, 'ehub1', '&1') "1"
from dual;
define user_name='&1'
drop user &&user_name cascade;

exit;
