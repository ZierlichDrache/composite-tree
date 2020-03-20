delete from COMPONENT_PARENT;
delete from COMPONENT where id <> 1;
update COMPONENT set value = 0 where id = 1;
