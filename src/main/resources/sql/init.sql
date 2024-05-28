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
GRANT SELECT,INSERT ON TABLE account_time_zone TO ssbd01mok;
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

GRANT SELECT, INSERT, UPDATE ON TABLE public.event TO ssbd01mow;
GRANT SELECT, INSERT, UPDATE ON TABLE public.session TO ssbd01mow;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.location TO ssbd01mow;
GRANT SELECT, INSERT, DELETE ON TABLE public.room TO ssbd01mow;

GRANT SELECT, INSERT, UPDATE ON TABLE public.speaker TO ssbd01mow;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.ticket TO ssbd01mow;


CREATE INDEX account_role_account_idx ON account_role USING btree (account_id);
CREATE INDEX account_role_role_idx ON account_role USING btree (roles_id);
CREATE INDEX account_history_role_account_idx ON account_history_role USING btree (accounthistory_id);
CREATE INDEX account_history_role_role_idx ON account_history_role USING btree (roles_id);
CREATE INDEX account_history_account_id_idx ON account_history USING btree (account_id);
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

INSERT INTO public.account_theme (id, version, theme) VALUES ('666e8400-e29b-41d4-a716-446655440000', 0, 'Light');
INSERT INTO public.account_theme (id, version, theme) VALUES ('777e8400-e29b-41d4-a716-446655440000', 0, 'Dark');

INSERT INTO public.account_time_zone (id, version, timeZone) VALUES ('888e8400-e29b-41d4-a716-446655440000', 0, 'Europe/Warsaw');
INSERT INTO public.account_time_zone (id, version, timeZone) VALUES ('999e8400-e29b-41d4-a716-446655440000', 0, 'Europe/London');

