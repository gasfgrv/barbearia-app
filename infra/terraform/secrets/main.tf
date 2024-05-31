locals {
  db_secret_name    = "barbeariaRdsCredentials"
  email_secret_name = "barbeariaEmailServerCredentials"
  jwt_secret_name   = "barbeariaJwtSecret"
}

resource "aws_secretsmanager_secret" "db_secret" {
  name                    = local.db_secret_name
  recovery_window_in_days = 0
  kms_key_id              = var.secrets_key_arn
}

resource "aws_secretsmanager_secret_version" "db_secrets_version" {
  secret_id     = aws_secretsmanager_secret.db_secret.id
  secret_string = jsonencode(var.db_credentials)
}

resource "aws_secretsmanager_secret" "email_secret" {
  name                    = local.email_secret_name
  recovery_window_in_days = 0
  kms_key_id              = var.secrets_key_arn

}

resource "aws_secretsmanager_secret_version" "email_secrets_version" {
  secret_id     = aws_secretsmanager_secret.email_secret.id
  secret_string = jsonencode(var.email_credentials)
}

resource "aws_secretsmanager_secret" "jwt_secret" {
  name                    = local.jwt_secret_name
  recovery_window_in_days = 0
  kms_key_id              = var.secrets_key_arn
}

resource "aws_secretsmanager_secret_version" "jwt_secrets_version" {
  secret_id     = aws_secretsmanager_secret.jwt_secret.id
  secret_string = var.jwt_secret_value
}
