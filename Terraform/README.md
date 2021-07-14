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

Una vez ejecutados estos pasos, tendremos corriendo el clúster de *management* y el de *deploy*. [Ampliar sobre terraform y lo que permite???]

## Acceso al cluster creado
1. Instalar [gcloud](https://cloud.google.com/sdk/docs/install). 
2. Logearse en gcloud: `gcloud auth login`.
3. Crear el archivo kube config del cluster de *management* ejecutando el siguiente comando
   ```gcloud container clusters get-credentials $(terraform output -raw kubernetes_cluster_name) --region $(terraform output -raw region)```
   dentro del directorio `Terraform/ManagementCluster/`. Luego,guardar una copia del archivo de configuración del cluster
   ejecutando `cp $HOME/.kube/config config.management`.
4. Volver a ejecutar el comando del punto 3. esta vez en el directorio `Terraform/DeployCluster/`, guardando el archivo 
   de configuración mediante el comando `cp $HOME/.kube/config config.deployment`
   
Una vez ejecutados estos pasos, tendremos las credenciales de cada cluster para poder cambiar de contexto según las necesidades.

## Referencias

[Provision a GKE Cluster learn guide](https://learn.hashicorp.com/terraform/kubernetes/provision-gke-cluster)

[Terraform container cluster](https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/container_cluster)
