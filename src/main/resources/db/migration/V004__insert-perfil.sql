INSERT INTO public.perfil
(id, nome)
VALUES(nextval('perfil_id_seq'::regclass), 'CLIENTE');

INSERT INTO public.perfil
(id, nome)
VALUES(nextval('perfil_id_seq'::regclass), 'BARBEIRO');