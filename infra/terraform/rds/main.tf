locals {
  db_subnet_group_name       = "barbearia.db.subnet.group"
  rds_engine                 = "postgres"
  rds_instance_class         = "db.t3.micro"
  rds_db                     = "barbearia"
  rds_size                   = 20
}

resource "aws_db_subnet_group" "db_subnet_group" {
  name       = local.db_subnet_group_name
  subnet_ids = var.private_subnets
}

resource "aws_db_instance" "db_instance" {
  performance_insights_enabled    = true
  identifier                      = local.rds_db
  engine                          = local.rds_engine
  instance_class                  = local.rds_instance_class
  allocated_storage               = local.rds_size
  db_name                         = local.rds_db
  username                        = var.db_username
  password                        = var.db_password
  db_subnet_group_name            = aws_db_subnet_group.db_subnet_group.name
  vpc_security_group_ids          = [var.rds_security_group_id]
  skip_final_snapshot             = true
  publicly_accessible             = false
  backup_retention_period         = 10
  storage_encrypted               = true
  kms_key_id                      = var.rds_kms_key_arn
  performance_insights_kms_key_id = var.rds_kms_key_arn
}
