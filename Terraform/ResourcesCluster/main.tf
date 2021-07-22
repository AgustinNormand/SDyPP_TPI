# google_client_config and kubernetes provider must be explicitly specified like the following.
data "google_client_config" "default" {}

provider "kubernetes" {
  host                   = "https://${module.gke.endpoint}"
  token                  = data.google_client_config.default.access_token
  cluster_ca_certificate = base64decode(module.gke.ca_certificate)
}

module "gke" {
  source                     = "terraform-google-modules/kubernetes-engine/google//modules/private-cluster"
  project_id                 = "sdypp-316414"
  name                       = "gke-resources-cluster"
  region                     = "us-central1"
  zones                      = ["us-central1-a", "us-central1-b", "us-central1-f"]
  network                    = "sdypp-316414-vpc-resources"
  subnetwork                 = "sdypp-316414-subnet-resources"
  ip_range_pods              = "sdypp-316414-subnet-resources-pods"
  ip_range_services          = "sdypp-316414-subnet-resources-services"
  http_load_balancing        = false
  horizontal_pod_autoscaling = true
  enable_vertical_pod_autoscaling = false
  network_policy             = false
  enable_private_endpoint    = false
  enable_private_nodes       = true
  master_ipv4_cidr_block     = "10.0.0.0/28"
  identity_namespace         = "sdypp-316414.svc.id.goog"
  cluster_autoscaling        = {
    enabled       = true
    min_cpu_cores = 1
    max_cpu_cores = 10
    min_memory_gb = 1
    max_memory_gb = 64
  }

  node_pools = [
    {
      name                      = "default-node-pool"
      machine_type              = "g1-small"
      node_locations            = "us-central1-b,us-central1-c"
      min_count                 = 1
      max_count                 = 3
      local_ssd_count           = 0
      disk_size_gb              = 10
      disk_type                 = "pd-standard"
      image_type                = "COS"
      auto_repair               = true
      auto_upgrade              = true
      service_account           = "serviceaccount@sdypp-316414.iam.gserviceaccount.com"
      preemptible               = true
      initial_node_count        = 3
      node_metadata             = "GKE_METADATA_SERVER"
    },
  ]

  node_pools_oauth_scopes = {
    all = []

    default-node-pool = [
      "https://www.googleapis.com/auth/cloud-platform",
      "https://www.googleapis.com/auth/ndev.clouddns.readwrite"
    ]
  }

  node_pools_labels = {
    all = {}

    default-node-pool = {
      default-node-pool = true
    }
  }

  node_pools_metadata = {
    all = {}

    default-node-pool = {
      node-pool-metadata-custom-value = "my-node-pool"
    }
  }

  node_pools_taints = {
    all = []

    default-node-pool = [
      {
        key    = "default-node-pool"
        value  = true
        effect = "PREFER_NO_SCHEDULE"
      },
    ]
  }

  node_pools_tags = {
    all = []

    default-node-pool = [
      "default-node-pool",
    ]
  }

}
