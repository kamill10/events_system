create user ssbd01mok with password 'mok';
create user ssbd01auth with password 'auth';
create user ssbd01mow with password 'mow';

GRANT SELECT, INSERT, UPDATE ON TABLE public.account TO ssbd01mok;
GRANT SELECT ON TABLE public.role TO ssbd01mok;
GRANT SELECT, INSERT, DELETE ON TABLE public.account_role TO ssbd01mok;

GRANT SELECT ON TABLE public.account TO ssbd01auth;
GRANT SELECT ON TABLE public.role TO ssbd01auth;
GRANT SELECT ON TABLE public.account_role TO ssbd01auth;

GRANT SELECT ON TABLE public.account TO ssbd01mow;

GRANT SELECT, INSERT, UPDATE ON TABLE public.event TO ssbd01mow;
GRANT SELECT, INSERT ON TABLE public.event_session TO ssbd01mow;
GRANT SELECT, INSERT, UPDATE ON TABLE public.session TO ssbd01mow;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.location TO ssbd01mow;
GRANT SELECT, INSERT, DELETE ON TABLE public.location_room TO ssbd01mow;
GRANT SELECT, INSERT, DELETE ON TABLE public.room TO ssbd01mow;

GRANT SELECT, INSERT ON TABLE public.speaker TO ssbd01mow;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.ticket TO ssbd01mow;


INSERT INTO public.role (id, version, name) VALUES ('550e8400-e29b-41d4-a716-446655440000', 0, 'ADMIN');
INSERT INTO public.role (id, version, name) VALUES ('4c90f86a-0d82-4c51-b72c-80e20949a3b9', 0, 'MANAGER');
INSERT INTO public.role (id, version, name) VALUES ('cd8ab1c1-2431-4e28-88b5-fdd54de3d92a', 0, 'PARTICIPANT');

CREATE INDEX session_speaker_idx ON "session" (speaker_id);
CREATE INDEX room_location_idx ON "room" (location_id);
CREATE INDEX session_event_idx ON "session" (event_id);