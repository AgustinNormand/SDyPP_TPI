# Creación de imagenes Docker

Dado que el *Receptionist* aplica archivos .yaml en el cluster de *Deployments*, esta app necesita tener *kubectl* instalado, y tener las credenciales válidas del cluster de *Deployments*.

La resolución de dichos objetivos es la que se documentará en este README.

Cabe destacar que si usted establece en el repositorio el pipeline CICD que se encuentra documentado en el README de la raiz del proyecto, no es necesario realizar todos los pasos documentados en este archivo, ya que, gran parte de estos pasos, se realizarán de forma automatizada.

# Kubectl

Para lograr que la aplicación tenga disponible *kubectl*, se agregó a la imagen base que la app utilizaba (openjdk) el binario de kubectl. 
Esto se puede ver en el archivo /Openjdk/Dockerfile

# Credenciales

En el entorno de desarrollo tal vez es cómodo autenticarse al cluster con *gcloud* como recomienda Google. 

Pero nos enfrentamos al inconveniente de que para agregar gcloud a la imagen base tal como hicimos con *kubectl*, teníamos muchos inconvenientes. 

Es por esto que resolvimos el problema, evitando la autenticación con *gcloud* dentro de los contenedores. 

Unicamente es necesario estar autenticado con gcloud en la maquina en la cual se desarrolla y generar el archivo de credenciales que usarán los conenedores.

# Explicación de la generación del archivo kubeconfig.yaml
Leer el readme ubicado en Docker/Receptionist/README.md

# Explicación de la generación del archivo .jar
1. Usando una terminal, posicionarse en /Receptionist
2. mvn clean package

# Paso a paso para la creación de las imagenes

* Armar la imagen base
1. En una terminal, posicionarse en Openjdk/
2. Ejecutar el comando docker build -t TAG_IMAGEN_BASE:VERSION .

* Armar la imagen de la app
1. En una terminal, posicionarse en Receptionist/
2. Verificar que los archivos Receptionist.jar, kubeconfig.yaml, credentials.json
3. Ejecutar el comando docker builld -t TAG_IMAGEN_APP:VERSION .

* Pushear las imagenes a dockerhub
1. docker login
2. docker push TAG_IMAGEN_BASE:VERSION


# Pasos para que funcione el pipeline CICD
Unicamente debería crear la imagen base, con openjdk y kubectl, pushearla a dockerhub y referencairla correctamente dentro del FROM del Receptionist/Dockerfile
