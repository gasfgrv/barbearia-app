ALTER TABLE public.usuario ADD CONSTRAINT usuario_perfil_fk FOREIGN KEY (perfil_id) REFERENCES public.perfil(id);
