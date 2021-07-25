module "vpc_deployments" {
    source  = "terraform-google-modules/network/google"
    version = "~> 3.0"

    project_id   = var.project_id
    network_name = format("%s-vpc-%s",var.project_id, var.deployments_vpc_identifier)
    routing_mode = "REGIONAL"

    subnets = [
        {
            subnet_name           = format("%s-subnet-%s",var.project_id, var.deployments_vpc_identifier)
            subnet_ip             = "192.168.64.0/20"
            subnet_region         = var.region
        },
    ]

    secondary_ranges = {
        (module.vpc_deployments.network_name) = [
            {
                range_name    = format("%s-subnet-%s-pods",var.project_id, var.deployments_vpc_identifier)
                ip_cidr_range = "192.168.80.0/20"
            },
            {
                range_name    = format("%s-subnet-%s-services",var.project_id, var.deployments_vpc_identifier)
                ip_cidr_range = "192.168.96.0/20"
            },
        ]
    }

    routes = [
        {
            name                   = format("%s-%s",module.vpc_deployments.network_name, "egress-internet")
            description            = "route through IGW to access internet"
            destination_range      = "0.0.0.0/0"
            tags                   = "egress-inet"
            next_hop_internet      = "true"
        },
    ]
  }

module "firewall_rules_deployments" {
  depends_on = [
    module.vpc_deployments
  ]
  source       = "terraform-google-modules/network/google//modules/firewall-rules"
  project_id   = var.project_id
  network_name = module.vpc_deployments.network_name

  rules = [{
    name                    = format("%s-%s",module.vpc_deployments.network_name, "allow-all-ingress")
    description             = null
    direction               = "INGRESS"
    priority                = null
    ranges                  = ["0.0.0.0/0"]
    source_tags             = null
    source_service_accounts = null
    target_tags             = null
    target_service_accounts = null
    allow = [{
      protocol = "all"
      ports    = []
    }]
    deny = []
    log_config = {
      metadata = "INCLUDE_ALL_METADATA"
    }
  },
  {
    name                    = format("%s-%s",module.vpc_deployments.network_name, "allow-all-egress")
    description             = null
    direction               = "EGRESS"
    priority                = null
    ranges                  = ["0.0.0.0/0"]
    source_tags             = null
    source_service_accounts = null
    target_tags             = null
    target_service_accounts = null
    allow = [{
      protocol = "all"
      ports    = []
    }]
    deny = []
    log_config = {
      metadata = "INCLUDE_ALL_METADATA"
    }
  }
  ]
}
  module "vpc_resources" {
    source  = "terraform-google-modules/network/google"
    version = "~> 3.0"

    project_id   = var.project_id
    network_name = format("%s-vpc-%s",var.project_id, var.resources_vpc_identifier)
    routing_mode = "REGIONAL"

    subnets = [
        {
            subnet_name           = format("%s-subnet-%s",var.project_id, var.resources_vpc_identifier)
            subnet_ip             = "192.168.128.0/20"
            subnet_region         = var.region
        },
    ]

    secondary_ranges = {
        (module.vpc_resources.network_name) = [
            {
                range_name    = format("%s-subnet-%s-pods",var.project_id, var.resources_vpc_identifier)
                ip_cidr_range = "192.168.144.0/20"
            },
            {
                range_name    = format("%s-subnet-%s-services",var.project_id, var.resources_vpc_identifier)
                ip_cidr_range = "192.168.160.0/20"
            },
        ]
    }

    routes = [
        {
            name                   = format("%s-%s",module.vpc_resources.network_name, "egress-internet")
            description            = "route through IGW to access internet"
            destination_range      = "0.0.0.0/0"
            tags                   = "egress-inet"
            next_hop_internet      = "true"
        },
    ]
  }

module "firewall_rules_resources" {
  depends_on = [
    module.vpc_resources
  ]

  source       = "terraform-google-modules/network/google//modules/firewall-rules"
  project_id   = var.project_id
  network_name = module.vpc_resources.network_name

