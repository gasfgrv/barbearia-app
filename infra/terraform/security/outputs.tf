output "rds_security_group_id" {
  value = aws_security_group.rds_security_group.id
  description = "Id do security group para o RDS"
}

output "ecs_security_group_id" {
  value = aws_security_group.ecs_security_group.id
  description = "Id do security group para o ECS"
}

output "alb_security_group_id" {
  value = aws_security_group.lb_security_group.id
  description = "Id do security group para o load balancer"
}
