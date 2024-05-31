output "ecs_task_execution_role_arn" {
  value = aws_iam_role.ecs_task_execution_role.arn
  description = "ARN da ECS task execution"
}

output "ecs_auto_scale_role_arn" {
  value = aws_iam_role.ecs_auto_scale_role.arn
  description = "ARN da role de auto scaling"
}

output "log_group_key_policy_document" {
  value = data.aws_iam_policy_document.log_group_key_policy_document.json
  description = "Policy do log group do KMS para o ECS"
}
