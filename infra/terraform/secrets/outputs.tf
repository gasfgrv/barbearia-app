output "db_username" {
  value = jsondecode(aws_secretsmanager_secret_version.db_secrets_version.secret_string)["username"]
  description = "Usuário do banco de dados"
}

output "db_password" {
  value     = jsondecode(aws_secretsmanager_secret_version.db_secrets_version.secret_string)["password"]
  description = "Senha do banco dados"
  sensitive = true
}

output "secrets_arn" {
  description = "ARNs do secrets manager"
  value = toset([
    aws_secretsmanager_secret.db_secret.arn,
    aws_secretsmanager_secret.jwt_secret.arn,
    aws_secretsmanager_secret.email_secret.arn
  ])
}
