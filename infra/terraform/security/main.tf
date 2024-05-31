resource "aws_security_group" "rds_security_group" {
  name_prefix = "barbearia.rds.sg"
  description = "Security group for RDS"
  vpc_id      = var.vpc_id

  ingress {
    description     = "Allow PostgreSQL access from anywhere"
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.ecs_security_group.id]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "ecs_security_group" {
  name        = "barbeariaEcsTasksSecurityGroup"
  description = "allow inbound access from the ALB only"
  vpc_id      = var.vpc_id

  ingress {
    description     = "regra de entrada do ecs"
    protocol        = "tcp"
    from_port       = var.app_port
    to_port         = var.app_port
    security_groups = [aws_security_group.lb_security_group.id]
  }

  egress {
    description = "regra de saida do ecs"
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "lb_security_group" {
  name        = "barbeariaLoadBalancerSecurityGroup"
  description = "controls access to the ALB"
  vpc_id      = var.vpc_id

  ingress {
    description = "regra de entrada do load balancer"
    protocol    = "tcp"
    from_port   = var.app_port
    to_port     = var.app_port
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "regra de saida do load balancer"
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}
