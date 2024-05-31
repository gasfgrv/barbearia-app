locals {
  ecs_task_execution_role_name          = "barbeariaEcsTaskExecutionRole"
  ecs_auto_scale_role_name              = "barbeariaEcsAutoScaleRole"
  ecs_task_execution_policy_arn         = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
  ecs_auto_scale_policy_arn             = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceAutoscaleRole"
  custom_ecs_task_execution_policy_name = "customEcsTaskExecutionPolicy"
}

resource "aws_iam_role" "ecs_task_execution_role" {
  name               = local.ecs_task_execution_role_name
  assume_role_policy = data.aws_iam_policy_document.ecs_task_execution_policy_document.json
}

data "aws_iam_policy_document" "ecs_task_execution_policy_document" {
  version = "2012-10-17"

  statement {
    actions = [
      "sts:AssumeRole"
    ]

    principals {
      identifiers = [
        "ecs-tasks.amazonaws.com"
      ]
      type = "Service"
    }

    effect = "Allow"
  }
}

data "aws_iam_policy_document" "custom_ecs_task_execution_policy_document" {
  version = "2012-10-17"

  statement {
    effect  = "Allow"
    actions = [
      "s3:PutObject",
      "s3:GetObject",
      "s3:ListBucket"
    ]
    resources = [
      var.bucket_arn
    ]
  }

  statement {
    effect  = "Allow"
    actions = [
      "secretsmanager:GetSecretValue"
    ]
    resources = var.secrets_arn
  }

  statement {
    effect  = "Allow"
    actions = [
      "rds-db:connect"
    ]
    resources = [
      "arn:aws:rds-db:${var.aws_region}:${var.account_id}:dbuser:${var.rds_resource_id}/postgres"
    ]
  }
}

resource "aws_iam_policy" "custom_ecs_task_execution_policy" {
  name        = local.custom_ecs_task_execution_policy_name
  description = "Política personalizada para a execução de tarefas do ECS"
  policy      = data.aws_iam_policy_document.custom_ecs_task_execution_policy_document.json
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy_attachment" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = local.ecs_task_execution_policy_arn
}

resource "aws_iam_role_policy_attachment" "custom_policy_attachment" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = aws_iam_policy.custom_ecs_task_execution_policy.arn
}


resource "aws_iam_role" "ecs_auto_scale_role" {
  name               = local.ecs_auto_scale_role_name
  assume_role_policy = data.aws_iam_policy_document.ecs_auto_scale_policy.json
}

data "aws_iam_policy_document" "ecs_auto_scale_policy" {
  version = "2012-10-17"
  statement {

    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["application-autoscaling.amazonaws.com"]
    }

    effect = "Allow"
  }
}

resource "aws_iam_role_policy_attachment" "ecs_auto_scale_role" {
  role       = aws_iam_role.ecs_auto_scale_role.name
  policy_arn = local.ecs_auto_scale_policy_arn
}

data "aws_iam_policy_document" "log_group_key_policy_document" {
  version = "2012-10-17"

  statement {
    effect    = "Allow"
    actions   = ["kms:*"]
    resources = ["*"]

    principals {
      identifiers = ["arn:aws:iam::${var.account_id}:root"]
      type        = "AWS"
    }
  }

  statement {
    effect  = "Allow"
    actions = [
      "kms:Encrypt",
      "kms:Decrypt",
      "kms:ReEncrypt*",
      "kms:GenerateDataKey*",
      "kms:DescribeKey"
    ]
    resources = ["*"]

    principals {
      identifiers = ["logs.${var.aws_region}.amazonaws.com"]
      type        = "Service"
    }
  }
}

data "aws_iam_policy_document" "cloudwatch_logs_kms_policy_document" {
  statement {
    actions = [
      "logs:AssociateKmsKey",
      "logs:DescribeLogGroups"
    ]
    resources = [
      var.cloudwatch_kms_key_arn
    ]
  }
}

resource "aws_iam_policy" "cloudwatch_logs_kms_policy" {
  name   = "cloudWatchLogsKmsPolicy"
  policy = data.aws_iam_policy_document.cloudwatch_logs_kms_policy_document.json
}

data "aws_iam_policy_document" "kms_cmk_logs_policy_document" {
  version = "2012-10-17"

  statement {
    effect  = "Allow"
    actions = ["sts:AssumeRole"]
    principals {
      identifiers = ["ec2.amazonaws.com"]
      type        = "Service"
    }
  }
}

resource "aws_iam_role" "kms_cmk_logs_role" {
  name               = "cloudWatchLogsKmsRole"
  assume_role_policy = data.aws_iam_policy_document.kms_cmk_logs_policy_document.json
}
