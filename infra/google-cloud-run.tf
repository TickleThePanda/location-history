
resource "google_artifact_registry_repository" "location_history" {
  repository_id = "ttp-location-history"
  description   = "Location history registry"
  format        = "DOCKER"
}

resource "google_cloud_run_v2_job" "location_history_build_job" {
  name     = "location-history-builder"
  location = "europe-west1"

  template {
    template {
      containers {
        image = "${google_artifact_registry_repository.location_history.location}-docker.pkg.dev/${data.google_project.project.project_id}/${google_artifact_registry_repository.location_history.repository_id}/location-history:latest"
        resources {
          limits = {
            memory = "4Gi"
            cpu    = "1"
          }

        }
      }
      max_retries = 0
      timeout = 86400
    }
  }

  lifecycle {
    ignore_changes = [
      launch_stage,
    ]
  }

}
