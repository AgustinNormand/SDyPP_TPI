<h1 align="center">Trabajo Práctico Integrador </h1>
<h2 align="center">Sistemas Distribuidos y Programación Paralela</h2>

<p align="center">
<img src="https://www.universidades.com.ar/logos/original/logo-universidad-nacional-de-lujan.png" alt="UNLu">
</p>

### Introducción

Desarrollo de un framework de HPC basado en un modelo SWJ (Splitter-Worker-Joiner) para la resolución de tareas genéricas,
de naturaleza distribuida haciendo uso de recursos de soporte tales como RabbitMQ y Redis.  


### Arquitectura de la solución

Poner grafico de la arqui


#### Componentes 

Contar qué es cada cosa y que sirva de intro para los conceptos


### Paso a paso

En esta sección se linkea a los readme del propio repo. 

1. Terraform/README.md
2. ArgoCD/README.md
3. Docker/README.md 

- La documentación para el que quiere implementarlo + explicación de cómo funciona 
    - Levantar local (sin ci/cd)
    - Hacer los pasos idénticos a como lo usamos nosotros


- La documentación para el usuario final: esta sería cómo usar el FW

### Pipeline CICD

El repositorio cuenta con un pipeline CICD que realiza Continuous Integration y Continuous Deployment cuando se modifica el código o algun Dockerfile dentro de el directorio Docker/ aplicando los cambios realizados en el cluster de Kubernetes de management.

El componente de CI, Continuous Integration o Integración Contínua se realiza a traves de Github Actions.
El componente de CD, Continuous Delivery o Entrega Continua se realiza a traves de ArgoCD.

![GraficoCICD](Imagenes/ideas-final-sdypp-Github-Actions.png)


1. Preparación - Setup Job
En este caso, Github corre un ubuntu-latest en el cual realizará los pasos declarados en .github/workflows/receptionist.yaml
2. Checkout código
Clona o realiza un pull del repositorio completo, para poder armar la imagen docker con los ultimos cambios
3. Setup Java JDK
Instala o disponibiliza Java JDK para permitir compilar código Java
4. Build con Maven
Empaqueta el código en un archivo .jar, y lo guarda en Receptionist/target/
5. Mover el jar al contexto del Dockerfile
Mueve el archivo .jar de Receptionist/target/ a Docker/Receptionist/
6. Logeaerse a DockerHub
Se autentica con DockerHub utilizando un nombre de usuario y un token, para permitir subir la imagen luego de armarla.
7. Crear archivo de credentiales
Arma un archivo de credenciales cuyo contenido es un *secret* del repostorio, requerido por el código para autenticarse en el cluster de Deployments.
8. Build y Push de imagen docker
Arma la imagen utilizando el Dockerfile de Docker/Receptionist/Dockerfile y una vez construida realiza un push de la imagen a dockerHub.
9. Setup Kustomize
Instala o disponibiliza Kustomize para modificar la imagen de un archivo .yaml de Kubernetes de manera simple.
10. Actualizar recursos de Kubernetes
Actualiza el tag de la imagen docker creada en pasos anteriores, en un archivo de variables de Kustomize.
11. Kustomize Build
Aplica el archivo de variables de Kustomize sobre un template, generando el archivo de salida *01-receptionist-worker-deployment.yaml*, que será colocado en el directorio Kubernetes/. Cabe destacar que dicho archivo contendrá el tag de la imagen actualizado.
12. Agregar cambios, realizar commit y push.
Se ejecutan los comandos:
git add /Kubernetes/.
git commit -m "Commit from GitHub Actions (Publisher)"
git push origin main

Lo que provoca que ArgoCD detecte un cambio los archivos .yaml de Kubernetes y los aplique en el cluster. Completando el pipeline CICD.

