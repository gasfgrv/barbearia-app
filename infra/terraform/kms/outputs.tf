output "ecr_kms_key_arn" {
  value = aws_kms_key.ecr_kms_key.arn
  description = "ARN da chave KMS do ECR"
}

output "bucket_key_arn" {
  value = aws_kms_key.bucket_key.arn
  description = "ARN da chave KMS do bucket S3"
}

output "cloudwatch_kms_key_arn" {
  value = aws_kms_key.cloudwatch_kms_key.arn
  description = "ARN da chave KMS do cloudwatch"
}

output "secrets_key_arn" {
  value = aws_kms_key.secrets_key.arn
  description = "ARN da chave KMS do secrets manager"
}

output "rds_kms_key_arn" {
  value = aws_kms_key.rds_kms_key.arn
  description = "ARN da chave KMS do RDS"
}
