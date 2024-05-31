locals {
  api_name      = "barbeariaApiGateway"
  vpc_link_name = "barbeariaVpcLink"
}

resource "aws_apigatewayv2_api" "api" {
  name          = local.api_name
  protocol_type = "HTTP"
}

resource "aws_apigatewayv2_vpc_link" "vpc_link" {
  name               = local.vpc_link_name
  security_group_ids = [var.lb_security_group_id]
  subnet_ids         = var.private_subnets
}

resource "aws_apigatewayv2_integration" "api_integration" {
  api_id             = aws_apigatewayv2_api.api.id
  integration_type   = "HTTP_PROXY"
  connection_id      = aws_apigatewayv2_vpc_link.vpc_link.id
  connection_type    = "VPC_LINK"
  description        = "Integração com VPC Link"
  integration_method = "ANY"
  integration_uri    = var.lb_listener_arn
}

resource "aws_apigatewayv2_route" "routes" {
  api_id    = aws_apigatewayv2_api.api.id
  route_key = "ANY /{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.api_integration.id}"
}

resource "aws_apigatewayv2_stage" "stage" {
  api_id      = aws_apigatewayv2_api.api.id
  name        = "$default"
  auto_deploy = true

  access_log_settings {
    destination_arn = var.api_gw_logs_arn
    format          = jsonencode({
      requestId      = "$context.requestId",
      ip             = "$context.identity.sourceIp",
      caller         = "$context.identity.caller",
      user           = "$context.identity.user",
      requestTime    = "$context.requestTime",
      httpMethod     = "$context.httpMethod",
      resourcePath   = "$context.resourcePath",
      status         = "$context.status",
      protocol       = "$context.protocol",
      responseLength = "$context.responseLength"
    })
  }

  default_route_settings {
    throttling_burst_limit   = 2000
    throttling_rate_limit    = 1000
    data_trace_enabled       = true
    logging_level            = "INFO"
    detailed_metrics_enabled = true
  }
}
