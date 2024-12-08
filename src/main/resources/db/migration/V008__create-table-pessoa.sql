 CREATE TABLE public.pessoa (
   id UUID NOT NULL,
   nome VARCHAR(255) NOT NULL,
   cpf VARCHAR(11) NOT NULL,
   data_nascimento DATE NOT NULL,
   usuario_login VARCHAR(255) NOT NULL,
   celular VARCHAR(11) NOT NULL,
   ativo BOOLEAN NOT NULL,
   CONSTRAINT pessoa_pkey PRIMARY KEY (id)
 )
