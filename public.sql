/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : PostgreSQL
 Source Server Version : 170006 (170006)
 Source Host           : localhost:5432
 Source Catalog        : im_lite
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 170006 (170006)
 File Encoding         : 65001

 Date: 02/10/2025 21:18:06
*/


-- ----------------------------
-- Sequence structure for account_profiles_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."account_profiles_id_seq";
CREATE SEQUENCE "public"."account_profiles_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."account_profiles_id_seq" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for accounts_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."accounts_id_seq";
CREATE SEQUENCE "public"."accounts_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."accounts_id_seq" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for friends_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."friends_id_seq";
CREATE SEQUENCE "public"."friends_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."friends_id_seq" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for group_members_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."group_members_id_seq";
CREATE SEQUENCE "public"."group_members_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."group_members_id_seq" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for group_profiles_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."group_profiles_id_seq";
CREATE SEQUENCE "public"."group_profiles_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."group_profiles_id_seq" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for groups_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."groups_id_seq";
CREATE SEQUENCE "public"."groups_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."groups_id_seq" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for messages_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."messages_id_seq";
CREATE SEQUENCE "public"."messages_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."messages_id_seq" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for session_groups_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."session_groups_id_seq";
CREATE SEQUENCE "public"."session_groups_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."session_groups_id_seq" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for session_members_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."session_members_id_seq";
CREATE SEQUENCE "public"."session_members_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."session_members_id_seq" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for sessions_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sessions_id_seq";
CREATE SEQUENCE "public"."sessions_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."sessions_id_seq" OWNER TO "postgres";

-- ----------------------------
-- Table structure for account_profiles
-- ----------------------------
DROP TABLE IF EXISTS "public"."account_profiles";
CREATE TABLE "public"."account_profiles" (
  "id" int4 NOT NULL DEFAULT nextval('account_profiles_id_seq'::regclass),
  "user_id" int4 NOT NULL,
  "name" varchar(32) COLLATE "pg_catalog"."default",
  "biography" varchar(128) COLLATE "pg_catalog"."default",
  "email" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" int8,
  "update_time" int8,
  "delete_time" int8,
  "avatar_file_id" int8
)
;
ALTER TABLE "public"."account_profiles" OWNER TO "postgres";

