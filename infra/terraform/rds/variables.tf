variable "vpc_id" {
  description = "id da VPC"
  type        = string
}

variable "private_subnets" {
  description = "Ids das subnets privadas do VPC"
  type = set(string)
}

variable "db_username" {
  description = "Usuário do banco de dados"
  type        = string
}

variable "db_password" {
  description = "Senha do banco de dados"
  type        = string
}

variable "rds_kms_key_arn" {
  description = "ARN da chave KMS para o RDS"
  type        = string
}

variable "rds_security_group_id" {
  description = "Id do security group para o RDS"
  type        = string
}
