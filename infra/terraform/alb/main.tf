locals {
  lb_name              = "barbeariaLoadBalancer"
  lb_target_group_name = "barbeariaTargetGroup"
}

resource "aws_lb" "lb" {
  name                       = local.lb_name
  subnets                    = var.private_subnets_ids
  security_groups = [var.lb_security_group_id]
  internal                   = true
  drop_invalid_header_fields = true
}

resource "aws_lb_target_group" "lb_target_group" {
  name        = local.lb_target_group_name
  port        = 80
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"

  health_check {
    healthy_threshold   = "3"
    interval            = "30"
    protocol            = "HTTP"
    matcher             = "200"
    timeout             = "3"
    path                = "/actuator/health"
    unhealthy_threshold = "2"
  }
}

resource "aws_lb_listener" "lb_listener" {
  load_balancer_arn = aws_lb.lb.id
  port              = var.app_port
  protocol          = "HTTP"

  default_action {
    target_group_arn = aws_lb_target_group.lb_target_group.id
    type             = "forward"
  }
}
