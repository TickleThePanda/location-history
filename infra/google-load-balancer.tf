
# === Load balancer ===

resource "google_compute_global_forwarding_rule" "default" {
  name                  = "http-lb-forwarding-rule"
  ip_protocol           = "TCP"
  load_balancing_scheme = "EXTERNAL_MANAGED"
  port_range            = "443"
  target                = google_compute_target_https_proxy.default.id
  ip_address            = google_compute_global_address.default.id
}

resource "google_compute_target_https_proxy" "default" {
  name    = "http-lb-proxy"
  url_map = google_compute_url_map.default.id

  ssl_certificates = [
    google_compute_managed_ssl_certificate.lb_default.name
  ]
  depends_on = [
    google_compute_managed_ssl_certificate.lb_default
  ]
}

resource "google_compute_url_map" "default" {
  name = "http-lb"

  default_service = google_compute_backend_bucket.history_gallery.id
}

resource "google_compute_backend_bucket" "history_gallery" {
  name        = "location-history-gallery"
  description = "Location history gallery"
  bucket_name = google_storage_bucket.history_gallery.name
}


# === SSL and IP ===

resource "google_compute_global_address" "default" {
  name = "load-balancer-ip"
}

resource "google_compute_managed_ssl_certificate" "lb_default" {
  name     = "location-history-ticklethepanda-ssl-cert"

  managed {
    domains = ["location-history.ticklethepanda.dev"]
  }
}


# === HTTP Redirect ===

resource "google_compute_global_forwarding_rule" "http-redirect" {
  name       = "http-redirect"
  target     = google_compute_target_http_proxy.http-redirect.self_link
  ip_address = google_compute_global_address.default.address
  port_range = "80"
}

resource "google_compute_url_map" "http-redirect" {
  name = "http-redirect"

  default_url_redirect {
    redirect_response_code = "MOVED_PERMANENTLY_DEFAULT"
    strip_query            = false
    https_redirect         = true
  }
}

resource "google_compute_target_http_proxy" "http-redirect" {
  name    = "http-redirect"
  url_map = google_compute_url_map.http-redirect.self_link
}