-- ----------------------------
-- Table structure for accounts
-- ----------------------------
DROP TABLE IF EXISTS "public"."accounts";
CREATE TABLE "public"."accounts" (
  "id" int4 NOT NULL DEFAULT nextval('accounts_id_seq'::regclass),
  "passwd_hash" bytea,
  "create_time" int8,
  "update_time" int8,
  "delete_time" int8,
  "salt" varchar(255) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."accounts" OWNER TO "postgres";

-- ----------------------------
-- Table structure for client_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."client_data";
CREATE TABLE "public"."client_data" (
  "id" int4 NOT NULL,
  "user_id" int4,
  "client_data" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."client_data" OWNER TO "postgres";

-- ----------------------------
-- Table structure for files
-- ----------------------------
DROP TABLE IF EXISTS "public"."files";
CREATE TABLE "public"."files" (
  "id" int8 NOT NULL,
  "name" varchar(32) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" int8,
  "update_time" int8,
  "delete_time" int8,
  "size" int8,
  "hash" varchar(255) COLLATE "pg_catalog"."default",
  "uploader_id" int4,
  "token" varchar(64) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."files" OWNER TO "postgres";

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS "public"."friends";
CREATE TABLE "public"."friends" (
  "id" int4 NOT NULL DEFAULT nextval('friends_id_seq'::regclass),
  "account_id" int4 NOT NULL,
  "friend_id" int4 NOT NULL,
  "create_time" int8,
  "update_time" int8,
  "delete_time" int8
)
;
ALTER TABLE "public"."friends" OWNER TO "postgres";

-- ----------------------------
-- Table structure for group_members
-- ----------------------------
DROP TABLE IF EXISTS "public"."group_members";
CREATE TABLE "public"."group_members" (
  "id" int4 NOT NULL DEFAULT nextval('group_members_id_seq'::regclass),
  "group_id" int4,
  "member_id" int4,
  "role" varchar(16) COLLATE "pg_catalog"."default",
  "create_time" int8,
  "update_time" int8,
  "delete_time" int8
)
;
ALTER TABLE "public"."group_members" OWNER TO "postgres";

-- ----------------------------
-- Table structure for group_profiles
-- ----------------------------
DROP TABLE IF EXISTS "public"."group_profiles";
CREATE TABLE "public"."group_profiles" (
  "id" int4 NOT NULL DEFAULT nextval('group_profiles_id_seq'::regclass),
  "group_id" int4 NOT NULL,
  "avatar_file_id" int8,
  "name" varchar(32) COLLATE "pg_catalog"."default",
  "biography" varchar(256) COLLATE "pg_catalog"."default",
  "create_time" int8,
  "update_time" int8,
  "delete_time" int8
)
;
ALTER TABLE "public"."group_profiles" OWNER TO "postgres";

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS "public"."groups";
CREATE TABLE "public"."groups" (
  "id" int4 NOT NULL DEFAULT nextval('groups_id_seq'::regclass),
  "create_time" int8,
  "update_time" int8,
  "delete_time" int8
)
;
ALTER TABLE "public"."groups" OWNER TO "postgres";

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS "public"."messages";
CREATE TABLE "public"."messages" (
  "id" int8 NOT NULL,
  "session_id" int4 NOT NULL,
  "type" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "create_time" int8,
  "update_time" int8,
  "delete_time" int8,
  "file_id" int8,
  "file_token" varchar(255) COLLATE "pg_catalog"."default",
  "local_id" int8,
  "user_id" int4
)
;
ALTER TABLE "public"."messages" OWNER TO "postgres";

-- ----------------------------
-- Table structure for session_groups
-- ----------------------------
DROP TABLE IF EXISTS "public"."session_groups";
CREATE TABLE "public"."session_groups" (
  "id" int4 NOT NULL DEFAULT nextval('session_groups_id_seq'::regclass),
  "session_id" int4 NOT NULL,
  "group_id" int4 NOT NULL
)
;
ALTER TABLE "public"."session_groups" OWNER TO "postgres";

-- ----------------------------
-- Table structure for session_members
-- ----------------------------
DROP TABLE IF EXISTS "public"."session_members";
CREATE TABLE "public"."session_members" (
  "id" int4 NOT NULL DEFAULT nextval('session_members_id_seq'::regclass),
  "member_id" int4,
  "session_id" int4
)
;
ALTER TABLE "public"."session_members" OWNER TO "postgres";

-- ----------------------------
-- Table structure for sessions
-- ----------------------------
DROP TABLE IF EXISTS "public"."sessions";
CREATE TABLE "public"."sessions" (
  "id" int4 NOT NULL DEFAULT nextval('sessions_id_seq'::regclass),
  "type" varchar(16) COLLATE "pg_catalog"."default",
  "create_time" int8,
  "update_time" int8,
  "delete_time" int8
)
;
ALTER TABLE "public"."sessions" OWNER TO "postgres";

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."account_profiles_id_seq"
OWNED BY "public"."account_profiles"."id";
SELECT setval('"public"."account_profiles_id_seq"', 2503, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."accounts_id_seq"
OWNED BY "public"."accounts"."id";
SELECT setval('"public"."accounts_id_seq"', 2505, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."friends_id_seq"
OWNED BY "public"."friends"."id";
SELECT setval('"public"."friends_id_seq"', 15, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."group_members_id_seq"', 6, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."group_profiles_id_seq"
OWNED BY "public"."group_profiles"."id";
SELECT setval('"public"."group_profiles_id_seq"', 6, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."groups_id_seq"
OWNED BY "public"."groups"."id";
SELECT setval('"public"."groups_id_seq"', 6, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."messages_id_seq"
OWNED BY "public"."messages"."id";
SELECT setval('"public"."messages_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."session_groups_id_seq"
OWNED BY "public"."session_groups"."id";
SELECT setval('"public"."session_groups_id_seq"', 6, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."session_members_id_seq"
OWNED BY "public"."session_members"."id";
SELECT setval('"public"."session_members_id_seq"', 30, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sessions_id_seq"
OWNED BY "public"."sessions"."id";
SELECT setval('"public"."sessions_id_seq"', 18, true);

-- ----------------------------
-- Indexes structure for table account_profiles
-- ----------------------------
CREATE UNIQUE INDEX "email" ON "public"."account_profiles" USING btree (
  "email" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WITH (FILLFACTOR = 80);
CREATE UNIQUE INDEX "user_id" ON "public"."account_profiles" USING btree (
  "user_id" "pg_catalog"."int4_ops" ASC NULLS LAST
) WITH (FILLFACTOR = 80);

-- ----------------------------
-- Primary Key structure for table account_profiles
-- ----------------------------
ALTER TABLE "public"."account_profiles" ADD CONSTRAINT "user_profile_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table accounts
-- ----------------------------
ALTER TABLE "public"."accounts" ADD CONSTRAINT "account_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table client_data
-- ----------------------------
CREATE UNIQUE INDEX "client_data_user_id_ind" ON "public"."client_data" USING btree (
  "user_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table client_data
-- ----------------------------
ALTER TABLE "public"."client_data" ADD CONSTRAINT "client_data_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table files
-- ----------------------------
CREATE INDEX "files_hash_ind" ON "public"."files" USING btree (
  "hash" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table files
-- ----------------------------
ALTER TABLE "public"."files" ADD CONSTRAINT "files_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table friends
-- ----------------------------
CREATE INDEX "account_id" ON "public"."friends" USING btree (
  "account_id" "pg_catalog"."int4_ops" ASC NULLS LAST
) WITH (FILLFACTOR = 80);
CREATE INDEX "friend_id_ind" ON "public"."friends" USING btree (
  "friend_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table friends
-- ----------------------------
ALTER TABLE "public"."friends" ADD CONSTRAINT "friends_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table group_members
-- ----------------------------
CREATE INDEX "group_id" ON "public"."group_members" USING btree (
  "group_id" "pg_catalog"."int4_ops" ASC NULLS LAST
) WITH (FILLFACTOR = 80);
CREATE INDEX "member_id" ON "public"."group_members" USING btree (
  "member_id" "pg_catalog"."int4_ops" ASC NULLS LAST
) WITH (FILLFACTOR = 80);

-- ----------------------------
-- Primary Key structure for table group_members
-- ----------------------------
ALTER TABLE "public"."group_members" ADD CONSTRAINT "group_members_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table group_profiles
-- ----------------------------
CREATE UNIQUE INDEX "group_id_key" ON "public"."group_profiles" USING btree (
  "group_id" "pg_catalog"."int4_ops" ASC NULLS FIRST
) WITH (FILLFACTOR = 80);

-- ----------------------------
-- Primary Key structure for table group_profiles
-- ----------------------------
ALTER TABLE "public"."group_profiles" ADD CONSTRAINT "group_profile_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table groups
-- ----------------------------
ALTER TABLE "public"."groups" ADD CONSTRAINT "groups_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table messages
-- ----------------------------
CREATE INDEX "create_time" ON "public"."messages" USING btree (
  "create_time" "pg_catalog"."int8_ops" DESC NULLS FIRST
);
CREATE INDEX "messages_file_id_ind" ON "public"."messages" USING btree (
  "file_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "messages_user_id_ind" ON "public"."messages" USING btree (
  "user_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE INDEX "session_id_ind" ON "public"."messages" USING btree (
  "session_id" "pg_catalog"."int4_ops" ASC NULLS FIRST
) WITH (FILLFACTOR = 70);

-- ----------------------------
-- Primary Key structure for table messages
-- ----------------------------
ALTER TABLE "public"."messages" ADD CONSTRAINT "messages_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table session_groups
-- ----------------------------
CREATE UNIQUE INDEX "session_groups_group_id_ind" ON "public"."session_groups" USING btree (
  "group_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "session_groups_session_id_ind" ON "public"."session_groups" USING btree (
  "session_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table session_groups
-- ----------------------------
ALTER TABLE "public"."session_groups" ADD CONSTRAINT "session_groups_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table session_members
-- ----------------------------
CREATE INDEX "member_id_ind" ON "public"."session_members" USING btree (
  "member_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE INDEX "session_id" ON "public"."session_members" USING btree (
  "session_id" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table session_members
-- ----------------------------
ALTER TABLE "public"."session_members" ADD CONSTRAINT "session_members_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sessions
-- ----------------------------
ALTER TABLE "public"."sessions" ADD CONSTRAINT "session_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table account_profiles
-- ----------------------------
ALTER TABLE "public"."account_profiles" ADD CONSTRAINT "avatar_file_id_key" FOREIGN KEY ("avatar_file_id") REFERENCES "public"."files" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."account_profiles" ADD CONSTRAINT "user_id" FOREIGN KEY ("user_id") REFERENCES "public"."accounts" ("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Keys structure for table client_data
-- ----------------------------
ALTER TABLE "public"."client_data" ADD CONSTRAINT "client_data_user_id_key" FOREIGN KEY ("user_id") REFERENCES "public"."accounts" ("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Keys structure for table files
-- ----------------------------
ALTER TABLE "public"."files" ADD CONSTRAINT "uploader_id_account_id_key" FOREIGN KEY ("uploader_id") REFERENCES "public"."accounts" ("id") ON DELETE SET NULL ON UPDATE SET NULL;

-- ----------------------------
-- Foreign Keys structure for table friends
-- ----------------------------
ALTER TABLE "public"."friends" ADD CONSTRAINT "account_id" FOREIGN KEY ("account_id") REFERENCES "public"."accounts" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."friends" ADD CONSTRAINT "friend_id" FOREIGN KEY ("friend_id") REFERENCES "public"."accounts" ("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Keys structure for table group_members
-- ----------------------------
ALTER TABLE "public"."group_members" ADD CONSTRAINT "group_member_group_id_key" FOREIGN KEY ("group_id") REFERENCES "public"."groups" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."group_members" ADD CONSTRAINT "group_member_id_key" FOREIGN KEY ("member_id") REFERENCES "public"."accounts" ("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Keys structure for table group_profiles
-- ----------------------------
ALTER TABLE "public"."group_profiles" ADD CONSTRAINT "group_id_groups" FOREIGN KEY ("group_id") REFERENCES "public"."groups" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."group_profiles" ADD CONSTRAINT "group_profile_avatar_file_id_key" FOREIGN KEY ("avatar_file_id") REFERENCES "public"."files" ("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Keys structure for table messages
-- ----------------------------
ALTER TABLE "public"."messages" ADD CONSTRAINT "messages_file_id_key" FOREIGN KEY ("file_id") REFERENCES "public"."files" ("id") ON DELETE SET NULL ON UPDATE SET NULL;
ALTER TABLE "public"."messages" ADD CONSTRAINT "messages_user_id_key" FOREIGN KEY ("user_id") REFERENCES "public"."accounts" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."messages" ADD CONSTRAINT "session_id_key" FOREIGN KEY ("session_id") REFERENCES "public"."sessions" ("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Keys structure for table session_groups
-- ----------------------------
ALTER TABLE "public"."session_groups" ADD CONSTRAINT "group_id_key" FOREIGN KEY ("group_id") REFERENCES "public"."groups" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."session_groups" ADD CONSTRAINT "session_id_key" FOREIGN KEY ("session_id") REFERENCES "public"."sessions" ("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Keys structure for table session_members
-- ----------------------------
ALTER TABLE "public"."session_members" ADD CONSTRAINT "session_members_member_id_key" FOREIGN KEY ("member_id") REFERENCES "public"."accounts" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."session_members" ADD CONSTRAINT "session_members_session_id_key" FOREIGN KEY ("session_id") REFERENCES "public"."sessions" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
