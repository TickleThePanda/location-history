terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
      version = ">=4.28"
    }
  }
  cloud {
    organization = "TickleThePanda"
    workspaces {
      name = "location-history"
    }
  }

  required_version = ">=1.2.8"
}
