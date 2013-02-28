set serveroutput on

declare
   type constraint_columns_type is table of user_cons_columns.column_name%TYPE;
   statement varchar2(4000);
   index_name user_indexes.index_name%TYPE;
   constraint_columns constraint_columns_type:=constraint_columns_type(); 
   old_index_name user_indexes.index_name%TYPE;
   current_index_name user_indexes.index_name%TYPE;
   pos integer;
   len integer;
   dummy char(1);
begin
    for uc in (
        select table_name, index_name, constraint_name 
        from user_constraints 
        where constraint_type in ('R') AND table_name in (select table_name from user_tables) 
        order by table_name,constraint_type
    )
    loop
        len := length(uc.constraint_name);
        if len > 28 and substr(uc.constraint_name, len - 1, 1) = '_' and ascii(substr(uc.constraint_name, len, 1)) >= ascii('0') and ascii(substr(uc.constraint_name, len, 1)) <= ascii('9') then
            index_name := 'I_'||substr(uc.constraint_name, 1, 26)||substr(uc.constraint_name, len - 1, 2);
        else
            index_name := 'I_'||substr(uc.constraint_name, 1, 28);
        end if;    
        constraint_columns.delete();
        --dbms_output.put_line('Processing table: '||uc.table_name||' index_name: '||index_name||' constraint_name: '||uc.constraint_name);
        for ucc in (
            select column_name 
            from user_cons_columns 
            where constraint_name = uc.constraint_name AND table_name in (select table_name from user_tables) 
            order by position
        )
        loop
            constraint_columns.extend;
            constraint_columns(constraint_columns.last) := ucc.column_name;
        end loop;
        --Check if index exists 
        old_index_name := null;
        current_index_name := null;
        statement:=null;
        for uic in (
            select index_name, column_name 
            from user_ind_columns 
            where table_name = uc.table_name
            order by index_name, column_position
        )
        loop
          if old_index_name is null or old_index_name != uic.index_name then
               if current_index_name is not null then
                   --dbms_output.put_line('Index: '||uic.index_name||' has the same columns');
                   exit;
               end if;
               old_index_name := uic.index_name;
               current_index_name:= uic.index_name;
               pos := 0;
               --dbms_output.put_line('Trying index: '||uic.index_name);
          end if;     
          pos:=pos+1;
          --dbms_output.put_line(' pos: '||pos||' constraint_columns.last: '||constraint_columns.last||' column_name: '||uic.column_name );
          if constraint_columns.last is null or constraint_columns.last < pos or constraint_columns(pos) != uic.column_name then
             current_index_name := null;
          end if;
        end loop;
        --dbms_output.put_line('current_index_name: '||current_index_name);
        if current_index_name is null then
            statement := 'CREATE INDEX '||index_name||' on '||uc.table_name||'(';
            pos:=0;
            for i IN constraint_columns.first .. constraint_columns.last loop
                if i > constraint_columns.first then
                    statement := statement || ',';
                end if;    
                statement := statement || constraint_columns(i);
            end loop;
            statement:=statement||')';
        elsif current_index_name != index_name then
            -- Check if there exist a constraint with the same name
            begin
                select 'x' into dummy
                from user_constraints where constraint_name=current_index_name;
            exception    
                when no_data_found then
                    statement:='ALTER INDEX '||current_index_name||' RENAME TO '||index_name;
            end;    
        end if;
        if statement is not null then
            dbms_output.put_line('sql: '||statement);
		        execute immediate statement;    
        end if; 
    end loop;
end;
/