GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.account TO ssbd01mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.role TO ssbd01mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.account_role TO ssbd01mok;

INSERT INTO public.role (id,version, name) VALUES ('550e8400-e29b-41d4-a716-446655440000',0, 'ADMIN');
INSERT INTO public.role (id,version, name) VALUES ('4c90f86a-0d82-4c51-b72c-80e20949a3b9',0, 'MANAGER');
INSERT INTO public.role (id,version, name) VALUES ('3b149d62-67cb-4b81-b4c6-31a1e0714ef3',0, 'GUEST');
INSERT INTO public.role (id,version, name) VALUES ('cd8ab1c1-2431-4e28-88b5-fdd54de3d92a',0, 'CLIENT');