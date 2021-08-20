<h1 align="center">Trabajo Práctico Integrador </h1>
<h2 align="center">Sistemas Distribuidos y Programación Paralela</h2>

<p align="center">
<img src="https://www.universidades.com.ar/logos/original/logo-universidad-nacional-de-lujan.png" alt="UNLu">
</p>


## Introducción

En el contexto de la asignatura Sistemas Distribuidos y Programación Paralela de la Universidad Nacional de Luján, se propone y documenta la creación de una herramienta de cómputo intensivo basado en un modelo SWJ (Splitter-Worker-Joiner) para la resolución de tareas genéricas, apoyado en la plataforma de gestión de contenedores Kubernetes y tecnologías de Cloud Computing. Todos los componentes utilizados (propios y de terceros) presentan características de alta disponibilidad y tolerancia a fallos. 

La herramienta está orientada, por el momento, al uso exclusivo de los autores del trabajo. Sin embargo, se toman algunas consideraciones para soportar aspectos tales como cargas de tráfico mayores resultantes de publicar la aplicación. En la sección de Alcance se presentan los límites del mismo.

La motivación principal del trabajo es la necesidad ocasional de ejecutar tareas que se verían beneficiadas de una gran capacidad de cómputo para la obtención de resultados. Dado que el costo de contar con una infraestructura disponible 24/7 que permita satisfacer los requerimientos eventuales implicaría un costo excesivo y un gran desperdicio la mayor parte del tiempo, la propuesta considera desde la creación hasta la destrucción de los recursos necesarios de manera automatizada, tan solo configurando unas pocas variables dependientes. 

### Alcance

Concretamente, el presente trabajo aporta:
- Los scripts necesarios para la creación de la infraestructura en Google Cloud Platform mediante Terraform. 
- La documentación de los pasos previos requeridos para poder crearla.
- La configuración y puesta en funcionamiento de un pipeline de CI/CD para la herramienta en sí misma y los recursos auxiliares.
- Los manifiestos de Kubernetes que serán desplegados por el componente de CD (Continuous Delivery) y las imágenes de Docker para cada uno de los componentes de la herramienta, independientes de la tecnología subyacente. 
- Una herramienta de gestión denominada Management App, que permite ejecutar tareas genéricas con alta capacidad de cómputo de forma transparente al usuario.
- La configuración necesaria para lograr el auto-escalado de los componentes de apoyo e involucrados en la tarea del usuario, como así también la replicación de los mismos - cuando se requiera - para lograr tolerancia a fallos. 
- La documentación de cada uno de los recursos utilizados y su función en el proyecto, así como ventajas y desventajas identificadas por su uso. 
- La implementación de la herramienta Prometheus para el monitoreo de los servicios, y Fluentd para la recolección de logs, junto al stack ELK para identificar errores y fallas en la aplicación y sus dependencias, utilizando tableros de visualización y alertas. 


### Out-of-scope

Algunas cuestiones no consideradas por el trabajo hasta el momento, que presentan oportunidades de extensión a futuro, son:
- La publicación de la herramienta para uso de terceros, y como consecuencia:
    - La prevención de que individuos maliciosos la utilicen para ejecutar ataques.
    - Un esquema de medición y costo frente al uso de la herramienta.
    - Implementación de mecanismos de seguridad para evitar ataques a la propia infraestructura. 
