# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table project (
  id                            uuid not null,
  name                          varchar(255) not null,
  constraint pk_project primary key (id)
);

create table project_user (
  user_id                       uuid not null,
  project_id                    uuid not null,
  is_owner                      boolean default false not null,
  can_write                     boolean default false not null
);

create table users (
  id                            uuid not null,
  name                          varchar(255) not null,
  email                         varchar(255) not null,
  password                      varchar(255) not null,
  constraint pk_users primary key (id)
);

create index ix_project_user_user_id on project_user (user_id);
alter table project_user add constraint fk_project_user_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;

create index ix_project_user_project_id on project_user (project_id);
alter table project_user add constraint fk_project_user_project_id foreign key (project_id) references project (id) on delete restrict on update restrict;


# --- !Downs

alter table if exists project_user drop constraint if exists fk_project_user_user_id;
drop index if exists ix_project_user_user_id;

alter table if exists project_user drop constraint if exists fk_project_user_project_id;
drop index if exists ix_project_user_project_id;

drop table if exists project cascade;

drop table if exists project_user cascade;

drop table if exists users cascade;

