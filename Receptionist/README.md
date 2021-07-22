## Receptionist

El objetivo es explicar cómo funciona el servicio, el flujo entre los componentes y el punta a punta desde que el cliente 
envía sus archivos.
 
### Arquitectura

![Componentes receptionist](/images/ideas-final-sdypp-Receptionist.png)

### Capas

La aplicación está dividida en 3 capas: Controladores, Servicios y Core. A continuación, se detalla cada una.

#### Controladores

Exponen los endpoints HTTP que consumen los clientes. Definen las rutas y los métodos por los cuales serán accedidos los recursos.

Hacen uso de los Servicios para acceder a las funcionalidades de la aplicación.

#### Servicios

Agregan un nivel de abstracción para alcanzar los recursos del Core. En este caso, por ejemplo, se provee acceso a una 
interfaz de archivos YAML que impactarán en el cluster.

#### Core

Contiene la lógica necesaria para el framework y la aplicación de los manifiestos, la coordinación y el control de errores.
Encapsula el acceso a los recursos y provee interfaces genéricas para su acceso. 
