merge into release r
using dual on (r.version= '${project.version}')
when not matched then
insert(r.version,r.create_datetime,r.modify_datetime) values('${project.version}',systimestamp,systimestamp)
when matched then
update set r.modify_datetime = systimestamp;
commit;



