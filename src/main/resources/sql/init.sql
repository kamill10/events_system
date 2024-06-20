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
GRANT SELECT, INSERT ON TABLE account_theme TO ssbd01mok;
GRANT SELECT, INSERT ON TABLE account_time_zone TO ssbd01mok;
GRANT SELECT  ON TABLE account_theme TO ssbd01auth;
GRANT SELECT ON TABLE account_time_zone TO ssbd01auth;
GRANT SELECT, INSERT, DELETE ON TABLE public.account_unlock TO ssbd01mok;
GRANT SELECT, INSERT ON TABLE public.account_history TO ssbd01mok;
GRANT SELECT, INSERT ON TABLE public.account_history_role TO ssbd01mok;
GRANT SELECT, INSERT ON TABLE public.personal_data_history TO ssbd01mok;

GRANT SELECT, INSERT, DELETE ON TABLE public.jwt_whitelist_token TO ssbd01auth;
GRANT SELECT, UPDATE ON TABLE public.account TO ssbd01auth;
GRANT SELECT ON TABLE public.role TO ssbd01auth;
GRANT SELECT ON TABLE public.account_role TO ssbd01auth;
GRANT SELECT ON TABLE public.personal_data TO ssbd01auth;
GRANT SELECT, INSERT ON TABLE public.account_history TO ssbd01auth;
GRANT SELECT, INSERT ON TABLE public.account_history_role TO ssbd01auth;
GRANT SELECT, INSERT ON TABLE public.personal_data_history TO ssbd01auth;

GRANT SELECT ON TABLE public.account TO ssbd01mow;
GRANT SELECT ON TABLE public.personal_data TO ssbd01mow;
GRANT SELECT ON TABLE public.account_role TO ssbd01mow;
GRANT SELECT ON TABLE public.role TO ssbd01mow;

GRANT SELECT, INSERT, UPDATE ON TABLE public.event TO ssbd01mow;
GRANT SELECT, INSERT, UPDATE ON TABLE public.session TO ssbd01mow;

GRANT SELECT, INSERT, UPDATE ON TABLE public.location TO ssbd01mow;
GRANT SELECT, INSERT, UPDATE ON TABLE public.room TO ssbd01mow;

GRANT SELECT, INSERT, UPDATE ON TABLE public.speaker TO ssbd01mow;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.ticket TO ssbd01mow;

GRANT SELECT, INSERT ON TABLE public.speaker_history TO ssbd01mow;


CREATE INDEX account_created_by_idx ON account USING btree (created_by);
CREATE INDEX account_updated_by_idx ON account USING btree (updated_by);
CREATE INDEX account_theme_account_id_idx ON account USING btree (accounttheme_id);
CREATE INDEX account_time_zone_account_id_idx ON account USING btree (accounttimezone_id);

CREATE INDEX account_history_account_id_idx ON account_history USING btree (account_id);
CREATE INDEX account_history_created_by_idx ON account_history USING btree (created_by);
CREATE INDEX account_history_updated_by_idx ON account_history USING btree (updated_by);
CREATE INDEX account_history_account_theme_idx ON account_history USING btree (accounttheme_id);
CREATE INDEX account_history_account_time_zone_idx ON account_history USING btree (accounttimezone_id);

CREATE INDEX account_history_role_account_idx ON account_history_role USING btree (accounthistory_id);
CREATE INDEX account_history_role_role_idx ON account_history_role USING btree (roles_id);

CREATE INDEX account_role_account_idx ON account_role USING btree (account_id);
CREATE INDEX account_role_role_idx ON account_role USING btree (roles_id);

CREATE INDEX change_email_created_by_idx ON change_email USING btree (created_by);
CREATE INDEX change_email_updated_by_idx ON change_email USING btree (updated_by);

CREATE INDEX change_my_password_created_by_idx ON change_my_password USING btree (created_by);
CREATE INDEX change_my_password_updated_by_idx ON change_my_password USING btree (updated_by);

CREATE INDEX credential_reset_created_by_idx ON credential_reset USING btree (created_by);
CREATE INDEX credential_reset_updated_by_idx ON credential_reset USING btree (updated_by);

CREATE INDEX event_created_by_idx ON event USING btree (created_by);
CREATE INDEX event_updated_by_idx ON event USING btree (updated_by);

CREATE INDEX location_created_by_idx ON location USING btree (created_by);
CREATE INDEX location_updated_by_idx ON location USING btree (updated_by);

CREATE INDEX password_history_account_id_idx ON password_history USING btree (account_id);

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
CREATE INDEX speaker_history_speaker_id_idx ON speaker_history USING btree (speaker_id);
CREATE INDEX speaker_history_created_by_idx ON speaker_history USING btree (created_by);
CREATE INDEX speaker_history_updated_by_idx ON speaker_history USING btree (updated_by);

CREATE INDEX ticket_account_id_idx ON ticket USING btree (account_id);
CREATE INDEX ticket_created_by_idx ON ticket USING btree (created_by);
CREATE INDEX ticket_session_id_idx ON ticket USING btree (session_id);
CREATE INDEX ticket_updated_by_idx ON ticket USING btree (updated_by);


