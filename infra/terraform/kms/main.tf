locals {
  s3_alias                = "alias/s3-bucket-key"
  logs_alias              = "alias/cloudwatch-logs-key"
  ecr_alias               = "alias/ecs-key"
  rds_alias               = "alias/rds-alias"
  secrets_alias           = "alias/secrets-alias"
  deletion_window_in_days = 7
  enable_key_rotation     = true
}

resource "aws_kms_key" "cloudwatch_kms_key" {
  description             = "Chave KMS para os Log Groups do CloudWatch"
  deletion_window_in_days = local.deletion_window_in_days
  enable_key_rotation     = local.enable_key_rotation
  policy                  = var.log_group_key_policy
}

resource "aws_kms_alias" "logs_key_alias" {
  name          = local.logs_alias
  target_key_id = aws_kms_key.bucket_key.id
}

resource "aws_kms_key" "ecr_kms_key" {
  description             = "Chave KMS para encriptar imagens"
  deletion_window_in_days = local.deletion_window_in_days
  enable_key_rotation     = local.enable_key_rotation
}

resource "aws_kms_alias" "ecr_key_alias" {
  name          = local.ecr_alias
  target_key_id = aws_kms_key.bucket_key.id
}

resource "aws_kms_key" "rds_kms_key" {
  description             = "Chave KMS para RDS"
  deletion_window_in_days = local.deletion_window_in_days
  enable_key_rotation     = local.enable_key_rotation
}

resource "aws_kms_alias" "rds_key_alias" {
  name          = local.rds_alias
  target_key_id = aws_kms_key.bucket_key.id
}

resource "aws_kms_key" "bucket_key" {
  description             = "Chave KMS para o bucket S3"
  deletion_window_in_days = local.deletion_window_in_days
  enable_key_rotation     = local.enable_key_rotation
}

resource "aws_kms_alias" "bucket_key_alias" {
  name          = local.s3_alias
  target_key_id = aws_kms_key.bucket_key.id
}

resource "aws_kms_key" "secrets_key" {
  description             = "Chave KMS para os secrets"
  deletion_window_in_days = local.deletion_window_in_days
  enable_key_rotation     = local.enable_key_rotation
}

resource "aws_kms_alias" "secrets_key_alias" {
  name          = local.secrets_alias
  target_key_id = aws_kms_key.bucket_key.id
}
