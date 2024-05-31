output "ecs_log_group_name" {
  value       = aws_cloudwatch_log_group.log_group.name
  description = "Nome do log group para o ECS"
}

output "api_gw_logs_arn" {
  value       = aws_cloudwatch_log_group.api_gw_logs.arn
  description = "ARN do log group para o API gateway"
}
