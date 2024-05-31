variable "lb_security_group_id" {
  type        = string
  description = "Id do security group do load balancer"
}

variable "private_subnets" {
  type = set(string)
  description = "Ids das subnets privadas da VPC"
}

variable "lb_listener_arn" {
  type        = string
  description = "ARN do listener do load balancer"
}

variable "api_gw_logs_arn" {
  type        = string
  description = "ARN do log group exclusivo para os logs do API Gateway"
}
