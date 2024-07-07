package com.gasfgrv.barbearia.adapter.database.reset;

import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchemaMock;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class PasswordResetTokenSchemaMock {

    public PasswordResetTokenSchema montarPasswordResetTokenSchema() {
        PasswordResetTokenSchema schema = new PasswordResetTokenSchema();
        schema.setId(1);
        schema.setUsuarioLogin(UsuarioSchemaMock.montarUsuarioSchema());
        schema.setExpiryDate(LocalDateTime.of(2024, 1, 1, 10, 10));
        schema.setToken("ewogICJhbGciOiJIUzI1NiIsCiAgInR5cCI6IkpXVCIKfQ." +
                "ewogICJpc3MiOiJMb2dpbiIsCiAgInN1YiI6InRlc3RlQHRlc3RlLmNvbSIsCiAgImV4cCI6MTcxNzU0ODMyNgp9." +
                "ZXdvZ0lDSmhiR2NpT2lKSVV6STFOaUlzQ2lBZ0luUjVjQ0k2SWtwWFZDSUtmUS4uZ1d0NkJTdks3Uk45bWQ3b2NYb0VZMHJhNnZ4eTZsQzU4VEw1ZzNzQjIxVQ");
        return schema;
    }

}