set serveroutput on

declare
   statement varchar2(4000);
  uq_no number := null;
   table_name_prev user_constraints.table_name%TYPE := null;
   constraint_name user_constraints.constraint_name%TYPE;

begin
    for uc in (
        select table_name, index_name, constraint_name, constraint_type
        from user_constraints
        where constraint_type in ('P','U') AND table_name in (select table_name from user_tables)
        order by table_name,constraint_type,
        (select listagg(column_name, '|') WITHIN GROUP (ORDER BY column_name) from user_ind_columns where
        user_ind_columns.table_name=user_constraints.table_name AND user_ind_columns.index_name=user_constraints.index_name)
    )
    loop
        if uc.constraint_type = 'P' then
            constraint_name := 'PK_'||substr(uc.table_name,1,27);
        else
            if table_name_prev is NULL OR table_name_prev <> uc.table_name then
               constraint_name := 'UK_'||substr(uc.table_name,1,27);
               table_name_prev := uc.table_name;
               uq_no := null;
            else
               uq_no := nvl(uq_no,0) + 1;
               constraint_name := 'UK_'||substr(uc.table_name,1,25)||'_'||uq_no;
            end if;
        end if;
        if constraint_name <> uc.constraint_name then
            statement:='alter table ' || uc.table_name|| ' rename constraint '|| uc.constraint_name || ' to '||constraint_name;
		        dbms_output.put_line('sql: '||statement);
		        execute immediate statement;
        end if;
        if constraint_name <> uc.index_name then
            statement:='alter index ' || uc.index_name|| ' rename to '||constraint_name;
		        dbms_output.put_line('sql: '||statement);
		        execute immediate statement;
        end if;
    end loop;
end;
/
