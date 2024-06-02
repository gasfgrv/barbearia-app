CREATE TABLE public.usuario (
	login varchar NOT NULL,
	senha varchar NOT NULL,
	perfil_id int4 NOT NULL,
	CONSTRAINT usuario_pkey PRIMARY KEY (login)
);