  rules = [{
    name                    = format("%s-%s",module.vpc_resources.network_name, "allow-all-ingress")
    description             = null
    direction               = "INGRESS"
    priority                = null
    ranges                  = ["0.0.0.0/0"]
    source_tags             = null
    source_service_accounts = null
    target_tags             = null
    target_service_accounts = null
    allow = [{
      protocol = "all"
      ports    = []
    }]
    deny = []
    log_config = {
      metadata = "INCLUDE_ALL_METADATA"
    }
  },
  {
    name                    = format("%s-%s",module.vpc_resources.network_name, "allow-all-egress")
    description             = null
    direction               = "EGRESS"
    priority                = null
    ranges                  = ["0.0.0.0/0"]
    source_tags             = null
    source_service_accounts = null
    target_tags             = null
    target_service_accounts = null
    allow = [{
      protocol = "all"
      ports    = []
    }]
    deny = []
    log_config = {
      metadata = "INCLUDE_ALL_METADATA"
    }
  }
  ]
}

module "peering" {
  depends_on = [
    module.vpc_resources,
    module.vpc_deployments
  ]
  source = "terraform-google-modules/network/google//modules/network-peering"

  prefix        = "peering"
  local_network = module.vpc_deployments.network_self_link
  peer_network  = module.vpc_resources.network_self_link
}

module "vpc_management" {
    source  = "terraform-google-modules/network/google"
    version = "~> 3.0"

    project_id   = var.project_id
    network_name = format("%s-vpc-%s",var.project_id, var.management_vpc_identifier)
    routing_mode = "REGIONAL"

    subnets = [
        {
            subnet_name           = format("%s-subnet-%s",var.project_id, var.management_vpc_identifier)
            subnet_ip             = "192.168.64.0/20"
            subnet_region         = var.region
        },
    ]

    secondary_ranges = {
        (module.vpc_management.network_name) = [
            {
                range_name    = format("%s-subnet-%s-pods",var.project_id, var.management_vpc_identifier)
                ip_cidr_range = "192.168.80.0/20"
            },
            {
                range_name    = format("%s-subnet-%s-services",var.project_id, var.management_vpc_identifier)
                ip_cidr_range = "192.168.96.0/20"
            },
        ]
    }

    routes = [
        {
            name                   = format("%s-%s",module.vpc_management.network_name, "egress-internet")
            description            = "route through IGW to access internet"
            destination_range      = "0.0.0.0/0"
            tags                   = "egress-inet"
            next_hop_internet      = "true"
        },
    ]
  }

module "firewall_rules_management" {
  depends_on = [
    module.vpc_management
  ]
  source       = "terraform-google-modules/network/google//modules/firewall-rules"
  project_id   = var.project_id
  network_name = module.vpc_management.network_name

  rules = [{
    name                    = format("%s-%s",module.vpc_management.network_name, "allow-all-ingress")
    description             = null
    direction               = "INGRESS"
    priority                = null
    ranges                  = ["0.0.0.0/0"]
    source_tags             = null
    source_service_accounts = null
    target_tags             = null
    target_service_accounts = null
    allow = [{
      protocol = "all"
      ports    = []
    }]
    deny = []
    log_config = {
      metadata = "INCLUDE_ALL_METADATA"
    }
  },
  {
    name                    = format("%s-%s",module.vpc_management.network_name, "allow-all-egress")
    description             = null
    direction               = "EGRESS"
    priority                = null
    ranges                  = ["0.0.0.0/0"]
    source_tags             = null
    source_service_accounts = null
    target_tags             = null
    target_service_accounts = null
    allow = [{
      protocol = "all"
      ports    = []
    }]
    deny = []
    log_config = {
      metadata = "INCLUDE_ALL_METADATA"
    }
  }
  ]
}

module "cloud_router_deployments" {
  depends_on = [
    module.vpc_deployments
  ]
  source  = "terraform-google-modules/cloud-router/google"
  version = "~> 0.4"

