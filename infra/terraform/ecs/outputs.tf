output "ecs_cluster_name" {
  value = aws_ecs_cluster.ecs_cluster.name
  description = "Nome do cluster do ECS"
}

output "ecs_service_name" {
  value = aws_ecs_service.ecs_service.name
  description = "Nome do serviço do ECS"
}
