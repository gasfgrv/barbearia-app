variable "aws_region" {
  type = string
  description = "Região da AWS"
}

variable "bucket_arn" {
  type = string
  description = "ARN do bucket do S3"
}

variable "secrets_arn" {
  type = set(string)
  description = "ARNs dos secrets manager"
}

variable "account_id" {
  type = string
  description = "Id da conta"
}

variable "rds_resource_id" {
  type = string
  description = "Resource Id da instância do RDS"
}

variable "cloudwatch_kms_key_arn" {
  type = string
  description = "ARN da Chave KMS para o cloudwatch"
}
