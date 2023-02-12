- 共享连接+独立schema

```postgresql
CREATE ROLE share_user WITH
    LOGIN
    NOSUPERUSER
    NOINHERIT--设置角色为不继承
    NOCREATEDB
    NOCREATEROLE
    NOREPLICATION;

CREATE ROLE t1 WITH
    LOGIN
    NOSUPERUSER
    NOINHERIT--设置角色为不继承
    NOCREATEDB
    NOCREATEROLE
    NOREPLICATION;

CREATE ROLE t2 WITH
    LOGIN
    NOSUPERUSER
    NOINHERIT--设置角色为不继承
    NOCREATEDB
    NOCREATEROLE
    NOREPLICATION;

GRANT t1, t2 TO share_user;

CREATE DATABASE saas_shared_db
    OWNER = share_user
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE SCHEMA IF NOT EXISTS t1
    AUTHORIZATION t1;


CREATE SCHEMA IF NOT EXISTS t2
    AUTHORIZATION t2;
```

- 独立db,schema

```postgresql
CREATE ROLE t3 WITH
    LOGIN
    NOSUPERUSER
    NOINHERIT--设置角色为不继承
    NOCREATEDB
    NOCREATEROLE
    NOREPLICATION;

CREATE DATABASE t3
    OWNER = t3
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE SCHEMA IF NOT EXISTS t3
    AUTHORIZATION t3;
```

- 表

```postgresql
CREATE TABLE IF NOT EXISTS account
(
    id       bigserial primary key,
    nickname text,
    age      bigint,
    address  text
);
```