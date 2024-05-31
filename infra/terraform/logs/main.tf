locals {
  ecs_log_group_name     = "/ecs/barbeariaApp"
  ecs_log_stream_name    = "barbeariaLogStream"
  api_gtw_log_group_name = "/gateway/api"
  retention              = 30
}

resource "aws_cloudwatch_log_group" "log_group" {
  name              = local.ecs_log_group_name
  retention_in_days = local.retention
  kms_key_id        = var.cloudwatch_kms_key_arn
}

resource "aws_cloudwatch_log_stream" "log_stream" {
  name           = local.ecs_log_stream_name
  log_group_name = aws_cloudwatch_log_group.log_group.name
}

resource "aws_cloudwatch_log_group" "api_gw_logs" {
  name              = local.api_gtw_log_group_name
  retention_in_days = local.retention
}
