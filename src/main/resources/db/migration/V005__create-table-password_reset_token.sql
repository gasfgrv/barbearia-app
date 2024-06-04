CREATE TABLE public.password_reset_token (
	id serial4 NOT NULL,
	"token" varchar NOT NULL,
	usuario_login varchar NOT NULL,
	expiry_date timestamp NOT NULL,
	CONSTRAINT password_reset_token_pkey PRIMARY KEY (id)
);
