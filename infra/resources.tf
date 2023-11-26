terraform {
  cloud {
    organization = "TickleThePanda"
    workspaces {
      name = "location-history"
    }
  }

  required_version = ">=1.2.8"
}
