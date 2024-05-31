locals {
  ecs_cluster_name           = "barbeariaEcsCluster"
  ecs_task_definition_family = "barbeariaAppTask"
  ecs_container_name         = "barbeariaApp"
  ecs_service_name           = "barbeariaService"
}

resource "aws_ecs_cluster" "ecs_cluster" {
  name = local.ecs_cluster_name
}

resource "aws_ecs_task_definition" "ecs_task_definition" {
  family             = local.ecs_task_definition_family
  execution_role_arn = var.ecs_task_execution_role_arn
  network_mode       = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                = var.fargate_cpu
  memory             = var.fargate_memory
  container_definitions = jsonencode([
    {
      name        = local.ecs_container_name,
      image       = var.aws_ecr_repository_url
      cpu         = var.fargate_cpu
      memory      = var.fargate_memory
      networkMode = "awsvpc"
      healthCheck = {
        command = [
          "CMD-SHELL",
          "wget --spider --no-verbose http://localhost:8080/actuator/health || exit 1"
        ]
        interval    = 30
        timeout     = 10
        retries     = 5
        startPeriod = 30
      }
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = var.ecs_log_group_name
          awslogs-region        = var.aws_region
          awslogs-stream-prefix = "ecs"
        }
      }
      portMappings = [
        {
          containerPort = var.app_port,
          hostPort      = var.app_port
        }
      ]
    }
  ])
}

resource "aws_ecs_service" "ecs_service" {
  name            = local.ecs_service_name
  cluster         = aws_ecs_cluster.ecs_cluster.id
  task_definition = aws_ecs_task_definition.ecs_task_definition.arn
  desired_count   = var.app_count
  launch_type     = "FARGATE"

  network_configuration {
    security_groups = [var.ecs_security_group_id]
    subnets          = var.private_subnets_ids
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = var.aws_lb_target_group_id
    container_name   = local.ecs_container_name
    container_port   = var.app_port
  }
}
