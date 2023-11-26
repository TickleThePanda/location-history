
resource "google_storage_bucket" "history_config" {
  name          = "ttp-location-history-config"
  location      = "europe-west1"
  storage_class = "STANDARD"
  force_destroy = true

  uniform_bucket_level_access = true
}

resource "google_storage_bucket" "history_gallery" {
  name          = "ttp-location-history-gallery"
  location      = "europe-west1"
  storage_class = "STANDARD"
  force_destroy = true

  uniform_bucket_level_access = true
}

resource "google_storage_bucket_iam_member" "history_gallery_public" {
  bucket = google_storage_bucket.history_gallery_public.name
  role   = "roles/storage.objectViewer"
  member = "allUsers"
}
