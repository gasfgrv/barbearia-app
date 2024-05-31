variable "ecs_task_execution_role_arn" {
  type = string
  description = "ARN da ECS task definition"
}

variable "fargate_cpu" {
  type = number
  description = "Quantidade alocada para a CPU"
}

variable "fargate_memory" {
  type = number
  description = "Quantidade alocada para a memória"
}

variable "aws_ecr_repository_url" {
  type = string
  description = "URL do repositório do ECR"
}

variable "aws_region" {
  type = string
  description = "Região da AWS"
}

variable "app_port" {
  type = number
  description = "Porta para da aplicação"
}

variable "app_count" {
  type = number
  description = "Quantidade de instâncias"
}

variable "vpc_id" {
  type = string
  description = "Id da VPC"
}

variable "lb_security_group_id" {
  type = string
  description = "Id do security group do load balancer"
}

variable "private_subnets_ids" {
  type = set(string)
  description = "Ids das subnets privadas"
}

variable "aws_lb_target_group_id" {
  type = string
  description = "Id do target group do load balancer"
}

variable "ecs_log_group_name" {
  type = string
  description = "Nome do log group para o ECS"
}

variable "ecs_security_group_id" {
  type = string
  description = "Id do security group do ECS"
}
