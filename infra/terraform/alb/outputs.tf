output "target_group_id" {
  value       = aws_lb_target_group.lb_target_group.id
  description = "Id do target group do load balancer"
}

output "lb_listener_arn" {
  value       = aws_lb_listener.lb_listener.arn
  description = "ARN do listener do load balancer"
}