- La posibilidad de entregar imágenes Dockerizadas o incluso, código fuente, en lugar de manifiestos de Kubernetes. 
- Desplegar Kubernetes obviando el servicio administrado provisto por GCP. Más información [aquí](https://github.com/kelseyhightower/kubernetes-the-hard-way).

## Management App

Es una aplicación orientada a microservicios encargada de procesar las tareas que el usuario final desea ejecutar en la plataforma HPC. Está diseñada con el propósito de escalar cuando se requiera, por lo que los componentes son independientes entre sí, comunicados a través de colas de mensajería, compartiendo un *storage* en común y utilizando una caché para el almacenamiento de estado de las tareas. 

![Grafico-Management-App](Imagenes/ideas-final-sdypp-Management-app.jpg)


## Pipeline CICD

La Management App cuenta con un pipeline de CI (Continuous Integration o Integración Continua) que se activa al modificar el código fuente del proyecto o algún Dockerfile dentro del directorio `Docker/`. Esta funcionalidad está implementada a través de Github Actions, reaccionando ante commits del usuario a los paths correspondientes del repositorio.

A su vez, frente a la modificación en los manifiestos de las carpetas `Kubernetes/Management` y `Kubernetes/Resources` - sea manual (por el usuario) o automática (llevada a cabo por Github Actions en el pipeline de CI) - se desencadena el circuito de CD (Continuous Deployment o Entrega continua) implementado a través de ArgoCD, quien se encargará de determinar las diferencias en los manifiestos del clúster correspondiente, aplicando estos cambios y reflejando el estado correcto. 

En el gráfico a continuación, se detallan los pasos y la relación entre CI/CD para la Management app.

![GraficoCICD](Imagenes/ideas-final-sdypp-Github-Actions.png)


### Etapas del pipeline

A continuación, una breve explicación de cada uno de los pasos involucrados en el pipeline de la *Management app*. Dado que la aplicación está compuesta de varios microservicios, debemos considerar que el flujo será idéntico para cada uno de ellos, a excepción de las rutas de los directorios involucrados.

1. **Preparación - Setup Job**: En este caso, Github corre un ubuntu-latest en el cual realizará los pasos declarados en cada uno de los *workflows* de la carpeta `github/workflows/`.
2. **Checkout código**: Clona el repositorio completo, para poder armar la imagen Docker con los últimos cambios.
3. **Setup Java JDK**: Instala y disponibiliza Java JDK para poder compilar el código Java de cada una de los componentes.
4. **Build con Maven**: Empaqueta el código en un archivo .jar, y lo guarda en el directorio del componente, bajo la ruta `/target`.
5. **Mover el jar al contexto del Dockerfile**: Mueve el archivo .jar creado en el paso anterior al directorio `Docker/` del proyecto, en una subcarpeta correspondiente al componente afectado.
6. **Loguearse a DockerHub**: Se autentica con DockerHub utilizando un nombre de usuario y un token, para permitir subir la imagen luego de armarla.
7. **Crear archivo de credentiales**: Arma un archivo de credenciales cuyo contenido es un *secret* del repostorio, requerido por el código para autenticarse en el clúster de Deployments y acceder al *Storage* compartido.
8. **Build y Push de imagen docker**: Se construye la imagen de Docker utilizando el Dockerfile del componente bajo el subdirectorio correspondiente en la carpeta `Docker/`. Luego, se realiza un push de la imagen a dockerHub.
9. **Setup Kustomize**: Instala y disponibiliza Kustomize, que permitirá modificar la imagen de un manifiesto de Kubernetes de forma sencilla.
10. **Actualizar recursos de Kubernetes**: Actualiza el tag de la imagen docker creada en el paso 8, en un archivo de variables de Kustomize.
11. **Kustomize Build**: Aplica el archivo de variables de Kustomize sobre un template, generando el archivo .yaml de salida para el componente afectado, que será colocado en el directorio `Kubernetes/` bajo el subdirectorio correspondiente. Cabe destacar que este archivo contendrá el tag de la imagen actualizado.
12. **Agregar cambios, realizar commit y push**: Se ejecutan los comandos de *git* necesarios para publicar los cambios en el repositorio, lo que provoca que ArgoCD detecte una modificación de los archivos .yaml de Kubernetes y los aplique en el clúster, completando así el pipeline CICD.

**Importante**: En el caso de los componentes del clúster de *Resources* representados por los manifiestos de la carpeta `Kubernetes/Resources`, el circuito de CI no es necesario dado que no se trabaja con el código fuente. Sin embargo, se aprovechan las bondades de ArgoCD para su despliegue en el clúster.


### Entrypoint 

Es un servicio desarrollado en Java que expone endpoints HTTP desde los cuales recibirá las solicitudes de procesamiento de los usuarios. Cada endpoint espera recibir contenido distinto dependiendo del formato que utilice el usuario para informar la tarea a ejecutar. En el caso de solicitar la ejecución de la tarea mediante manifiestos de Kubernetes, se deberá enviar un `POST /yaml` entregando como cuerpo de la solicitud un conjunto de archivos en formato YAML a aplicar en el clúster. 

![Grafico-Management-App-Entrypoint](Imagenes/ideas-final-sdypp-Entrypoint.jpg)

Una vez recibida la tarea, el componente genera un identificador que vincule los archivos subidos y los deposita en un almacenamiento compartido con el resto de los microservicios. A su vez, emite un mensaje a un *exchange* de RabbitMQ para notificar a los componentes interesados que existe una tarea en formato YAML pendiente de validación.


### YAML Manager

Frente a la notificación emitida por el Entrypoint, el YAML Manager accederá al almacenamiento compartido para descargar los archivos bajo el identificador recibido y llevará a cabo un circuito de validación. Dependiendo del estado de los manifiestos, procederá a depositarlos nuevamente en el mismo *storage* con las modificaciones pertinentes sobre los archivos originales. A su vez, generará una notificación que será enviada a RabbitMQ para avisar al resto de los componentes sobre el estado de la validación. 


### Cluster Applier

Una vez validados los YAML del usuario, la Management App se encuentra en condiciones de aplicar los manifiestos sobre el clúster de *Deployment*. Esta es la tarea del Cluster Applier, que al recibir la notificación del YAML Manager indicando la validación exitosa, accederá al *storage* compartido para descargar los archivos y aplicarlos. 


![Grafico-Management-App-Cluster-Applier](Imagenes/ideas-final-sdypp-Cluster-Applier.jpg)

Para poder interactuar con el clúster, el Cluster Applier utiliza la herramienta de linea de comandos de Kubernetes: `kubectl`. Para ello, necesita tenerla instalada en el entorno de ejecución; por eso, en `Docker/Cluster-Applier/Dockerfile` podemos observar el uso de una imagen base de OpenJDK propia y modificada, cuyo contenido puede verse reflejado en `Docker/Openjdk/Dockerfile`.

### Status Worker

Se trata de un componente auxiliar que da seguimiento al estado del job del usuario. A partir del evento de "tarea pendiente" publicado por el Entrypoint, el Status Worker almacena en Redis el identificador de la tarea junto con un estado PENDING. Una vez validados los YAMLs del usuario y publicado el evento por el YAML Manager, se procede a la actualización del estado a APPLYING_IN_CLUSTER. Cuando el Cluster Applier finaliza la aplicación de los manifiestos y emite el evento correspondiente, este componente actualiza el estado a DONE. Eventualmente, el usuario solicitará la eliminación de la tarea, por lo que se realizará una vuelta atrás de los recursos en el clúster. Esto dispara la notificación correspondiente, resultando en una actualización del estado a ROLLED_BACK.

Gracias a la arquitectura desacoplada de la solución, este componente se suscribe a los mismos *exchanges* utilizados por el resto de los microservicios, sin necesidad de hacer explícito el envío de los mensajes.

El servicio expone un endpoint HTTP desde el cual puede consultarse el estado de la tarea a través del identificador generado para la misma. 

## Infraestructura

Tanto la Management App como los recursos auxiliares y - principalmente - las tareas del usuario, son gestionados mediante la plataforma de contenedores Kubernetes. A continuación, se brinda mayor detalle con respecto a la distribución de los recursos, proveedor de cómputo en la nube utilizado, interrelación entre los servicios necesarios, entre otros.

![GraficoArquitectura](Imagenes/ideas-final-sdypp-Arquitectura.png)

La arquitectura de la aplicación consta de 3 clústers: *deployments*, *resources* y *management*. Todos ellos poseen *nodos privados*, es decir, que no tienen direcciones IP públicas. Esta decisión se ve justificada por el costo y porque en el presente resultan dispensables.

Cada clúster tiene configurada una *VPC* o *Virtual Private Cloud*, donde se alojan los *nodos*, *pods* y *servicios* de cada uno.

Para lograr que los nodos tengan acceso a Internet, fue necesario configurar un *Cloud Router* en cada una de las *VPCs* de los clústers, que realice un enmascaramiento de la dirección IP privada por una pública. Esto se logró utilizando *Cloud NAT*.

Las instancias utilizadas son de tipo *preemptible*, equivalentes a las conocidas como *instancias spot* en AWS, ya que tienen un costo reducido. Como contrapartida, solo duran 24 horas como máximo lo cual puede resultar inconveniente para ciertos casos de uso.

Los clústers de *deployments* y *resources* deben poder comunicarse entre sí, ya que las aplicaciones que se encuentren en el primero necesitan consumir los servicios que proveen los recursos alojados en el segundo. Para esto fue necesario configurar *VPC Peering*, habilitando el routeo entre las *VPCs* del clúster de *deployments* y el de *resources*.

A su vez, fue necesario agregar las respectivas reglas de *firewall* para permitir el tráfico entre las *VPCs* mencionadas anteriormente.

Todos los clústers se encuentran configurados con *cluster auto scaler* y *horizontal pod autoscaler*.

![GraficoComunicacion](Imagenes/ideas-final-sdypp-ServiciosInternos.png)

Para lograr comunicar a las aplicaciones del clúster de *deployments* con los recursos alojados en el clúster de *resources* fue necesario levantar servicios de tipo "internal", ya que - de lo contrario - los pods de las aplicaciones no podían acceder a los servicios, por más que se encuentren en *VPCs* emparejadas, con el tráfico permitido a través del firewall.

### GKE

Se utilizó Google Kubernetes Engine como servicio administrado para el despliegue de Kubernetes. Si bien existen diversos proveedores, la elección de GCP, se ve justificada por la oferta de 300USD de crédito y la certeza de que no se cobrara nada a la tarjeta introducida.

#### Separación de responsabilidades

Tal lo mencionado, se desplegaron tres clústers para mantener una separación de responsabilidades e incrementar la seguridad mediante el aislamiento total de los recursos. Si bien podrían haberse tomado otros aproximamientos para lograr un fin similar (por ejemplo, mediante el uso de *namespaces*), consideramos que desde la perspectiva de la escalibilidad resulta más conveniente la separación. Por otra parte, los fines didácticos complementan la elección.  

A nivel funcional, el clúster de *management* contiene cada uno de los microservicios que conforman la Management App, junto con los recursos que esta necesita. Este despliega las tareas del usuario en el clúster de *deployment*, por lo que no existe un contacto directo entre aplicaciones externas y la de gestión. A su vez, la plataforma provee al usuario servicios complementarios tales como RabbitMQ y Redis. Estos últimos se encuentran en un clúster adicional, denominado *resources*. 

#### Interconexión de clústers

Para sostener el aislamiento planteado, cada clúster se encuentra en una VPC distinta. Sin embargo, por motivos funcionales, los recursos del clúster de *deployment* necesitan acceder a los servicios del de *resources*. Como consecuencia, para que dos servicios en VPCs diferentes lograran comunicarse, fue necesario llevar a cabo configuraciones adicionales, detalladas a continuación. 

Por otro lado, fue deseable que los servicios levantados en el clúster de recursos pudieran ser accedidos mediante un mnemónico en lugar de su dirección IP, dado que no consideramos aceptable que esta última fuese un valor estático. Para resolver este inconveniente, fue necesario acudir a las bondades de un recurso adicional, detallado posteriormente.

##### VPC Peering, Reglas de firewall e Internal Services

En GCP, la interconexión de dos redes privadas virtuales es posible mediante el uso de la tecnología denominada *VPC Peering*. La configuración de la misma debe llevarse a cabo de forma recíproca, esto implica que - en ambas VPCs - se establezca un emparejamiento desde la red origen hacia la red destino, respectivamente. 

Además, para poder cursar el tráfico entre las VPCs vinculadas, es necesario definir las reglas de firewall correspondientes, permitiendo los paquetes entrantes hacia los puertos de Redis y RabbitMQ.

Aún con las dos configuraciones previas, no bastaba para que los servicios definidos como ClusterIP pudieran ser accedidos desde fuera del clúster, sino que fue necesario declarar a los servicios de tipo LoadBalancer y con una anotación "internal". 

```yaml
...
    type: LoadBalancer
    annotations: 
      networking.gke.io/load-balancer-type: "Internal"
...
```

De esta forma, la IP externa del LoadBalancer deja de ser una IP pública como normalmente lo es, y pasa a ser una IP privada del subrango correspondiente a la red de VPC (en lugar de la subnet de *resources*). Dicho rango es accesible por los *pods* ejecutándose en la subred de la VPC emparejada.

##### ExternalDNS y CloudDNS

Con lo configurado en la sección previa, contamos con un servicio que tiene una IP que puede ser accedida desde el clúster de *deployments* al clúster de *resources*. Bajo una configuración tradicional, donde todos los recursos se encuentran desplegados en un único clúster, los *pods* podrían alcanzar los servicios mediante un mnemónico, manejado por *kubedns*. Sin embargo, recordando la separación implementada en esta arquitectura, nos encontramos con la imposibilidad de acceder a dicha facilidad. Por consiguiente, fue necesario registrar el servicio en CloudDNS, la API de GCP que permite establecer un mnemónico que puede ser consultado desde cualquier instancia del clúster. Si bien podría haberse registrado de manera manual como una entrada estática en los registros DNS, decidimos ir un paso más allá e implementar la carga y actualización automática del registro mediante la herramienta ExternalDNS. Esta última - a todos los servicios anotados con la anotación correspondiente luego de ser creados - reemplaza el *fully qualified domain name* (FQDN) de la anotación por la dirección IP asignada al servicio. 

```yaml
...
    type: LoadBalancer
    annotations: 
      networking.gke.io/load-balancer-type: "Internal"
      external-dns.alpha.kubernetes.io/hostname: redis-service.framework.services.gcp.com.ar.
...
```


#### Clúster privado, Cloud NAT y Cloud Router

Si bien GCP ofrece cŕedito gratuito, tiene algunas limitaciones llamadas *quotas*. Una de estas consiste en el número de direcciones IP públicas que pueden utilizarse. Dado que en el proyecto contamos con más de 8 nodos, cada uno con una IP pública, nos vimos obligados a evitar que todos los nodos tengan una IP pública. GCP ofrece este tipo de instancias bajo la denominación de nodos privados, resultando en un clúster con un único punto de acceso. 

Ahora bien, debido a la falta de una dirección routeable en Internet, y el requisito de que los los nodos tengan acceso a esta última, fue necesario configurar otro servicio proporcionado por Google, denominado Cloud NAT, que requiere ser aplicado a un Cloud Router vinculado a una VPC. Cabe destacar que esta configuración fue aplicada en los tres clústers, resultando en tres Cloud Routers que realizan Cloud NAT para sus respectivas VPCs. 


#### Instancias *Preemtible*

En pos de reducir los costos y ampliar la duración de los créditos disponibles, se utilizaron instancias *preemptible*. Estas últimas, a diferencia de las denominadas *on demand*, tienen un costo tres veces inferior con la consecuencia de que duran - como máximo - 24 hrs. Sin embargo, previo a ese rango temporal, la instancia podría "caerse". Al suceder esto, Kubernetes intenta reubicar los *pods* del nodo afectado a algún otro del clúster. Probablemente, al concretarse dicha acción, algún *pod* no tenga suficientes recursos (cpu y/o memoria) en el nodo asignado. Como consecuencia, GKE instancia un nuevo nodo, ya que se encuentra habilitada la característica de *cluster autoscaler*.


#### Google Storage

La Management App utiliza un almacenamiento compartido entre los distintos microservicios. La herramienta utilizada para dar soporte a esta necesidad es Google Storage por formar parte de la suite de GCP. Además, este *storage* está disponible para los usuarios que lo requieran. 


### Terraform

Todos los recursos mencionados anteriormente podrían ser desplegados de forma manual mediante alguna interfaz de GCP. Sin embargo, optamos por la opción de Infraestructura como Código (IaaC) a través de la herramienta Terraform. Para ello se cuenta con un archivo principal, el cual hace uso de los módulos de Google necesarios para satisfacer los requerimientos de infraestructura, junto a un archivo de variables, que permite personalizar el despliegue con los parámetros del proyecto actual. 

El archivo mencionado consta de una serie de bloques denominados módulos, denotados por la palabra reservada *module* junto a un identificador y un grupo de entradas, entre las cuales debe encontrarse la propiedad "source", que indica el origen del código fuente del módulo. 


### Scripts

Dado que el proyecto fue construido con el propósito de ser utilizado desde cero sin necesidad de conocer en detalle su funcionamiento, se construyeron los scripts necesarios para crear y descartar todos los elementos involucrados, facilitando la tarea al usuario, ya que Terraform no era suficiente para cubrir por completo esta tarea. 

#### Init

Este puede encontrase en `/Terraform/init.sh`.
La funcionalidad de este script es inicializar una cuenta de Google Cloud Platform recien creada. Primero se logeará en esta cuenta, se creará el proyecto, las cuentas de servicio, se otargarán roles/permisos a estas. Se habilitarán las API necesarias, se habilitará la cuenta de facturación, se genera el archivo de credenciales.

#### Despliegue

Este puede encontrarse en `/Terraform/deploy.sh`.
Una vez cumplidas las condiciones para el despliegue, este script realizará esa tarea. Exportará la variable necesaria para ubicar el archivo de credenciales, ejecutará los comandos necesarios de Terraform para desplegar la infraestructura. Establecerá el secret de github que contiene las credenciales de la cuenta de GCP, para poder acceder, junto con el archivo kubeconfig, al cluster de Deployments. Descarga la ultima version de ArgoCD, aplica el manifiesto en todos los cluster e instala todas las aplicaciones de ArgoCD ubicadas en el directorio ArgoCD y el subdirectorio homonimo a cada cluster. Instala External-dns en el cluster de Resources y realiza un commit de los cambios realizados al repositorio. (Add/Update kubeconfig.conf, Add/Update external-dns)

#### Get Credentials

Este puede encontrarse en `/Terraform/get_credentials.sh`.
La funcionalidad de este script es establecer las credentiales de los cluster desplegados en el equipo local. Establece el proyecto actual en gcloud, crea un nuevo archivo de credenciales, lo activa, remueve las credenciales viejas de kubectl, y obtiene las nuevas.

#### Destrucción

Este puede encontrarse en `/Terraform/destroy.sh`. 
Este script elimina toda la insfraestructura, aplicaciones, configuraciones.
Si bien bastaría con "terraform destroy", existen ciertos recursos creados posteriormente al despliegue, que requieren ser eliminados previamente a ejecutar dicho comando de terraform, dado que, de lo contrario, no coincidiría el estado que terraform percibe contra el real, fallando en tiempo de ejecución.

Terraform

Firewall Roules
VPC Peering
ExternalDNS
Cloud DNS

Private clusters
Cloud Routers
Cloud NAT

Bucket

Preemtible


## Kubernetes

ArgoCD Apps
Helm Subcharts
.yamls

TRANSPARENTE A GOOGLE

## Autoscaling

Cluster Autoscaler
HPA
Prometheus
Prometheus Adapter
Recording Rules
Custom API
Ingress
Ingres Controller NGINX

## Replication

Redis Cluster Mode
Redirect, Lettuce

RabbitMQ clúster Mode
Quorum queues

## Logging/Monitoring

Fluent-bit
Logstash
Elasticsearch Cluster Mode
Kibana
Grafana
Prometheus ?
