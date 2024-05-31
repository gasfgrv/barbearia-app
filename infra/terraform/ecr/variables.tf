variable "ecr_repository_name" {
  type = string
  description = "Nome do repositório ECR"
}

variable "account_id" {
  type = string
  description = "Id da conta da AWS"
}

variable "ecr_kms_key_arn" {
  type = string
  description = "Arn da chave KMS para o ECR"
}
