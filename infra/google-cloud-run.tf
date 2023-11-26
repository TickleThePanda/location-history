
resource "google_artifact_registry_repository" "location_history" {
  location      = "europe-west1"
  repository_id = "ttp-location-history"
  description   = "Location history registry"
  format        = "DOCKER"
}

resource "google_cloud_run_v2_job" "location_history_builder" {
  name     = "location-history-builder"
  location = "europe-west1"

  template {
    template {
      containers {
        image = "${google_artifact_registry_repository.location_history.location}-docker.pkg.dev/${data.google_project.project.project_id}/${google_artifact_registry_repository.location_history.repository_id}/location-history:latest"
      }
    }
  }

  lifecycle {
    ignore_changes = [
      launch_stage,
    ]
  }
}
