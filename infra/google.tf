provider "google" {
  project     = "ticklethepanda-web"
  region      = "europe-west1"
}

data "google_project" "project" {
}
