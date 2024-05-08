INSERT INTO public.role (id, version, name) VALUES ('550e8400-e29b-41d4-a716-446655440000', 0, 'ADMIN');
INSERT INTO public.role (id, version, name) VALUES ('4c90f86a-0d82-4c51-b72c-80e20949a3b9', 0, 'MANAGER');
INSERT INTO public.role (id, version, name) VALUES ('cd8ab1c1-2431-4e28-88b5-fdd54de3d92a', 0, 'PARTICIPANT');

INSERT INTO public.account (id,username,password,active,verified, nonlocked,failedloginattempts,email,gender,firstname,lastname,version,created_at,action_type,language) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1','testAdmin','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,'admin202401@proton.me',1,'Pudzian','Admin',0,'2021-01-01 00:00:00','CREATE','POLISH');
INSERT INTO public.account_role (account_id,roles_id) VALUES ('8b25c94f-f10f-4285-8eb2-39ee1c4002f1','550e8400-e29b-41d4-a716-446655440000');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'8b25c94f-f10f-4285-8eb2-39ee1c4002f1','2f3e920d-8c7a-4a15-a4d7-1ae28e5930b6','9a36797f-08e6-4e8b-b0c3-88b8d279a8c1','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id,username,password,active,verified,nonlocked,failedloginattempts,email,gender,firstname,lastname,version,created_at,action_type,language) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','testParticipant','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,'participant202401@proton.me',1,'Test','Participant',0,'2021-01-01 00:00:00','CREATE','ENGLISH');
INSERT INTO public.account_role (account_id,roles_id) VALUES ('a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','cd8ab1c1-2431-4e28-88b5-fdd54de3d92a');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'a8816c75-e735-4d16-9f3e-7fcf3d0e7fe6','67f7f35b-03d9-45df-b2d6-81cb6d63113f','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');

INSERT INTO public.account (id,username,password,active,verified,nonlocked,failedloginattempts,email,gender,firstname,lastname,version,created_at,action_type,language) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157','testManager','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y',true,true,true,0,'manager202401@proton.me',1,'Test','Manager',0,'2021-01-01 00:00:00','CREATE','ENGLISH');
INSERT INTO public.account_role (account_id,roles_id) VALUES ('5454d58c-6ae2-4eee-8980-a49a1664f157','4c90f86a-0d82-4c51-b72c-80e20949a3b9');
INSERT INTO public.password_history(version,account_id,id,password) VALUES (0,'5454d58c-6ae2-4eee-8980-a49a1664f157','b57d5a3f-aae0-4a3d-9315-1e5e16b1f28d','$2a$10$cZM2GhvetO6fZur/9s26P.alLI.bZmSWfxsrrsLWw4qHlD6F3903y');