  name    = format("router-%s", module.vpc_deployments.network_name)
  project = var.project_id
  region  = var.region
  network = module.vpc_deployments.network_name
}

module "cloud_router_management" {
  depends_on = [
    module.vpc_management
  ]
  source  = "terraform-google-modules/cloud-router/google"
  version = "~> 0.4"

  name    = format("router-%s", module.vpc_management.network_name)
  project = var.project_id
  region  = var.region
  network = module.vpc_management.network_name
}

module "cloud_router_resources" {
  depends_on = [
    module.vpc_resources
  ]
  source  = "terraform-google-modules/cloud-router/google"
  version = "~> 0.4"

  name    = format("router-%s", module.vpc_resources.network_name)
  project = var.project_id
  region  = var.region
  network = module.vpc_resources.network_name
}

module "cloud-nat_deployments" {
  depends_on = [
    module.cloud_router_deployments
  ]
  name       = format("nat-%s", module.cloud_router_deployments.router.name)
  source     = "terraform-google-modules/cloud-nat/google"
  version    = "~> 1.2"
  project_id = var.project_id
  region     = var.region
  router     = module.cloud_router_deployments.router.name
}

module "cloud-nat_management" {
  depends_on = [
    module.cloud_router_management
  ]
  name       = format("nat-%s", module.cloud_router_management.router.name)
  source     = "terraform-google-modules/cloud-nat/google"
  version    = "~> 1.2"
  project_id = var.project_id
  region     = var.region
  router     = module.cloud_router_management.router.name
}

module "cloud-nat_resources" {
  depends_on = [
    module.cloud_router_resources
  ]
  name       = format("nat-%s", module.cloud_router_resources.router.name)
  source     = "terraform-google-modules/cloud-nat/google"
  version    = "~> 1.2"
  project_id = var.project_id
  region     = var.region
  router     = module.cloud_router_resources.router.name
}

data "google_client_config" "default" {}

provider "kubernetes" {
  host                   = "https://${module.gke.endpoint}"
  token                  = data.google_client_config.default.access_token
  cluster_ca_certificate = base64decode(module.gke.ca_certificate)
}

module "gke" {
  source                     = "terraform-google-modules/kubernetes-engine/google//modules/private-cluster"
  project_id                 = var.project_id
  name                       = var.deployments_cluster_name
  region                     = var.region
  zones                      = [var.zone1, var.zone2, var.zone3]
  network                    = module.vpc_deployments.network_name
  subnetwork                 = format("%s-subnet-%s",var.project_id, var.deployments_vpc_identifier)
  ip_range_pods              = format("%s-subnet-%s-pods",var.project_id, var.deployments_vpc_identifier)
  ip_range_services          = format("%s-subnet-%s-services",var.project_id, var.deployments_vpc_identifier)
  http_load_balancing        = false
  horizontal_pod_autoscaling = true
  enable_vertical_pod_autoscaling = false
  network_policy             = false
  enable_private_endpoint    = false
  enable_private_nodes       = true
  master_ipv4_cidr_block     = "10.0.0.0/28"
  identity_namespace         = format("%s.svc.id.goog",var.project_id)
  cluster_autoscaling        = {
    enabled       = true
    gpu_resources = []
    min_cpu_cores = var.deployments_min_cpu
    max_cpu_cores = var.deployments_max_cpu
    min_memory_gb = var.deployments_min_mem
    max_memory_gb = var.deployments_max_mem
  }

  node_pools = [
    {
      name                      = "default-node-pool"
      machine_type              = var.machine_type
      node_locations            = var.node_locations
      min_count                 = 1
      max_count                 = 3
      local_ssd_count           = 0
      disk_size_gb              = 10
      disk_type                 = "pd-standard"
      image_type                = "COS"
      auto_repair               = true
      auto_upgrade              = true
      service_account           = var.service_account
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