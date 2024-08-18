### Script para subir tudo

```shell
cd infra/terraform/
terraform apply -auto-approve -target=module.s3
terraform apply -auto-approve -target=module.secrets

cd ../../
docker compose up -d
```

### Script para destruir tudo

```shell
cd infra/terraform/
terraform destroy -auto-approve -target=module.s3
terraform destroy -auto-approve -target=module.secrets

cd ../../
docker compose down
```

### Links Uteis

- Sonar: http://localhost:9000
- Prometheus: http://localhost:9090/
- Swagger UI: http://localhost:8080/swagger/ui.html
- OpenApi: http://localhost:8080/api-docs
- Actuaror: http://localhost:8080/actuator
- Grafana: http://localhost:3000
