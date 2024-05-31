variable "vpc_id" {
  type        = string
  description = "Id da VPC onde está o load balancer"
}

variable "app_port" {
  type        = number
  description = "Porta da aplicação que será usado pelo listener"
}

variable "private_subnets_ids" {
  type = set(string)
  description = "Ids das subnets privadas na vpc"
}

variable "lb_security_group_id" {
  type        = string
  description = "Id do security group do load balancer"
}
