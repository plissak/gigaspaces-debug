create table ID_DOMAIN (DOMAIN varchar(255) not null, LAST_ID bigint, primary key (DOMAIN))
create table WIDGET (ID bigint not null, CREATED_DATE date, WIDGET_NAME varchar(255), UPDATED_TIME timestamp, primary key (ID))
create table WIDGET_CODE (WIDGET_ID bigint not null, codes varchar(255))
create table WIDGET_ID_TABLE (WIDGET_ID bigint not null, IDENTIFIER bigint, IDENTIFIER_INDEX integer not null, primary key (WIDGET_ID, IDENTIFIER_INDEX))
create table WIDGET_PART (ID bigint not null, COMMENT varchar(255), PART_NAME varchar(255), WIDGET_ID bigint, primary key (ID))
alter table WIDGET_CODE add constraint FK7wj5wn3meg9j204tpqa84t14p foreign key (WIDGET_ID) references WIDGET
alter table WIDGET_ID_TABLE add constraint FK2rqnwdgeeeyyfo57r83ljq1i foreign key (WIDGET_ID) references WIDGET
alter table WIDGET_PART add constraint FKdo54emjy4mivc3kppkc28pfyu foreign key (WIDGET_ID) references WIDGET

