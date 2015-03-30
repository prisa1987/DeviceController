# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table device (
  device_id                 bigint not null,
  name                      varchar(255),
  status                    boolean,
  com_port                  integer,
  constraint pk_device primary key (device_id))
;

create table permission (
  permission_id             bigint not null,
  user_user_id              bigint,
  device_device_id          bigint,
  status                    boolean,
  constraint pk_permission primary key (permission_id))
;

create table request (
  request_id                bigint not null,
  user_user_id              bigint,
  device_device_id          bigint,
  date                      timestamp,
  status                    integer,
  constraint ck_request_status check (status in (0,1,2)),
  constraint pk_request primary key (request_id))
;

create table room (
  room_id                   bigint not null,
  name                      varchar(255),
  constraint pk_room primary key (room_id))
;

create table user (
  user_id                   bigint not null,
  name                      varchar(255),
  phone_number              varchar(255),
  client_id                 varchar(255),
  date                      timestamp,
  role                      integer,
  constraint ck_user_role check (role in (0,1)),
  constraint pk_user primary key (user_id))
;


create table room_device (
  room_room_id                   bigint not null,
  device_device_id               bigint not null,
  constraint pk_room_device primary key (room_room_id, device_device_id))
;
create sequence device_seq;

create sequence permission_seq;

create sequence request_seq;

create sequence room_seq;

create sequence user_seq;

alter table permission add constraint fk_permission_user_1 foreign key (user_user_id) references user (user_id) on delete restrict on update restrict;
create index ix_permission_user_1 on permission (user_user_id);
alter table permission add constraint fk_permission_device_2 foreign key (device_device_id) references device (device_id) on delete restrict on update restrict;
create index ix_permission_device_2 on permission (device_device_id);
alter table request add constraint fk_request_user_3 foreign key (user_user_id) references user (user_id) on delete restrict on update restrict;
create index ix_request_user_3 on request (user_user_id);
alter table request add constraint fk_request_device_4 foreign key (device_device_id) references device (device_id) on delete restrict on update restrict;
create index ix_request_device_4 on request (device_device_id);



alter table room_device add constraint fk_room_device_room_01 foreign key (room_room_id) references room (room_id) on delete restrict on update restrict;

alter table room_device add constraint fk_room_device_device_02 foreign key (device_device_id) references device (device_id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists device;

drop table if exists permission;

drop table if exists request;

drop table if exists room;

drop table if exists room_device;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists device_seq;

drop sequence if exists permission_seq;

drop sequence if exists request_seq;

drop sequence if exists room_seq;

drop sequence if exists user_seq;

