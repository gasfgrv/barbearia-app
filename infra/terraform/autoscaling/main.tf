locals {
  service_namespace  = "ecs"
  resource_id        = "service/${var.ecs_cluster_name}/${var.ecs_service_name}"
  scalable_dimension = "ecs:service:DesiredCount"
  adjustment_type    = "ChangeInCapacity"
}

resource "aws_appautoscaling_target" "target" {
  service_namespace  = local.service_namespace
  resource_id        = local.resource_id
  scalable_dimension = local.scalable_dimension
  role_arn           = var.ecs_auto_scale_role_arn
  min_capacity       = 1
  max_capacity       = 3
}

resource "aws_appautoscaling_policy" "up" {
  name               = "barbeariaScaleUp"
  service_namespace  = local.service_namespace
  resource_id        = local.resource_id
  scalable_dimension = local.scalable_dimension

  step_scaling_policy_configuration {
    adjustment_type         = local.adjustment_type
    cooldown                = 60
    metric_aggregation_type = "Maximum"

    step_adjustment {
      metric_interval_lower_bound = 0
      scaling_adjustment          = 1
    }
  }

  depends_on = [aws_appautoscaling_target.target]
}

resource "aws_appautoscaling_policy" "down" {
  name               = "barbeariaScaleDown"
  service_namespace  = local.service_namespace
  resource_id        = local.resource_id
  scalable_dimension = local.scalable_dimension

  step_scaling_policy_configuration {
    adjustment_type         = local.adjustment_type
    cooldown                = 60
    metric_aggregation_type = "Maximum"

    step_adjustment {
      metric_interval_lower_bound = 0
      scaling_adjustment          = -1
    }
  }

  depends_on = [aws_appautoscaling_target.target]
}

resource "aws_cloudwatch_metric_alarm" "service_cpu_high" {
  alarm_name          = "barbeariaCpuUtilizationHigh"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = "60"
  statistic           = "Average"
  threshold           = "85"

  dimensions = {
    ClusterName = var.ecs_cluster_name
    ServiceName = var.ecs_service_name
  }

  alarm_actions = [aws_appautoscaling_policy.up.arn]
}

resource "aws_cloudwatch_metric_alarm" "service_cpu_low" {
  alarm_name          = "barbeariaCpuUtilizationLow"
  comparison_operator = "LessThanOrEqualToThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = "60"
  statistic           = "Average"
  threshold           = "10"

  dimensions = {
    ClusterName = var.ecs_cluster_name
    ServiceName = var.ecs_service_name
  }

  alarm_actions = [aws_appautoscaling_policy.down.arn]
}
