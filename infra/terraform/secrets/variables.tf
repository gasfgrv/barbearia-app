variable "db_credentials" {
  type = map(string)
  description = "Credênciais do banco de dados"
}

variable "email_credentials" {
  type = map(string)
  description = "Credênciais do servidor de e-mail"
}

variable "jwt_secret_value" {
  type = string
  description = "Secret para JWT"
}

variable "secrets_key_arn" {
  type = string
  description = "ARN da chave KMS para o secrets"
}
