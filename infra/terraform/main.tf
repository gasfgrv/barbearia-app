terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.50.0"
    }
  }
}

provider "aws" {
  shared_config_files      = ["~/.aws/config"]
  shared_credentials_files = ["~/.aws/credentials"]
  profile                  = "default"
  region                   = var.region
}

data "aws_caller_identity" "current" {}

module "s3" {
  source         = "./s3"
  bucket_key_arn = module.kms.bucket_key_arn
}

module "secrets" {
  source = "./secrets"

  db_credentials = {
    username = var.db_user
    password = var.db_pass
  }

  email_credentials = {
    host     = var.email_host
    port     = var.email_port
    username = var.email_user
    password = var.email_pass
    auth     = var.smtp_auth
    starttls = var.smtp_starttls
  }

  jwt_secret_value = var.token_secret
  secrets_key_arn  = module.kms.secrets_key_arn
}

module "ecr" {
  source              = "./ecr"
  ecr_repository_name = var.repository_name
  account_id          = data.aws_caller_identity.current.account_id
  ecr_kms_key_arn     = module.kms.ecr_kms_key_arn
}

module "vpc" {
  source   = "./vpc"
  az_count = 2
}

module "security" {
  source   = "./security"
  app_port = var.port
  vpc_id   = module.vpc.vpc_id
}

module "rds" {
  source                = "./rds"
  vpc_id                = module.vpc.vpc_id
  private_subnets       = module.vpc.private_subnets
  db_username           = module.secrets.db_username
  db_password           = module.secrets.db_password
  rds_kms_key_arn       = module.kms.secrets_key_arn
  rds_security_group_id = module.security.rds_security_group_id
}

module "iam" {
  source                 = "./iam"
  aws_region             = var.region
  account_id             = data.aws_caller_identity.current.account_id
  bucket_arn             = module.s3.bucket_arn
  rds_resource_id        = module.rds.resource_id
  secrets_arn            = module.secrets.secrets_arn
  cloudwatch_kms_key_arn = module.kms.cloudwatch_kms_key_arn
}

module "logs" {
  source                 = "./logs"
  cloudwatch_kms_key_arn = module.kms.cloudwatch_kms_key_arn
}

module "kms" {
  source               = "./kms"
  log_group_key_policy = module.iam.log_group_key_policy_document
}

module "alb" {
  source               = "./alb"
  app_port             = var.port
  private_subnets_ids  = module.vpc.private_subnets
  vpc_id               = module.vpc.vpc_id
  lb_security_group_id = module.security.alb_security_group_id
}

module "ecs" {
  source                      = "./ecs"
  app_count                   = var.instances
  app_port                    = var.port
  aws_ecr_repository_url      = module.ecr.repository_url
  aws_lb_target_group_id      = module.alb.target_group_id
  aws_region                  = var.region
  ecs_task_execution_role_arn = module.iam.ecs_task_execution_role_arn
  fargate_cpu                 = var.cpu
  fargate_memory              = var.memory
  lb_security_group_id        = module.security.alb_security_group_id
  private_subnets_ids         = module.vpc.private_subnets
  vpc_id                      = module.vpc.vpc_id
  ecs_security_group_id       = module.security.ecs_security_group_id
  ecs_log_group_name          = module.logs.ecs_log_group_name
  depends_on                  = [module.ecr]
}

module "autoscaling" {
  source                  = "./autoscaling"
  ecs_auto_scale_role_arn = module.iam.ecs_auto_scale_role_arn
  ecs_cluster_name        = module.ecs.ecs_cluster_name
  ecs_service_name        = module.ecs.ecs_service_name
}

module "apigateway" {
  source               = "./apigateway"
  lb_listener_arn      = module.alb.lb_listener_arn
  lb_security_group_id = module.security.alb_security_group_id
  private_subnets      = module.vpc.private_subnets
  api_gw_logs_arn      = module.logs.api_gw_logs_arn
}

output "url" {
  value       = module.apigateway.url
  description = "URL do API gateway para acesso à aplicação"
}
