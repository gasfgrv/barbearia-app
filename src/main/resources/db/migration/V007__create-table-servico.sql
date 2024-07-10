 CREATE TABLE public.servico (
   id UUID NOT NULL,
   nome VARCHAR(255) NOT NULL,
   descricao VARCHAR(255) NOT NULL,
   preco NUMERIC(5, 2) NOT NULL,
   duracao INTEGER NOT NULL,
   ativo BOOLEAN NOT NULL,
   CONSTRAINT servico_pkey PRIMARY KEY (id)
 )
