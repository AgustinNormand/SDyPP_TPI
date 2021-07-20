# Armado de cluster GKE con Terraform

[Revisar!!] La idea sería explicar cómo levantar un cluster de Kubernetes Engine de tipo estándar en Google Cloud Platform.  

## Creación de un cluster de Kubernetes en Google Cloud
1. Crear una cuenta gratuita en Google Cloud Platform ingresando el número de tarjeta.
2. Crear un proyecto.
3. Habilitar [Kubernetes Engine API](https://console.cloud.google.com/marketplace/product/google/container.googleapis.com).
4. Crear un archivo de credenciales (IAM y Administracion -> Cuentas de Servicio -> Crear cuenta de Servicio 
   -> Definir nombre de cuenta de servicio -> Crear y continuar -> Seleccionar permiso "Propietario" -> Seleccionar cuenta 
   -> Claves -> Agregar Clave -> Crear clave nueva -> JSON -> Renombrar a `credentials.json`)
5. Copiar el archivo `credentials.json` al directorio `/usr/src/google_credentials/`.
6. Reemplazar los valores de `project_id` y `region` dentro de `Terraform/ManagementCluster/terraform.tfvars` y 
   `Terraform/DeployCluster/terraform.tfvars`. Pueden encontrarse las regiones disponibles en [este enlace](https://cloud.google.com/compute/docs/regions-zones).
7. Ejecutar los comandos `terraform init` y `terraform apply -auto-approve` dentro del directorio `Terraform/ManagementCluster/` 
   y luego dentro de `Terraform/ManagementDeploy/`.   

Una vez ejecutados estos pasos, tendremos corriendo el clúster de *management* y el de *deployment*. [Ampliar sobre terraform y lo que permite???]

## Acceso al cluster creado
1. Instalar [gcloud](https://cloud.google.com/sdk/docs/install). 
2. Logearse en gcloud: `gcloud auth login`.
3. Crear el archivo kube config del cluster de *management* ejecutando el siguiente comando
   ```
   gcloud container clusters get-credentials $(terraform output -raw kubernetes_cluster_name) --region $(terraform output -raw region)
   ```
   dentro del directorio `Terraform/ManagementCluster/`. Luego, guardar una copia del archivo de configuración del cluster
   ejecutando `cp $HOME/.kube/config $HOME/.kube/config.management`.
4. Volver a ejecutar el comando del punto 3. esta vez en el directorio `Terraform/DeployCluster/`, guardando el archivo 
   de configuración mediante el comando `cp $HOME/.kube/config $HOME/.kube/config.deployment`.

Una vez ejecutados estos pasos, tendremos las credenciales de cada cluster para poder cambiar de contexto según las necesidades.   

## Establecer el cluster actual
Al ejecutar el comando `kubectl`, el cluster accedido es el correspondiente al archivo de configuración `$HOME/.kube/config`.
Para cambiar de contexto, tendremos que colocar el archivo generado en la sección anterior para cada uno de los clusters creados, según corresponda.

### Cambiar al cluster de *management*
1. Copiar archivo de configuración al directorio de kubectl con el comando:
```
cp $HOME/.kube/config.management $HOME/.kube/config 
```

### Cambiar al cluster de *deployment*
1. Copiar archivo de configuración al directorio de kubectl con el comando:
```
cp $HOME/.kube/config.deployment $HOME/.kube/config 
```
   

## Referencias

[Provision a GKE Cluster learn guide](https://learn.hashicorp.com/terraform/kubernetes/provision-gke-cluster)

[Terraform container cluster](https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/container_cluster)



## TO ADD

gcloud compute networks create sdypp-316414-vpc-resources --project=sdypp-316414 --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional 

gcloud compute networks subnets create sdypp-316414-subnet-resources --project=sdypp-316414 --range=10.0.0.0/9 --network=sdypp-316414-vpc-resources --region=us-central1 --secondary-range=sdypp-316414-subnet-resources-pods=10.140.0.0/20,sdypp-316414-subnet-resources-services=10.141.0.0/20

gcloud compute networks create sdypp-316414-vpc-deployment --project=sdypp-316414 --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional 

gcloud compute networks subnets create sdypp-316414-subnet-deployment --project=sdypp-316414 --range=11.0.0.0/9 --network=sdypp-316414-vpc-deployment --region=us-central1 --secondary-range=sdypp-316414-subnet-deployment-pods=10.142.0.0/20,sdypp-316414-subnet-deployment-services=10.143.0.0/20

gcloud compute networks create sdypp-316414-vpc-management --project=sdypp-316414 --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional 

gcloud compute networks subnets create sdypp-316414-subnet-management --project=sdypp-316414 --range=12.0.0.0/9 --network=sdypp-316414-vpc-management --region=us-central1 --secondary-range=sdypp-316414-subnet-management-pods=10.144.0.0/20,sdypp-316414-subnet-management-services=10.145.0.0/20

gcloud compute --project=sdypp-316414 firewall-rules create deployments-resources --direction=INGRESS --priority=1000 --network=sdypp-316414-vpc-resources --action=ALLOW --rules=all --source-ranges=11.0.0.0/9

gcloud compute --project=sdypp-316414 firewall-rules create resources-deployments --direction=INGRESS --priority=1000 --network=sdypp-316414-vpc-deployment --action=ALLOW --rules=all --source-ranges=10.0.0.0/9

https://registry.terraform.io/modules/terraform-google-modules/kubernetes-engine/google/latest/submodules/private-cluster

Documentar: HPA, Cluster auto sclaer, preemtible instances, Router, NAT, VPC Peering, reglas de firewall

Que es y porque creamos un cluster privado?
Como lo creamos?
Como se accede a los nodos desde internet? 

gcloud container clusters get-credentials gke-resources-cluster --region us-central1 --project sdypp-316414

gcloud container clusters get-credentials gke-management-cluster --region us-central1 --project sdypp-316414

gcloud container clusters get-credentials gke-deployments-cluster --region us-central1 --project sdypp-316414