ALTER TABLE account_role ADD UNIQUE (account_id, roles_id);
ALTER TABLE ticket ADD UNIQUE (account_id, session_id);


INSERT INTO public.role (id, version, name) VALUES ('550e8400-e29b-41d4-a716-446655440000', 0, 'ROLE_ADMIN');
INSERT INTO public.role (id, version, name) VALUES ('4c90f86a-0d82-4c51-b72c-80e20949a3b9', 0, 'ROLE_MANAGER');
INSERT INTO public.role (id, version, name) VALUES ('cd8ab1c1-2431-4e28-88b5-fdd54de3d92a', 0, 'ROLE_PARTICIPANT');

INSERT INTO public.account_theme (id, version, theme) VALUES ('666e8400-e29b-41d4-a716-446655440000', 0, 'Light');
INSERT INTO public.account_theme (id, version, theme) VALUES ('777e8400-e29b-41d4-a716-446655440000', 0, 'Dark');

INSERT INTO public.account_time_zone (id, version, timezone) VALUES ('888e8400-e29b-41d4-a716-446655440000', 0, 'Europe/Warsaw');
INSERT INTO public.account_time_zone (id, version, timezone) VALUES ('999e8400-e29b-41d4-a716-446655440000', 0, 'Europe/London');

INSERT INTO public.account (id,username,password,active,verified, nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1','testAdmin','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1', 'Maciej', 'Wilnczynski-Druciarz', 'admin202401@proton.me', 1);
INSERT INTO public.account_role (account_id,roles_id) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1','550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.account_history (id,account_id,username,password,active,verified, nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('03a33b01-6524-4eac-8d8d-7a9f6ab3d034','8b25c94f-f10f-4285-8eb2-39ee1c4002f1','testAdmin','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('03a33b01-6524-4eac-8d8d-7a9f6ab3d034', 'Pudzian', 'Admin', 'admin202401@proton.me', 1);
INSERT INTO public.account_history_role (accounthistory_id,roles_id) VALUES ('03a33b01-6524-4eac-8d8d-7a9f6ab3d034','550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'8b25c94f-f10f-4285-8eb2-39ee1c4002f1','2f3e920d-8c7a-4a15-a4d7-1ae28e5930b6','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id,username,password,active,verified,nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe7','testParticipant','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe7', 'Maciej', 'Druciarz_Wilczynski', 'participant202401@proton.me', 1);
INSERT INTO public.account_role (account_id,roles_id) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe7','cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id,account_id,username,password,active,verified,nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('a8cf7cf9-5c57-4a46-ad28-6698c13ef947','a8816c75-e735-4d16-9f3e-7fcf3d0e7fe7','testParticipant','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('a8cf7cf9-5c57-4a46-ad28-6698c13ef947', 'Test', 'Participant', 'participant202401@proton.me', 1);
INSERT INTO public.account_history_role (accounthistory_id,roles_id) VALUES ('a8cf7cf9-5c57-4a46-ad28-6698c13ef947','cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'a8816c75-e735-4d16-9f3e-7fcf3d0e7fe7','67f7f35b-03d9-45df-b2d6-81cb6d63113f','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id,username,password,active,verified,nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157','testManager','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157', 'Test', 'Manager', 'manager_ssbd01@proton.me', 1);
INSERT INTO public.account_role (account_id,roles_id) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157','4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.account_history (id,account_id,username,password,active,verified,nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('34badb9e-5c5e-430a-82fa-bc9444da85a5','5454d58c-6ae2-4eee-8980-a49a1664f157','testManager','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('34badb9e-5c5e-430a-82fa-bc9444da85a5', 'Test', 'Manager', 'manager_ssbd01@proton.me', 1);
INSERT INTO public.account_history_role (accounthistory_id,roles_id) VALUES ('34badb9e-5c5e-430a-82fa-bc9444da85a5','4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'5454d58c-6ae2-4eee-8980-a49a1664f157','b57d5a3f-aae0-4a3d-9315-1e5e16b1f28d','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('44073ec1-5db0-48bd-ae42-15298b22dcea', 'witoldgombrowicz12', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-01-15 10:23:45', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('44073ec1-5db0-48bd-ae42-15298b22dcea', 'Witold', 'Gombrowicz', 'witoldgombrowicz12@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('44073ec1-5db0-48bd-ae42-15298b22dcea', '4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('0226059a-cdf7-4aa0-92eb-71619fd800b8', '44073ec1-5db0-48bd-ae42-15298b22dcea', 'witoldgombrowicz12', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-01-15 10:23:45', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('0226059a-cdf7-4aa0-92eb-71619fd800b8', 'Witold', 'Gombrowicz', 'witoldgombrowicz12@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('0226059a-cdf7-4aa0-92eb-71619fd800b8', '4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '44073ec1-5db0-48bd-ae42-15298b22dcea', '7829bafb-be80-4bd2-91e8-fb6eb44c717b', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('1aeac6a9-17d3-4962-9508-a8cf960101b2', 'agnieszkaosiecka23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-20 14:56:32', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('1aeac6a9-17d3-4962-9508-a8cf960101b2', 'Agnieszka', 'Osiecka', 'agnieszkaosiecka23@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('1aeac6a9-17d3-4962-9508-a8cf960101b2', '4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('27839ada-745b-4f6d-8ec8-f9afa18a63bb', '1aeac6a9-17d3-4962-9508-a8cf960101b2', 'agnieszkaosiecka23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-20 14:56:32', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('27839ada-745b-4f6d-8ec8-f9afa18a63bb', 'Agnieszka', 'Osiecka', 'agnieszkaosiecka23@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('27839ada-745b-4f6d-8ec8-f9afa18a63bb', '4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '1aeac6a9-17d3-4962-9508-a8cf960101b2', '3b206cfc-d154-4233-a891-f87d1889904b', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('781127ce-3665-4750-846e-7803b006c7dd', 'stanislawmrozek44', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-03-05 08:12:11', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('781127ce-3665-4750-846e-7803b006c7dd', 'Stanisław', 'Mrożek', 'stanislawmrozek44@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('781127ce-3665-4750-846e-7803b006c7dd', '550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('7711475a-8190-4af0-8e93-2c9620b03e81', '781127ce-3665-4750-846e-7803b006c7dd', 'stanislawmrozek44', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-03-05 08:12:11', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('7711475a-8190-4af0-8e93-2c9620b03e81', 'Stanisław', 'Mrożek', 'stanislawmrozek44@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('7711475a-8190-4af0-8e93-2c9620b03e81', '550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '781127ce-3665-4750-846e-7803b006c7dd', '370045a3-53ab-48ea-a7a4-6c74d813ba29', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('3aeb5f95-0d43-424e-9614-def7c2307445', 'jeremiprzybora23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-17 16:45:23', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('3aeb5f95-0d43-424e-9614-def7c2307445', 'Jeremi', 'Przybora', 'jeremiprzybora23@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('3aeb5f95-0d43-424e-9614-def7c2307445', '550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('f25f2318-e01a-4dbe-a917-7189d36e3e17', '3aeb5f95-0d43-424e-9614-def7c2307445', 'jeremiprzybora23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-17 16:45:23', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('f25f2318-e01a-4dbe-a917-7189d36e3e17', 'Jeremi', 'Przybora', 'jeremiprzybora23@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('f25f2318-e01a-4dbe-a917-7189d36e3e17', '550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '3aeb5f95-0d43-424e-9614-def7c2307445', '609ca93d-d456-437d-9f9d-eb9ea2799393', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('57358248-2a68-49c0-bb5a-29b23755903b', 'stanislawlem51', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-05-23 12:34:56', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('57358248-2a68-49c0-bb5a-29b23755903b', 'Stanisław', 'Lem', 'stanislawlem51@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('57358248-2a68-49c0-bb5a-29b23755903b', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('f61fc204-1a79-406f-8e4e-7dbf73ecad99', '57358248-2a68-49c0-bb5a-29b23755903b', 'stanislawlem51', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-05-23 12:34:56', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('f61fc204-1a79-406f-8e4e-7dbf73ecad99', 'Stanisław', 'Lem', 'stanislawlem51@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('f61fc204-1a79-406f-8e4e-7dbf73ecad99', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '57358248-2a68-49c0-bb5a-29b23755903b', '61e884d5-71cd-48d9-954b-e94a1501509f', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('537d27df-c557-445b-bfb3-2e2adf9d95ef', 'konstantygalczynski124', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-10 09:28:44', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('537d27df-c557-445b-bfb3-2e2adf9d95ef', 'Konstanty', 'Gałczyński', 'konstantygalczynski124@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('537d27df-c557-445b-bfb3-2e2adf9d95ef', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('93b6e60d-c300-4f55-a64d-6b6f22f9350a', '537d27df-c557-445b-bfb3-2e2adf9d95ef', 'konstantygalczynski124', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-10 09:28:44', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('93b6e60d-c300-4f55-a64d-6b6f22f9350a', 'Konstanty', 'Gałczyński', 'konstantygalczynski124@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('93b6e60d-c300-4f55-a64d-6b6f22f9350a', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '537d27df-c557-445b-bfb3-2e2adf9d95ef', 'f9dddcb1-7d52-4e3c-8004-a0ac778109a6', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('08839a96-9432-4ada-a9bd-aa6a59d460ec', 'halinaposwiatoswka12', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-20 18:55:12', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('08839a96-9432-4ada-a9bd-aa6a59d460ec', 'Halina', 'Poświatowska', 'halinaposwiatoswka12@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('08839a96-9432-4ada-a9bd-aa6a59d460ec', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('814f5aca-5590-4a13-99f4-c1ed9ad70b11', '08839a96-9432-4ada-a9bd-aa6a59d460ec', 'halinaposwiatoswka12', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-20 18:55:12', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('814f5aca-5590-4a13-99f4-c1ed9ad70b11', 'Halina', 'Poświatowska', 'halinaposwiatoswka12@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('814f5aca-5590-4a13-99f4-c1ed9ad70b11', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '08839a96-9432-4ada-a9bd-aa6a59d460ec', 'c06a158b-2a06-4bad-a53d-60f315f7e75e', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('0ebf010f-1655-4090-bb31-2802574c09e9', 'aleksanderchwat31', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-01-25 21:43:54', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('0ebf010f-1655-4090-bb31-2802574c09e9', 'Aleksander', 'Chwat', 'aleksanderchwat31@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('0ebf010f-1655-4090-bb31-2802574c09e9', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('011458b9-c739-464a-a031-5cd5c5bbb5fa', '0ebf010f-1655-4090-bb31-2802574c09e9', 'aleksanderchwat31', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-01-25 21:43:54', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('011458b9-c739-464a-a031-5cd5c5bbb5fa', 'Aleksander', 'Chwat', 'aleksanderchwat31@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('011458b9-c739-464a-a031-5cd5c5bbb5fa', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '0ebf010f-1655-4090-bb31-2802574c09e9', '5677c8f2-9518-428e-b5d2-55257c007927', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('0d3d8632-0ed6-4e43-8021-633c5c7e66c5', 'wandaRutkiewicz21', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-01 09:10:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('0d3d8632-0ed6-4e43-8021-633c5c7e66c5', 'Wanda', 'Rutkiewicz', 'wandaRutkiewicz21@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('0d3d8632-0ed6-4e43-8021-633c5c7e66c5', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('3d4c2770-7426-43fe-a7c9-d97c8aadce5e', '0d3d8632-0ed6-4e43-8021-633c5c7e66c5', 'wandaRutkiewicz21', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-01 09:10:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('3d4c2770-7426-43fe-a7c9-d97c8aadce5e', 'Wanda', 'Rutkiewicz', 'wandaRutkiewicz21@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('3d4c2770-7426-43fe-a7c9-d97c8aadce5e', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '0d3d8632-0ed6-4e43-8021-633c5c7e66c5', 'd8fdda9f-8818-4fab-9e41-381a69fa7c46', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('a8a48455-5df0-442c-a63b-be4f63fef56a', 'jerzyKukuczka22', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-02 11:12:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('a8a48455-5df0-442c-a63b-be4f63fef56a', 'Jerzy', 'Kukuczka', 'jerzyKukuczka22@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('a8a48455-5df0-442c-a63b-be4f63fef56a', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('7e5d2183-9801-47d9-8cfb-d8e81872e70a', 'a8a48455-5df0-442c-a63b-be4f63fef56a', 'jerzyKukuczka22', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-02 11:12:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('7e5d2183-9801-47d9-8cfb-d8e81872e70a', 'Jerzy', 'Kukuczka', 'jerzyKukuczka22@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('7e5d2183-9801-47d9-8cfb-d8e81872e70a', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'a8a48455-5df0-442c-a63b-be4f63fef56a', 'a639c6e6-5f0a-44eb-ab95-6a5e4d67d9f7', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('e74664b8-5ba6-49c4-a294-078ef52cb999', 'krzysztofWielicki23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-03 13:14:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('e74664b8-5ba6-49c4-a294-078ef52cb999', 'Krzysztof', 'Wielicki', 'krzysztofWielicki23@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('e74664b8-5ba6-49c4-a294-078ef52cb999', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('0f5635a4-4748-48b3-9964-c3ddee0548d3', 'e74664b8-5ba6-49c4-a294-078ef52cb999', 'krzysztofWielicki23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-03 13:14:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('0f5635a4-4748-48b3-9964-c3ddee0548d3', 'Krzysztof', 'Wielicki', 'krzysztofWielicki23@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('0f5635a4-4748-48b3-9964-c3ddee0548d3', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'e74664b8-5ba6-49c4-a294-078ef52cb999', '43f1a9a9-32cc-453d-9207-d7051b44cd3b', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('d8e6f8f3-3776-4902-9b25-76c3fcff7541', 'wojciechKurtyka24', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-04 15:16:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('d8e6f8f3-3776-4902-9b25-76c3fcff7541', 'Wojciech', 'Kurtyka', 'wojciechKurtyka24@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('d8e6f8f3-3776-4902-9b25-76c3fcff7541', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('c3e43521-2fbb-4c74-8500-363389fa1f48', 'd8e6f8f3-3776-4902-9b25-76c3fcff7541', 'wojciechKurtyka24', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-04 15:16:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('c3e43521-2fbb-4c74-8500-363389fa1f48', 'Wojciech', 'Kurtyka', 'wojciechKurtyka24@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('c3e43521-2fbb-4c74-8500-363389fa1f48', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'd8e6f8f3-3776-4902-9b25-76c3fcff7541', '263a032e-94ce-49ed-8ed3-c73b155631be', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('fc684830-af98-4723-93b1-8263a1680bba', 'piotrPustelnik25', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-05 17:18:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('fc684830-af98-4723-93b1-8263a1680bba', 'Piotr', 'Pustelnik', 'piotrPustelnik25@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('fc684830-af98-4723-93b1-8263a1680bba', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('a54a81ff-676f-43f5-a830-a5682a66a14b', 'fc684830-af98-4723-93b1-8263a1680bba', 'piotrPustelnik25', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-05 17:18:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('a54a81ff-676f-43f5-a830-a5682a66a14b', 'Piotr', 'Pustelnik', 'piotrPustelnik25@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('a54a81ff-676f-43f5-a830-a5682a66a14b', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'fc684830-af98-4723-93b1-8263a1680bba', 'f482b6f2-5153-43a7-ba3d-a9e93139c66a', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('8e569ce7-e388-471a-a4a1-7fb557a3684a', 'adamBielecki27', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-06 19:20:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('8e569ce7-e388-471a-a4a1-7fb557a3684a', 'Adam', 'Bielecki', 'adamBielecki27@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('8e569ce7-e388-471a-a4a1-7fb557a3684a', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('499a6460-32c6-4539-b075-b7d9e67e5e11', '8e569ce7-e388-471a-a4a1-7fb557a3684a', 'adamBielecki27', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-06 19:20:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('499a6460-32c6-4539-b075-b7d9e67e5e11', 'Adam', 'Bielecki', 'adamBielecki27@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('499a6460-32c6-4539-b075-b7d9e67e5e11', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '8e569ce7-e388-471a-a4a1-7fb557a3684a', 'c7e4d1e5-933b-41d5-b54a-266afe127455', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('289da33b-54e7-4f54-8f5d-8618ddab970f', 'maciejBerbeka27', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-07 21:22:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('289da33b-54e7-4f54-8f5d-8618ddab970f', 'Maciej', 'Berbeka', 'maciejBerbeka27@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('289da33b-54e7-4f54-8f5d-8618ddab970f', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('79d611d5-1be4-4dc3-a871-9c9247c59cbf', '289da33b-54e7-4f54-8f5d-8618ddab970f', 'maciejBerbeka27', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-07 21:22:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('79d611d5-1be4-4dc3-a871-9c9247c59cbf', 'Maciej', 'Berbeka', 'maciejBerbeka27@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('79d611d5-1be4-4dc3-a871-9c9247c59cbf', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '289da33b-54e7-4f54-8f5d-8618ddab970f', 'e921c16e-8e65-4447-af5f-f83bb8c8ee08', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('edaf09f4-6293-4216-9cc1-1d792107c333', 'andrzejZawada29', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-08 23:24:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('edaf09f4-6293-4216-9cc1-1d792107c333', 'Andrzej', 'Zawada', 'andrzejZawada29@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('edaf09f4-6293-4216-9cc1-1d792107c333', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('0dc0b271-9879-408e-aab7-402581ba5159', 'edaf09f4-6293-4216-9cc1-1d792107c333', 'andrzejZawada29', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-08 23:24:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('0dc0b271-9879-408e-aab7-402581ba5159', 'Andrzej', 'Zawada', 'andrzejZawada29@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('0dc0b271-9879-408e-aab7-402581ba5159', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'edaf09f4-6293-4216-9cc1-1d792107c333', 'a535b82e-733a-4e9a-962f-bc6361413ed4', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('19f3126c-34a4-4ef4-a0ae-00f56343977e', 'januszMajer31', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 01:26:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('19f3126c-34a4-4ef4-a0ae-00f56343977e', 'Janusz', 'Majer', 'januszMajer31@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('19f3126c-34a4-4ef4-a0ae-00f56343977e', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('892ff64d-ebad-4e51-92eb-1ca189f772fd', '19f3126c-34a4-4ef4-a0ae-00f56343977e', 'januszMajer31', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 01:26:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('892ff64d-ebad-4e51-92eb-1ca189f772fd', 'Janusz', 'Majer', 'januszMajer31@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('892ff64d-ebad-4e51-92eb-1ca189f772fd', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '19f3126c-34a4-4ef4-a0ae-00f56343977e', 'a3e65725-1804-40cf-8fdd-1c41f1ea0dcf', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'arturHajzer32', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 03:28:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'Artur', 'Hajzer', 'arturHajzer32@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('76d5e986-8bea-4b22-996f-356d24f560df', 'be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'arturHajzer32', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 03:28:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('76d5e986-8bea-4b22-996f-356d24f560df', 'Artur', 'Hajzer', 'arturHajzer32@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('76d5e986-8bea-4b22-996f-356d24f560df', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', '23137368-d913-40c2-9f8b-d28e5f1d6b8d', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('d0d93942-9ea6-48fb-9762-27105b8b9f40', 'tadeuszPiotrowski33', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 05:30:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('d0d93942-9ea6-48fb-9762-27105b8b9f40', 'Tadeusz', 'Piotrowski', 'tadeuszPiotrowski33@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('d0d93942-9ea6-48fb-9762-27105b8b9f40', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('7e61f5c8-4183-4094-8212-defce0b47e71', 'd0d93942-9ea6-48fb-9762-27105b8b9f40', 'tadeuszPiotrowski33', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 05:30:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('7e61f5c8-4183-4094-8212-defce0b47e71', 'Tadeusz', 'Piotrowski', 'tadeuszPiotrowski33@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('7e61f5c8-4183-4094-8212-defce0b47e71', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'd0d93942-9ea6-48fb-9762-27105b8b9f40', '92242433-7cc9-4c62-9a12-5805a27a092c', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'eugeniuszChrobak34', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 07:32:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'Eugeniusz', 'Chrobak', 'eugeniuszChrobak34@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('0955d32e-960f-4819-85fd-872c58392b00', 'f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'eugeniuszChrobak34', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 07:32:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('0955d32e-960f-4819-85fd-872c58392b00', 'Eugeniusz', 'Chrobak', 'eugeniuszChrobak34@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('0955d32e-960f-4819-85fd-872c58392b00', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'ad76e659-2bb5-40b2-a106-b079a0e2a64e', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('fba123d1-eda3-4780-b5b8-108488747cfc', 'marcinKaczkan21', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 09:34:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('fba123d1-eda3-4780-b5b8-108488747cfc', 'Marcin', 'Kaczkan', 'marcinKaczkan21@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('fba123d1-eda3-4780-b5b8-108488747cfc', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('2874bdb5-0a1f-45e1-99ac-e60da97d5026', 'fba123d1-eda3-4780-b5b8-108488747cfc', 'marcinKaczkan21', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 09:34:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('2874bdb5-0a1f-45e1-99ac-e60da97d5026', 'Marcin', 'Kaczkan', 'marcinKaczkan21@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('2874bdb5-0a1f-45e1-99ac-e60da97d5026', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'fba123d1-eda3-4780-b5b8-108488747cfc', '69f5d1e7-d2d7-4db5-8b4d-942f9ab7fb70', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('09a7a0d7-59f3-4450-b87e-597e670ab4da', 'piotrMorawski22', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 11:36:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('09a7a0d7-59f3-4450-b87e-597e670ab4da', 'Piotr', 'Morawski', 'piotrMorawski22@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('09a7a0d7-59f3-4450-b87e-597e670ab4da', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('4671131e-b5d1-4a07-8636-9ab1d26bdd46', '09a7a0d7-59f3-4450-b87e-597e670ab4da', 'piotrMorawski22', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 11:36:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('4671131e-b5d1-4a07-8636-9ab1d26bdd46', 'Piotr', 'Morawski', 'piotrMorawski22@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('4671131e-b5d1-4a07-8636-9ab1d26bdd46', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '09a7a0d7-59f3-4450-b87e-597e670ab4da', '4e515ed3-2cce-4d96-8100-90bb4e20f3b6', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('4a7945b6-b063-45f8-acf6-a1c0d5391673', 'jacekTeler23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 13:38:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('4a7945b6-b063-45f8-acf6-a1c0d5391673', 'Jacek', 'Teler', 'jacekTeler23@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('4a7945b6-b063-45f8-acf6-a1c0d5391673', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('ef492bcf-7b4b-4adf-9fbc-db9ea8f1e1f6', '4a7945b6-b063-45f8-acf6-a1c0d5391673', 'jacekTeler23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 13:38:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('ef492bcf-7b4b-4adf-9fbc-db9ea8f1e1f6', 'Jacek', 'Teler', 'jacekTeler23@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('ef492bcf-7b4b-4adf-9fbc-db9ea8f1e1f6', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '4a7945b6-b063-45f8-acf6-a1c0d5391673', 'c4467f80-d274-4fa5-a12e-35a3a0aa7964', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.event (id, version ,name, descriptionPL ,descriptionEN, isNotCanceled, startDate, endDate, created_at, action_type) VALUES ('dbe1b405-aed0-4d0a-bda7-50938e7b45f1', 0, 'Dni Języków Programowania', 'Idealne wydarzenie dla fanów programownia', 'The perfect event for fans of programming', true,  '2024-11-11 00:00:00', '2024-11-13 23:59:59', '2024-05-21', 'CREATE');
INSERT INTO public.event (id, version ,name, descriptionPL ,descriptionEN, isNotCanceled, startDate, endDate, created_at, action_type) VALUES ('fffc5e88-3054-4776-9189-4e64f1a33291', 0, 'Research Days', 'Dni w których zobaczysz jak wygląda praca naukowców', 'Days in which you will see what the work of scientists looks like', true, '2024-09-20 00:00:00', '2024-09-25 23:59:59', '2024-06-21', 'CREATE');
INSERT INTO public.event (id, version ,name, descriptionPL ,descriptionEN, isNotCanceled, startDate, endDate, created_at, action_type) VALUES ('9470ac2b-d589-4740-b7b6-dd665690d531', 0, 'Dni Otwarte Politechniki', 'Zazwyczaj też są otwarte, ale teraz będą bardziej', 'They are usually open too, but now they will be more', true, '2024-10-10 00:00:00', '2024-10-24 23:59:59', '2024-08-21', 'CREATE');
INSERT INTO public.event (id, version ,name, descriptionPL ,descriptionEN, isNotCanceled, startDate, endDate, created_at, action_type) VALUES ('5b9cd583-b097-40a6-887f-7d4f440f99c9', 0, 'Binożowy Tydzień', 'W końcu będziesz wiedział od czego to skrót', 'Eventually you will know what it is short for', true, '2024-02-11 00:00:00', '2024-02-13 23:59:59', '2024-01-21', 'CREATE');
INSERT INTO public.event (id, version ,name, descriptionPL ,descriptionEN, isNotCanceled, startDate, endDate, created_at, action_type) VALUES ('c89e341e-7927-40e7-a4d9-1695314b20e8', 0, 'Łódka Naukowców', 'Popłyń z nami w rejs wiedzy', 'Sail with us on a voyage of knowledge', true, '2024-10-14 00:00:00', '2024-11-19 23:59:59', '2024-07-13', 'CREATE');
INSERT INTO public.event (id, version ,name, descriptionPL ,descriptionEN, isNotCanceled, startDate, endDate, created_at, action_type) VALUES ('c89e341e-7927-40e7-a4d9-1695314b20e9', 0, 'Wire week', 'Każdy ziom zbiera złom, sprzedaj pralkę, sprzedaj łom, popal kable, przetop druty, żeby mieć na nowe buty!', 'Every homie collects scrap metal, sell the washing machine, sell the crowbar, burn the cables, melt the wires to have for new shoes!', true, '2024-06-05 00:00:00', '2024-06-06 23:59:59', '2024-05-13', 'CREATE');
INSERT INTO public.event (id, version ,name, descriptionPL ,descriptionEN, isNotCanceled, startDate, endDate, created_at, action_type) VALUES ('c89e341e-7927-40e7-a4d9-1695314b20e0', 0, 'Ruby on rails', 'Ruby to najlepszy język', 'Ruby is the best language', true, '2024-06-05 00:00:00', '2024-06-06 23:59:59', '2024-05-13', 'CREATE');

INSERT INTO public.location (id, version, name, street, buildingNumber, postalCode, city, country, isactive, created_at, action_type) VALUES ('da7cfcfa-5f1c-4a85-8f93-1022f28f747a', 0, 'Lodex', 'Al. Politechniki', 8, '93-590', 'Łódź', 'Poland', true, '2024-06-05', 'CREATE');

INSERT INTO public.room (id, version, name, location_id, maxCapacity, isactive, created_at, action_type) VALUES ('78f0f497-10b7-4478-9a28-c9dc86118e67', 0, '443', 'da7cfcfa-5f1c-4a85-8f93-1022f28f747a', 30, true, '2024-06-05', 'CREATE');
INSERT INTO public.room (id, version, name, location_id, maxCapacity, isactive, created_at, action_type) VALUES ('fded6a37-5272-4e9c-94f0-436f5f17581c', 0, '42', 'da7cfcfa-5f1c-4a85-8f93-1022f28f747a', 30, true, '2024-06-13', 'CREATE');

INSERT INTO public.speaker (id, version, firstName, lastName, created_at, action_type) VALUES ('713c84a3-03bd-4206-ac5c-ecf8d7d04ae6', 0, 'Krzysztof', 'Ślusarski', '2024-06-05 07:32:00', 'CREATE');
INSERT INTO public.speaker_history (id, version, firstName, lastName, created_at, action_type, speaker_id) VALUES ('b706ed45-4ecc-4a36-9cd5-671ae1ad7ec1', 0, 'Krzysztof', 'Ślusarski', '2024-06-05 07:32:00', 'CREATE', '713c84a3-03bd-4206-ac5c-ecf8d7d04ae6');

INSERT INTO public.speaker (id, firstname, lastname,created_at,action_type,version) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'Lionel', 'Mesi', '2024-04-09 07:32:00', 'CREATE',1);
INSERT INTO public.speaker_history (id, firstname, lastname,created_at,action_type,version, speaker_id) VALUES ('2320eb06-d54f-4d87-8faa-774f0c56339f', 'Lionel', 'Mesi', '2024-04-09 07:32:00', 'CREATE',1, 'f3c50886-bb5a-451c-99a3-6a79a6329cb5');

INSERT INTO public.speaker (id, firstname, lastname,created_at,action_type,version) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb6', 'Antoni', 'Nowak', '2024-04-09 07:32:00', 'CREATE',1);
INSERT INTO public.speaker_history (id, firstname, lastname,created_at,action_type,version, speaker_id) VALUES ('67dee9aa-d64e-4050-9d7f-4ed09083a6e7', 'Antoni', 'Nowak', '2024-04-09 07:32:00', 'CREATE',1, 'f3c50886-bb5a-451c-99a3-6a79a6329cb6');

INSERT INTO public.speaker (id, firstname, lastname,created_at,action_type,version) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb7', 'Piotr', 'Kowalski', '2024-04-09 07:32:00', 'CREATE',1);
INSERT INTO public.speaker_history (id, firstname, lastname,created_at,action_type,version, speaker_id) VALUES ('a1884645-e567-4936-9a61-b19fd8c933b6', 'Piotr', 'Kowalski', '2024-04-09 07:32:00', 'CREATE',1, 'f3c50886-bb5a-451c-99a3-6a79a6329cb7');


INSERT INTO public.session (id, version, room_id, speaker_id, name, event_id, isActive, description, startTime, endTime, maxSeats,availableSeats, created_at, action_type) VALUES ('4b2555e9-61f1-4c1d-9d7a-f425696eb2d2', 0, '78f0f497-10b7-4478-9a28-c9dc86118e67', '713c84a3-03bd-4206-ac5c-ecf8d7d04ae6', 'Dlaczego Java jest najlepsza', 'dbe1b405-aed0-4d0a-bda7-50938e7b45f1', true, 'Wykład o najlepszym języku programowania na świecie', '2024-11-13 12:00:00', '2024-11-13 15:00:00', 3,2, '2024-06-05', 'CREATE');
INSERT INTO public.session (id, version, room_id, speaker_id, name, event_id, isActive, description, startTime, endTime, maxSeats,availableSeats, created_at, action_type) VALUES ('00afff55-19da-4359-8cbc-5ed7f28e0fad', 0, 'fded6a37-5272-4e9c-94f0-436f5f17581c', 'f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'Dlaczego JakartaEE jest przestarzała', 'dbe1b405-aed0-4d0a-bda7-50938e7b45f1', true, 'Wykład o tym, jak nie tworzyć frameworka', '2024-11-13 15:15:00', '2024-11-13 17:00:00', 3,2, '2024-06-13', 'CREATE');
INSERT INTO public.session (id, version, room_id, speaker_id, name, event_id, isActive, description, startTime, endTime, maxSeats,availableSeats, created_at, action_type) VALUES ('2afbeb8b-9495-4af3-b037-5283e49efb22', 0, 'fded6a37-5272-4e9c-94f0-436f5f17581c', 'f3c50886-bb5a-451c-99a3-6a79a6329cb6', 'JTA i inne rzeczy niestworzone', 'dbe1b405-aed0-4d0a-bda7-50938e7b45f1', true, 'Wykład o abominacjach nadal niestety używanych', '2024-11-13 17:15:00', '2024-11-13 19:00:00', 3,2, '2024-06-13', 'CREATE');
INSERT INTO public.session (id, version, room_id, speaker_id, name, event_id, isActive, description, startTime, endTime, maxSeats,availableSeats, created_at, action_type) VALUES ('0e83d2a7-4fda-417f-8476-584288dc4af5', 0, 'fded6a37-5272-4e9c-94f0-436f5f17581c', 'f3c50886-bb5a-451c-99a3-6a79a6329cb7', 'Wyjątki weryfikowalne i jak się przed nimi bronić', 'dbe1b405-aed0-4d0a-bda7-50938e7b45f1', true, 'Wykład o wyższości wyjątków nieweryfikowalnych', '2024-11-12 12:00:00', '2024-11-12 16:00:00', 3,1, '2024-06-13', 'CREATE');


INSERT INTO public.session (id, version, room_id, speaker_id, name, event_id, isActive, description, startTime, endTime, maxSeats,availableSeats, created_at, action_type) VALUES ('4b2555e9-61f1-4c1d-9d7a-f425696eb2d3', 0, '78f0f497-10b7-4478-9a28-c9dc86118e67', 'f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'Tajniki drutowania', 'c89e341e-7927-40e7-a4d9-1695314b20e9', true, 'Światowej sławy druciarz, Mesi, zaprezentuje tajne metody drutowania', '2024-06-05 12:00:00', '2024-06-05 15:00:00', 3,2, '2024-05-15', 'CREATE');

INSERT INTO public.ticket (id, version, account_id, session_id, isNotCancelled, reservationTime, created_at, action_type) VALUES ('0b7edef0-55cb-4cc9-a7f2-95a662125511', 0, '4a7945b6-b063-45f8-acf6-a1c0d5391673', '4b2555e9-61f1-4c1d-9d7a-f425696eb2d2', true, '2024-06-05 12:00:00', '2024-06-05 12:00:00', 'CREATE');
INSERT INTO public.ticket (id, version, account_id, session_id, isNotCancelled, reservationTime, created_at, action_type) VALUES ('0b7edef0-55cb-4cc9-a7f2-95a662125512', 0, 'fba123d1-eda3-4780-b5b8-108488747cfc', '4b2555e9-61f1-4c1d-9d7a-f425696eb2d3', true, '2024-06-05 12:00:00', '2024-06-05 12:00:00', 'CREATE');
INSERT INTO public.ticket (id, version, account_id, session_id, isNotCancelled, reservationTime, created_at, action_type) VALUES ('73e545bc-3b2e-491f-bee4-7eb37b625ea9', 0, '4a7945b6-b063-45f8-acf6-a1c0d5391673', '0e83d2a7-4fda-417f-8476-584288dc4af5', true, '2024-06-05 12:00:00', '2024-06-05 12:00:00', 'CREATE');
INSERT INTO public.ticket (id, version, account_id, session_id, isNotCancelled, reservationTime, created_at, action_type) VALUES ('22985088-0976-461c-92c9-14df474d18f1', 0, 'd0d93942-9ea6-48fb-9762-27105b8b9f40', '4b2555e9-61f1-4c1d-9d7a-f425696eb2d3', true, '2024-06-05 12:00:00', '2024-06-05 12:00:00', 'CREATE');

