module "gcs_buckets" {
  source  = "terraform-google-modules/cloud-storage/google"
  version = "~> 2.1.0"
  project_id  = "sdypp-316414"
  names = ["task-bucket"]
  location = "us-central1"
  storage_class = "REGIONAL"
  prefix = "sdypp-316414"
  set_admin_roles = true
  versioning = {
    first = true
  }
}
