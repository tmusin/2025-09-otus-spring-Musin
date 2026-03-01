create table users (
   id bigserial,
   username varchar(255) not null unique,
   password varchar(255) not null,
   role varchar(255) not null default 'USER',
   enabled boolean default true,
   primary key (id)
);

create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

create table comments (
    id bigserial,
    text varchar(1000) not null,
    book_id bigint references books (id) on delete cascade,
    user_id bigint REFERENCES users(id) ON delete set null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS acl_sid (
    id bigint NOT NULL AUTO_INCREMENT,
    principal smallint NOT NULL,
    sid varchar(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS acl_class (
    id bigint NOT NULL AUTO_INCREMENT,
    class varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS acl_entry (
    id bigint NOT NULL AUTO_INCREMENT,
    acl_object_identity bigint NOT NULL,
    ace_order int NOT NULL,
    sid bigint NOT NULL,
    mask int NOT NULL,
    granting smallint NOT NULL,
    audit_success smallint NOT NULL,
    audit_failure smallint NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS acl_object_identity (
    id bigint NOT NULL AUTO_INCREMENT,
    object_id_class bigint NOT NULL,
    object_id_identity bigint NOT NULL,
    parent_object bigint DEFAULT NULL,
    owner_sid bigint DEFAULT NULL,
    entries_inheriting smallint NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);