INSERT INTO public.account (id,username,password,active,verified, nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1','testAdmin','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1', 'Pudzian', 'Admin', 'admin202401@proton.me', 1);
INSERT INTO public.account_role (account_id,roles_id) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1','550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.account_history (id,account_id,username,password,active,verified, nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1','8b25c94f-f10f-4285-8eb2-39ee1c4002f1','testAdmin','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1', 'Pudzian', 'Admin', 'admin202401@proton.me', 1);
INSERT INTO public.account_history_role (accounthistory_id,roles_id) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1','550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'8b25c94f-f10f-4285-8eb2-39ee1c4002f1','2f3e920d-8c7a-4a15-a4d7-1ae28e5930b6','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id,username,password,active,verified,nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','testParticipant','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6', 'Test', 'Participant', 'participant202401@proton.me', 1);
INSERT INTO public.account_role (account_id,roles_id) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id,account_id,username,password,active,verified,nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','testParticipant','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6', 'Test', 'Participant', 'participant202401@proton.me', 1);
INSERT INTO public.account_history_role (accounthistory_id,roles_id) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','67f7f35b-03d9-45df-b2d6-81cb6d63113f','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id,username,password,active,verified,nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157','testManager','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157', 'Test', 'Manager', 'manager202401@proton.me', 1);
INSERT INTO public.account_role (account_id,roles_id) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157','4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.account_history (id,account_id,username,password,active,verified,nonlocked,failedloginattempts,version,created_at,action_type, language) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157','5454d58c-6ae2-4eee-8980-a49a1664f157','testManager','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,0,'2021-01-01 00:00:00','CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157', 'Test', 'Manager', 'manager202401@proton.me', 1);
INSERT INTO public.account_history_role (accounthistory_id,roles_id) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157','4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'5454d58c-6ae2-4eee-8980-a49a1664f157','b57d5a3f-aae0-4a3d-9315-1e5e16b1f28d','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('44073ec1-5db0-48bd-ae42-15298b22dcea', 'MENwitoldgombrowicz12', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-01-15 10:23:45', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('44073ec1-5db0-48bd-ae42-15298b22dcea', 'Witold', 'Gombrowicz', 'witoldgombrowicz12@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('44073ec1-5db0-48bd-ae42-15298b22dcea', '4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('44073ec1-5db0-48bd-ae42-15298b22dcea', '44073ec1-5db0-48bd-ae42-15298b22dcea', 'witoldgombrowicz12', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-01-15 10:23:45', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('44073ec1-5db0-48bd-ae42-15298b22dcea', 'Witold', 'Gombrowicz', 'witoldgombrowicz12@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('44073ec1-5db0-48bd-ae42-15298b22dcea', '4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '44073ec1-5db0-48bd-ae42-15298b22dcea', '44073ec1-5db0-48bd-ae42-15298b22dcea', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('1aeac6a9-17d3-4962-9508-a8cf960101b2', 'MENagnieszkaosiecka23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-20 14:56:32', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('1aeac6a9-17d3-4962-9508-a8cf960101b2', 'Agnieszka', 'Osiecka', 'agnieszkaosiecka23@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('1aeac6a9-17d3-4962-9508-a8cf960101b2', '4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('1aeac6a9-17d3-4962-9508-a8cf960101b2', '1aeac6a9-17d3-4962-9508-a8cf960101b2', 'agnieszkaosiecka23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-20 14:56:32', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('1aeac6a9-17d3-4962-9508-a8cf960101b2', 'Agnieszka', 'Osiecka', 'agnieszkaosiecka23@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('1aeac6a9-17d3-4962-9508-a8cf960101b2', '4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '1aeac6a9-17d3-4962-9508-a8cf960101b2', '1aeac6a9-17d3-4962-9508-a8cf960101b2', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('781127ce-3665-4750-846e-7803b006c7dd', 'ADMstanislawmrozek44', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-03-05 08:12:11', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('781127ce-3665-4750-846e-7803b006c7dd', 'Stanisław', 'Mrożek', 'stanislawmrozek44@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('781127ce-3665-4750-846e-7803b006c7dd', '550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('781127ce-3665-4750-846e-7803b006c7dd', '781127ce-3665-4750-846e-7803b006c7dd', 'stanislawmrozek44', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-03-05 08:12:11', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('781127ce-3665-4750-846e-7803b006c7dd', 'Stanisław', 'Mrożek', 'stanislawmrozek44@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('781127ce-3665-4750-846e-7803b006c7dd', '550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '781127ce-3665-4750-846e-7803b006c7dd', '781127ce-3665-4750-846e-7803b006c7dd', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('3aeb5f95-0d43-424e-9614-def7c2307445', 'ADMjeremiprzybora23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-17 16:45:23', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('3aeb5f95-0d43-424e-9614-def7c2307445', 'Jeremi', 'Przybora', 'jeremiprzybora23@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('3aeb5f95-0d43-424e-9614-def7c2307445', '550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('3aeb5f95-0d43-424e-9614-def7c2307445', '3aeb5f95-0d43-424e-9614-def7c2307445', 'jeremiprzybora23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-17 16:45:23', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('3aeb5f95-0d43-424e-9614-def7c2307445', 'Jeremi', 'Przybora', 'jeremiprzybora23@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('3aeb5f95-0d43-424e-9614-def7c2307445', '550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '3aeb5f95-0d43-424e-9614-def7c2307445', '3aeb5f95-0d43-424e-9614-def7c2307445', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('57358248-2a68-49c0-bb5a-29b23755903b', 'stanislawlem51', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-05-23 12:34:56', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('57358248-2a68-49c0-bb5a-29b23755903b', 'Stanisław', 'Lem', 'stanislawlem51@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('57358248-2a68-49c0-bb5a-29b23755903b', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('57358248-2a68-49c0-bb5a-29b23755903b', '57358248-2a68-49c0-bb5a-29b23755903b', 'stanislawlem51', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-05-23 12:34:56', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('57358248-2a68-49c0-bb5a-29b23755903b', 'Stanisław', 'Lem', 'stanislawlem51@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('57358248-2a68-49c0-bb5a-29b23755903b', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '57358248-2a68-49c0-bb5a-29b23755903b', '57358248-2a68-49c0-bb5a-29b23755903b', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('537d27df-c557-445b-bfb3-2e2adf9d95ef', 'konstantygalczynski124', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-10 09:28:44', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('537d27df-c557-445b-bfb3-2e2adf9d95ef', 'Konstanty', 'Gałczyński', 'konstantygalczynski124@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('537d27df-c557-445b-bfb3-2e2adf9d95ef', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('537d27df-c557-445b-bfb3-2e2adf9d95ef', '537d27df-c557-445b-bfb3-2e2adf9d95ef', 'konstantygalczynski124', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-10 09:28:44', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('537d27df-c557-445b-bfb3-2e2adf9d95ef', 'Konstanty', 'Gałczyński', 'konstantygalczynski124@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('537d27df-c557-445b-bfb3-2e2adf9d95ef', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '537d27df-c557-445b-bfb3-2e2adf9d95ef', '537d27df-c557-445b-bfb3-2e2adf9d95ef', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('08839a96-9432-4ada-a9bd-aa6a59d460ec', 'halinaposwiatoswka12', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-20 18:55:12', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('08839a96-9432-4ada-a9bd-aa6a59d460ec', 'Halina', 'Poświatowska', 'halinaposwiatoswka12@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('08839a96-9432-4ada-a9bd-aa6a59d460ec', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('08839a96-9432-4ada-a9bd-aa6a59d460ec', '08839a96-9432-4ada-a9bd-aa6a59d460ec', 'halinaposwiatoswka12', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-02-20 18:55:12', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('08839a96-9432-4ada-a9bd-aa6a59d460ec', 'Halina', 'Poświatowska', 'halinaposwiatoswka12@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('08839a96-9432-4ada-a9bd-aa6a59d460ec', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '08839a96-9432-4ada-a9bd-aa6a59d460ec', '08839a96-9432-4ada-a9bd-aa6a59d460ec', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('0ebf010f-1655-4090-bb31-2802574c09e9', 'aleksanderchwat31', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-01-25 21:43:54', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('0ebf010f-1655-4090-bb31-2802574c09e9', 'Aleksander', 'Chwat', 'aleksanderchwat31@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('0ebf010f-1655-4090-bb31-2802574c09e9', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('0ebf010f-1655-4090-bb31-2802574c09e9', '0ebf010f-1655-4090-bb31-2802574c09e9', 'aleksanderchwat31', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-01-25 21:43:54', 'CREATE', 'ENGLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('0ebf010f-1655-4090-bb31-2802574c09e9', 'Aleksander', 'Chwat', 'aleksanderchwat31@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('0ebf010f-1655-4090-bb31-2802574c09e9', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '0ebf010f-1655-4090-bb31-2802574c09e9', '0ebf010f-1655-4090-bb31-2802574c09e9', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('0d3d8632-0ed6-4e43-8021-633c5c7e66c5', 'wandaRutkiewicz21', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-01 09:10:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('0d3d8632-0ed6-4e43-8021-633c5c7e66c5', 'Wanda', 'Rutkiewicz', 'wandaRutkiewicz21@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('0d3d8632-0ed6-4e43-8021-633c5c7e66c5', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('0d3d8632-0ed6-4e43-8021-633c5c7e66c5', '0d3d8632-0ed6-4e43-8021-633c5c7e66c5', 'wandaRutkiewicz21', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-01 09:10:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('0d3d8632-0ed6-4e43-8021-633c5c7e66c5', 'Wanda', 'Rutkiewicz', 'wandaRutkiewicz21@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('0d3d8632-0ed6-4e43-8021-633c5c7e66c5', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '0d3d8632-0ed6-4e43-8021-633c5c7e66c5', '0d3d8632-0ed6-4e43-8021-633c5c7e66c5', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('a8a48455-5df0-442c-a63b-be4f63fef56a', 'jerzyKukuczka22', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-02 11:12:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('a8a48455-5df0-442c-a63b-be4f63fef56a', 'Jerzy', 'Kukuczka', 'jerzyKukuczka22@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('a8a48455-5df0-442c-a63b-be4f63fef56a', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('a8a48455-5df0-442c-a63b-be4f63fef56a', 'a8a48455-5df0-442c-a63b-be4f63fef56a', 'jerzyKukuczka22', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-02 11:12:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('a8a48455-5df0-442c-a63b-be4f63fef56a', 'Jerzy', 'Kukuczka', 'jerzyKukuczka22@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('a8a48455-5df0-442c-a63b-be4f63fef56a', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'a8a48455-5df0-442c-a63b-be4f63fef56a', 'a8a48455-5df0-442c-a63b-be4f63fef56a', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('e74664b8-5ba6-49c4-a294-078ef52cb999', 'krzysztofWielicki23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-03 13:14:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('e74664b8-5ba6-49c4-a294-078ef52cb999', 'Krzysztof', 'Wielicki', 'krzysztofWielicki23@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('e74664b8-5ba6-49c4-a294-078ef52cb999', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('e74664b8-5ba6-49c4-a294-078ef52cb999', 'e74664b8-5ba6-49c4-a294-078ef52cb999', 'krzysztofWielicki23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-03 13:14:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('e74664b8-5ba6-49c4-a294-078ef52cb999', 'Krzysztof', 'Wielicki', 'krzysztofWielicki23@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('e74664b8-5ba6-49c4-a294-078ef52cb999', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'e74664b8-5ba6-49c4-a294-078ef52cb999', 'e74664b8-5ba6-49c4-a294-078ef52cb999', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('d8e6f8f3-3776-4902-9b25-76c3fcff7541', 'wojciechKurtyka24', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-04 15:16:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('d8e6f8f3-3776-4902-9b25-76c3fcff7541', 'Wojciech', 'Kurtyka', 'wojciechKurtyka24@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('d8e6f8f3-3776-4902-9b25-76c3fcff7541', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('d8e6f8f3-3776-4902-9b25-76c3fcff7541', 'd8e6f8f3-3776-4902-9b25-76c3fcff7541', 'wojciechKurtyka24', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-04 15:16:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('d8e6f8f3-3776-4902-9b25-76c3fcff7541', 'Wojciech', 'Kurtyka', 'wojciechKurtyka24@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('d8e6f8f3-3776-4902-9b25-76c3fcff7541', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'd8e6f8f3-3776-4902-9b25-76c3fcff7541', 'd8e6f8f3-3776-4902-9b25-76c3fcff7541', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('fc684830-af98-4723-93b1-8263a1680bba', 'piotrPustelnik25', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-05 17:18:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('fc684830-af98-4723-93b1-8263a1680bba', 'Piotr', 'Pustelnik', 'piotrPustelnik25@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('fc684830-af98-4723-93b1-8263a1680bba', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('fc684830-af98-4723-93b1-8263a1680bba', 'fc684830-af98-4723-93b1-8263a1680bba', 'piotrPustelnik25', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-05 17:18:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('fc684830-af98-4723-93b1-8263a1680bba', 'Piotr', 'Pustelnik', 'piotrPustelnik25@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('fc684830-af98-4723-93b1-8263a1680bba', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'fc684830-af98-4723-93b1-8263a1680bba', 'fc684830-af98-4723-93b1-8263a1680bba', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('8e569ce7-e388-471a-a4a1-7fb557a3684a', 'adamBielecki27', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-06 19:20:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('8e569ce7-e388-471a-a4a1-7fb557a3684a', 'Adam', 'Bielecki', 'adamBielecki27@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('8e569ce7-e388-471a-a4a1-7fb557a3684a', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('8e569ce7-e388-471a-a4a1-7fb557a3684a', '8e569ce7-e388-471a-a4a1-7fb557a3684a', 'adamBielecki27', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-06 19:20:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('8e569ce7-e388-471a-a4a1-7fb557a3684a', 'Adam', 'Bielecki', 'adamBielecki27@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('8e569ce7-e388-471a-a4a1-7fb557a3684a', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '8e569ce7-e388-471a-a4a1-7fb557a3684a', '8e569ce7-e388-471a-a4a1-7fb557a3684a', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('289da33b-54e7-4f54-8f5d-8618ddab970f', 'maciejBerbeka27', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-07 21:22:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('289da33b-54e7-4f54-8f5d-8618ddab970f', 'Maciej', 'Berbeka', 'maciejBerbeka27@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('289da33b-54e7-4f54-8f5d-8618ddab970f', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('289da33b-54e7-4f54-8f5d-8618ddab970f', '289da33b-54e7-4f54-8f5d-8618ddab970f', 'maciejBerbeka27', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-07 21:22:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('289da33b-54e7-4f54-8f5d-8618ddab970f', 'Maciej', 'Berbeka', 'maciejBerbeka27@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('289da33b-54e7-4f54-8f5d-8618ddab970f', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '289da33b-54e7-4f54-8f5d-8618ddab970f', '289da33b-54e7-4f54-8f5d-8618ddab970f', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('edaf09f4-6293-4216-9cc1-1d792107c333', 'andrzejZawada29', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-08 23:24:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('edaf09f4-6293-4216-9cc1-1d792107c333', 'Andrzej', 'Zawada', 'andrzejZawada29@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('edaf09f4-6293-4216-9cc1-1d792107c333', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('edaf09f4-6293-4216-9cc1-1d792107c333', 'edaf09f4-6293-4216-9cc1-1d792107c333', 'andrzejZawada29', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-08 23:24:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('edaf09f4-6293-4216-9cc1-1d792107c333', 'Andrzej', 'Zawada', 'andrzejZawada29@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('edaf09f4-6293-4216-9cc1-1d792107c333', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'edaf09f4-6293-4216-9cc1-1d792107c333', 'edaf09f4-6293-4216-9cc1-1d792107c333', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('19f3126c-34a4-4ef4-a0ae-00f56343977e', 'januszMajer31', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 01:26:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('19f3126c-34a4-4ef4-a0ae-00f56343977e', 'Janusz', 'Majer', 'januszMajer31@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('19f3126c-34a4-4ef4-a0ae-00f56343977e', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('19f3126c-34a4-4ef4-a0ae-00f56343977e', '19f3126c-34a4-4ef4-a0ae-00f56343977e', 'januszMajer31', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 01:26:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('19f3126c-34a4-4ef4-a0ae-00f56343977e', 'Janusz', 'Majer', 'januszMajer31@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('19f3126c-34a4-4ef4-a0ae-00f56343977e', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '19f3126c-34a4-4ef4-a0ae-00f56343977e', '19f3126c-34a4-4ef4-a0ae-00f56343977e', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'arturHajzer32', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 03:28:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'Artur', 'Hajzer', 'arturHajzer32@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'arturHajzer32', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 03:28:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'Artur', 'Hajzer', 'arturHajzer32@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', 'be691bb9-8f91-46e6-b4d3-bcc8949c4d7d', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('d0d93942-9ea6-48fb-9762-27105b8b9f40', 'tadeuszPiotrowski33', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 05:30:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('d0d93942-9ea6-48fb-9762-27105b8b9f40', 'Tadeusz', 'Piotrowski', 'tadeuszPiotrowski33@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('d0d93942-9ea6-48fb-9762-27105b8b9f40', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('d0d93942-9ea6-48fb-9762-27105b8b9f40', 'd0d93942-9ea6-48fb-9762-27105b8b9f40', 'tadeuszPiotrowski33', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 05:30:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('d0d93942-9ea6-48fb-9762-27105b8b9f40', 'Tadeusz', 'Piotrowski', 'tadeuszPiotrowski33@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('d0d93942-9ea6-48fb-9762-27105b8b9f40', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'd0d93942-9ea6-48fb-9762-27105b8b9f40', 'd0d93942-9ea6-48fb-9762-27105b8b9f40', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'eugeniuszChrobak34', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 07:32:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'Eugeniusz', 'Chrobak', 'eugeniuszChrobak34@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'eugeniuszChrobak34', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 07:32:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'Eugeniusz', 'Chrobak', 'eugeniuszChrobak34@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'f3c50886-bb5a-451c-99a3-6a79a6329cb5', 'f3c50886-bb5a-451c-99a3-6a79a6329cb5', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('fba123d1-eda3-4780-b5b8-108488747cfc', 'marcinKaczkan21', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 09:34:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('fba123d1-eda3-4780-b5b8-108488747cfc', 'Marcin', 'Kaczkan', 'marcinKaczkan21@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('fba123d1-eda3-4780-b5b8-108488747cfc', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('fba123d1-eda3-4780-b5b8-108488747cfc', 'fba123d1-eda3-4780-b5b8-108488747cfc', 'marcinKaczkan21', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 09:34:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('fba123d1-eda3-4780-b5b8-108488747cfc', 'Marcin', 'Kaczkan', 'marcinKaczkan21@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('fba123d1-eda3-4780-b5b8-108488747cfc', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, 'fba123d1-eda3-4780-b5b8-108488747cfc', 'fba123d1-eda3-4780-b5b8-108488747cfc', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('09a7a0d7-59f3-4450-b87e-597e670ab4da', 'piotrMorawski22', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 11:36:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('09a7a0d7-59f3-4450-b87e-597e670ab4da', 'Piotr', 'Morawski', 'piotrMorawski22@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('09a7a0d7-59f3-4450-b87e-597e670ab4da', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('09a7a0d7-59f3-4450-b87e-597e670ab4da', '09a7a0d7-59f3-4450-b87e-597e670ab4da', 'piotrMorawski22', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 11:36:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('09a7a0d7-59f3-4450-b87e-597e670ab4da', 'Piotr', 'Morawski', 'piotrMorawski22@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('09a7a0d7-59f3-4450-b87e-597e670ab4da', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '09a7a0d7-59f3-4450-b87e-597e670ab4da', '09a7a0d7-59f3-4450-b87e-597e670ab4da', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('4a7945b6-b063-45f8-acf6-a1c0d5391673', 'jacekTeler23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 13:38:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data (account_id, firstname, lastname, email, gender) VALUES ('4a7945b6-b063-45f8-acf6-a1c0d5391673', 'Jacek', 'Teler', 'jacekTeler23@int.pl', 1);
INSERT INTO public.account_role (account_id, roles_id) VALUES ('4a7945b6-b063-45f8-acf6-a1c0d5391673', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.account_history (id, account_id, username, password, active, verified, nonlocked, failedloginattempts, version, created_at, action_type, language) VALUES ('4a7945b6-b063-45f8-acf6-a1c0d5391673', '4a7945b6-b063-45f8-acf6-a1c0d5391673', 'jacekTeler23', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y', true, true, true, 0, 0, '2024-04-09 13:38:00', 'CREATE', 'POLISH');
INSERT INTO public.personal_data_history (account_history_id, firstname, lastname, email, gender) VALUES ('4a7945b6-b063-45f8-acf6-a1c0d5391673', 'Jacek', 'Teler', 'jacekTeler23@int.pl', 1);
INSERT INTO public.account_history_role (accounthistory_id, roles_id) VALUES ('4a7945b6-b063-45f8-acf6-a1c0d5391673', 'cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history (version, account_id, id, password) VALUES (0, '4a7945b6-b063-45f8-acf6-a1c0d5391673', '4a7945b6-b063-45f8-acf6-a1c0d5391673', '$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');
