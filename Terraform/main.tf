provider "google" {
  project = "sdypp-framework-ago"
  region  = "us-central1"
  zone    = "us-central1-a"
}

resource "google_compute_instance" "terraform-instance" {
  name         = "terraform-instance"
  machine_type = "f1-micro"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-9"
    }
  }

  network_interface {
    # A default network is created for all GCP projects
    network = "sdypp-framework-ago-vpc-deployment"
    access_config {
    }
  }
}