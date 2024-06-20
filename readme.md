### Script para subir tudo

```shell
cd infra/terraform/
terraform apply -auto-approve -var-file=project.tfvars -target=module.s3
terraform apply -auto-approve -var-file=project.tfvars -target=module.secrets

cd ../../
docker compose up -d
```

### Script para destruir tudo

```shell
cd infra/terraform/
terraform destroy -auto-approve -var-file=project.tfvars -target=module.s3
terraform destroy -auto-approve -var-file=project.tfvars -target=module.secrets

cd ../../
docker compose down
```