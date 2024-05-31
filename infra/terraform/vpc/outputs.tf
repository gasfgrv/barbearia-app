output "vpc_id" {
  value = aws_vpc.vpc.id
  description = "Id da VPC"
}

output "private_subnets" {
  value = aws_subnet.private.*.id
  description = "Ids das subnets privadas"
}

