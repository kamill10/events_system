GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE public.account TO ssbd01mok;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE public.personal_data TO ssbd01mok;
GRANT SELECT ON TABLE public.role TO ssbd01mok;
GRANT SELECT, INSERT, DELETE ON TABLE public.account_role TO ssbd01mok;
GRANT SELECT, INSERT, DELETE ON TABLE public.password_history TO ssbd01mok;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE public.accountconfirmation TO ssbd01mok;
GRANT SELECT, INSERT, DELETE ON TABLE public.confirmation_reminder TO ssbd01mok;
GRANT SELECT, INSERT, DELETE ON TABLE credential_reset TO ssbd01mok;
GRANT SELECT, INSERT, DELETE ON TABLE change_my_password TO ssbd01mok;
GRANT SELECT, INSERT, DELETE ON TABLE change_email TO ssbd01mok;
GRANT SELECT  ON TABLE account_theme TO ssbd01mok;
GRANT SELECT ON TABLE account_time_zone TO ssbd01mok;
GRANT SELECT  ON TABLE account_theme TO ssbd01auth;
GRANT SELECT ON TABLE account_time_zone TO ssbd01auth;

GRANT SELECT, INSERT, DELETE ON TABLE public.jwt_whitelist_token TO ssbd01auth;
GRANT SELECT, UPDATE ON TABLE public.account TO ssbd01auth;
GRANT SELECT ON TABLE public.role TO ssbd01auth;
GRANT SELECT ON TABLE public.account_role TO ssbd01auth;
GRANT SELECT ON TABLE public.personal_data TO ssbd01auth;


GRANT SELECT ON TABLE public.account TO ssbd01mow;

GRANT SELECT, INSERT, UPDATE ON TABLE public.event TO ssbd01mow;
GRANT SELECT, INSERT, UPDATE ON TABLE public.session TO ssbd01mow;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.location TO ssbd01mow;
GRANT SELECT, INSERT, DELETE ON TABLE public.room TO ssbd01mow;

GRANT SELECT, INSERT, UPDATE ON TABLE public.speaker TO ssbd01mow;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.ticket TO ssbd01mow;


CREATE INDEX account_role_account_idx ON account_role USING btree (account_id);
CREATE INDEX account_role_role_idx ON account_role USING btree (roles_id);
CREATE INDEX event_created_by_idx ON event USING btree (created_by);
CREATE INDEX event_updated_by_idx ON event USING btree (updated_by);
CREATE INDEX location_created_by_idx ON location USING btree (created_by);
CREATE INDEX location_updated_by_idx ON location USING btree (updated_by);
CREATE INDEX room_created_by_idx ON room USING btree (created_by);
CREATE INDEX room_location_id_idx ON room USING btree (location_id);
CREATE INDEX room_updated_by_idx ON room USING btree (updated_by);
CREATE INDEX session_created_by_idx ON session USING btree (created_by);
CREATE INDEX session_event_id_idx ON session USING btree (event_id);
CREATE INDEX session_room_id_idx ON session USING btree (room_id);
CREATE INDEX session_speaker_id_idx ON session USING btree (speaker_id);
CREATE INDEX session_updated_by_idx ON session USING btree (updated_by);
CREATE INDEX speaker_created_by_idx ON speaker USING btree (created_by);
CREATE INDEX speaker_updated_by_idx ON speaker USING btree (updated_by);
CREATE INDEX ticket_account_id_idx ON ticket USING btree (account_id);
CREATE INDEX ticket_created_by_idx ON ticket USING btree (created_by);
CREATE INDEX ticket_session_id_idx ON ticket USING btree (session_id);
CREATE INDEX ticket_updated_by_idx ON ticket USING btree (updated_by);

ALTER TABLE account_role ADD UNIQUE (account_id, roles_id);


INSERT INTO public.role (id, version, name) VALUES ('550e8400-e29b-41d4-a716-446655440000', 0, 'ROLE_ADMIN');
INSERT INTO public.role (id, version, name) VALUES ('4c90f86a-0d82-4c51-b72c-80e20949a3b9', 0, 'ROLE_MANAGER');
INSERT INTO public.role (id, version, name) VALUES ('cd8ab1c1-2431-4e28-88b5-fdd54de3d92a', 0, 'ROLE_PARTICIPANT');

INSERT INTO public.account_theme (id, version, theme) VALUES ('666e8400-e29b-41d4-a716-446655440000', 0, 'LIGHT');
INSERT INTO public.account_theme (id, version, theme) VALUES ('777e8400-e29b-41d4-a716-446655440000', 0, 'DARK');

INSERT INTO public.account_time_zone (id, version, timeZoneEnum) VALUES ('888e8400-e29b-41d4-a716-446655440000', 0, 'WARSAW');
INSERT INTO public.account_time_zone (id, version, timeZoneEnum) VALUES ('999e8400-e29b-41d4-a716-446655440000', 0, 'LONDON');

INSERT INTO public.account (id,username,password,active,verified, nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1','testAdmin','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1', 'Pudzian', 'Admin', 'admin202401@proton.me', 1);
INSERT INTO public.account_role (account_id,roles_id) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1','550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'8b25c94f-f10f-4285-8eb2-39ee1c4002f1','2f3e920d-8c7a-4a15-a4d7-1ae28e5930b6','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id,username,password,active,verified,nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','testParticipant','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6', 'Test', 'Participant', 'participant202401@proton.me', 1);
INSERT INTO public.account_role (account_id,roles_id) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','67f7f35b-03d9-45df-b2d6-81cb6d63113f','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id,username,password,active,verified,nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157','testManager','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157', 'Test', 'Manager', 'manager202401@proton.me', 1);
INSERT INTO public.account_role (account_id,roles_id) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157','4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'5454d58c-6ae2-4eee-8980-a49a1664f157','b57d5a3f-aae0-4a3d-9315-1e5e16b1f28d','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');