resource "aws_ecr_repository" "barbearia_app" {
  name                 = var.ecr_repository_name
  force_delete         = true

  image_scanning_configuration {
    scan_on_push = true
  }

  encryption_configuration {
    encryption_type = "KMS"
    kms_key         = var.ecr_kms_key_arn
  }
}

data "aws_ecr_lifecycle_policy_document" "ecr_lifecycle_policy_document" {
  rule {
    priority    = 1
    description = "Manter apenas as ultimas 3 imagens não taggeadas"

    selection {
      count_number = 3
      count_type   = "imageCountMoreThan"
      tag_status   = "untagged"
    }

    action {
      type = "expire"
    }
  }
}

resource "aws_ecr_lifecycle_policy" "default_policy" {
  repository = aws_ecr_repository.barbearia_app.name
  policy     = data.aws_ecr_lifecycle_policy_document.ecr_lifecycle_policy_document.json
}

resource "null_resource" "docker_packaging" {
  provisioner "local-exec" {
    command = <<EOF
cd ../../
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin ${var.account_id}.dkr.ecr.us-east-1.amazonaws.com
mvn clean package -DskipTests
docker build -t "${aws_ecr_repository.barbearia_app.repository_url}:latest" .
docker push "${aws_ecr_repository.barbearia_app.repository_url}:latest"
EOF
  }

  triggers = {
    "run_at" = timestamp()
  }

  depends_on = [
    aws_ecr_repository.barbearia_app
  ]
}
