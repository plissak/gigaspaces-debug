
-- create tables
create table ID_DOMAIN (DOMAIN varchar(255) not null, LAST_ID bigint, primary key (DOMAIN))
create table ADDRESS (ID bigint not null, CITY varchar(255), ADDRESS_NAME varchar(255), ROUTING_KEY bigint default 0 not null, STATE varchar(255), STREET varchar(255), ZIP_CODE varchar(255), primary key (ID))

-- insert data
insert into ADDRESS values (5001, 'Washington', 'The White House', 2, 'DC', '1600 Pennsylvania Ave', '20500');
insert into ADDRESS values (5002, 'Houston', 'Space Center Houston', 4, 'TX', '1601 NASA Parkway', '77058');
insert into ADDRESS values (5003, 'Chicago', 'The Art Institute of Chicago', 6, 'IL', '111 S Michigan Ave', '60603');
insert into ADDRESS values (5004, 'Bronx', 'Yankee Stadium', 8, 'NY', 'One East 161st Street', '10451');
