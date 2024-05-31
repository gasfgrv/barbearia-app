variable "ecs_cluster_name" {
  type        = string
  description = "Nome do cluster ECS"
}

variable "ecs_service_name" {
  type        = string
  description = "Nome do serviço no ECS"
}

variable "ecs_auto_scale_role_arn" {
  type        = string
  description = "ARN da role de auto scaling do ECS"
}
