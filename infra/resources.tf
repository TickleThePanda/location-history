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

provider "aws" {
  region = "eu-central-1"

  default_tags {
    tags = {
      TerraformManaged = "True"
    }
  }
}

resource "aws_s3_bucket" "location_history" {
  arn            = "arn:aws:s3:::location-history"
  bucket         = "location-history"
  hosted_zone_id = "Z21DNDUVLTQW6Q"
}

resource "aws_s3_bucket_public_access_block" "location_history" {
  bucket = aws_s3_bucket.location_history.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}
