# Instalación de ArgoCD para Continuous Deployment 

El objetivo es instalar ArgoCD en el cluster de *management* para implementar el componente CD del pipeline CI/CD.

## Pre-requisitos
Haber levantado el clúster de *management* tal lo indicado en `Terraform/README.md`.

# Setup:
1. Establecer el contexto del cluster de *management* tal lo explicado en `Terraform/README.md`.
2. Descargar manifiesto de ArgoCD para su instalación: 
```shell   
wget https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```
3. Crear el namespace "argocd":
```shell
kubectl create namespace argocd
```
4. Instalar ArgoCD aplicando el manifiesto:
```shell
kubectl apply -n argocd -f install.yaml
```
5. Iniciar entrega continua de la aplicación:
```shell
kubectl apply -n argocd -f app.yaml
```

## Acceso a la GUI
1. Establecer túnel entre host y ArgoCD: 
```shell
kubectl port-forward svc/argocd-server -n argocd 8080:443
```
2. Loguearse en ArgoCD con usuario "admin". Para obtener la contraseña, acceder al *secret* mediante:
```shell
kubectl -n argocd get secrets argocd-initial-admin-secret -o jsonpath='{.data.password}' | base64 -d
```


## Referencias

[ArgoCD](https://argoproj.github.io/argo-cd/getting_started/)