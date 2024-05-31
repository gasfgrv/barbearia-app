variable "db_user" {
  type        = string
  description = "Usuário do banco de dados"
}

variable "db_pass" {
  type        = string
  description = "Senha do banco de dados"
}

variable "email_host" {
  type        = string
  description = "Host do servidor de e-mail"
}

variable "email_port" {
  type        = number
  description = "Porta do servidor de e-mail"
  default     = 25
}

variable "email_user" {
  type        = string
  description = "Usuário do servidor de e-mail"
}

variable "email_pass" {
  type        = string
  description = "Senha do servidor de e-mail"
}

variable "smtp_auth" {
  type        = bool
  description = "Permitir a autenticação SMTP para o sender do e-mail"
  default     = true
}

variable "smtp_starttls" {
  type        = bool
  description = "Permite a comunicação encriptada dos e-mails"
  default     = true
}

variable "token_secret" {
  type        = string
  description = "Secret para a assinatura do token jwt"
}

variable "repository_name" {
  type        = string
  description = "Nome do repositório do ECR"
}

variable "region" {
  type        = string
  description = "Região da AWS"
  default     = "us-east-1"
}

variable "port" {
  type        = number
  description = "Porta em que a aplicação é exposta"
  default     = 8080
}

variable "instances" {
  type        = number
  description = "Quantidade de containers que serão executados"
  default     = 1
}

variable "cpu" {
  type        = number
  description = "Tamanho da CPU"
  default     = 1024
}

variable "memory" {
  type        = number
  description = "Memória alocada"
  default     = 2048
}
