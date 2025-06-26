# Prácticas de Sistemas Distribuidos

Este repositorio contiene las soluciones para las prácticas de la asignatura de Sistemas Distribuidos. A continuación, se detalla la estructura del proyecto y las instrucciones para compilar y ejecutar cada práctica.

**Enlace al Repositorio de GitHub:**
[https://github.com/ATL1GOD/Practicas_Distribuidos.git](https://github.com/ATL1GOD/Practicas_Distribuidos.git)

---

### **Consideraciones Generales Importantes:**

- **Conector JDBC de MySQL:** Las prácticas que requieren conexión a una base de datos local (Prácticas 5) necesitan el **conector JDBC de MySQL** en este caso el conector `mysql-connector-j-9.2.0.jar`. Asegúrese de que el archivo `.jar` esté en la ruta correcta o incluido en el `classpath` al compilar y ejecutar, de igual manera aún así se incluye dentro de cada carpeta necesaria.
- **Terminales Separadas:** Para las prácticas de cliente-servidor (2, 3, 4 y 5), necesitara abrir varias terminales (como PowerShell o CMD en Windows) para ejecutar los componentes del servidor y del cliente de forma simultánea.
- **Compilación Previa:** Antes de ejecutar los archivos Java, es fundamental compilarlos primero. Los comandos proporcionados a continuación incluyen los pasos de compilación necesarios para cada una de las prácticas.
- **Errores `ClassNotFoundException`:** Si se encuentra con algun error como `Error: Could not find or load main class`, asegúrese de estar ejecutando el comando `java` desde el directorio raíz de las clases compiladas y de usar el nombre de la clase sin la extensión `.java`, debido a que Java no reconoce la extensión `.java` al ejecutar el programa.
- **Instalaciones requeridas :** -`Node.js y Maven` Las prácticas 6, 7 y 8 junto con los proyectos desplegados en la nube, requieren tener instalados Node.js y Apache Maven, respectivamente para compilar y ejecutar las aplicaciones correctamente.
  - `Postman` Para probar los servicios web RESTful de las prácticas 6 y 8 junto con los proyectos desplegados en la nube, se recomienda utilizar Postman para enviar peticiones HTTP y verificar las respuestas del servidor.
- **Base de Datos MySQL:** Para las prácticas que requieren una base de datos, asegúrese de tener MySQL instalado y en ejecución. Además, debe crear la base de datos `cineboletos` y las tablas `peliculas` `compras` `ventas` utilizando el script SQL proporcionado en cada práctica.
- **Extensiones de Visual Studio Code:** Para las prácticas 6, 7 y 8, asi como para los proyectos desplegados en la nube es recomendable que tenga instaladas las siguientes extensiones en Visual Studio Code para una mejor experiencia de desarrollo y para visualizar los proyectos de manera local de mejor manera:
  - `Live Server`: Permite ejecutar un servidor local para aplicaciones web.
  - `Java Extension Pack`: Incluye herramientas esenciales para el desarrollo en Java.
  - `Spring Boot Extension Pack`: Facilita el trabajo con aplicaciones Spring Boot.
  - `GitHub Actions`: Permite integrar y gestionar acciones de GitHub directamente desde Visual Studio Code.

---

### **Práctica 1_Procesos e Hilos**

**Descripción:** Esta práctica simula un cine donde múltiples clientes (hilos) intentan comprar boletos de forma concurrente de un stock compartido, demostrando el uso de `synchronized` para evitar condiciones de carrera.

**Ubicación de los archivos:** `Practica 1_Procesos e Hilos/`

**Pasos para la Ejecución:**

1.  Abra la terminal y navega al directorio de la práctica:
    ```bash
    cd "Practica 1_Procesos e Hilos"
    ```
2.  **Compile la clase Java:**
    ```bash
    javac SistemaReservasCine.java
    ```
3.  **Ejecute la simulación:**
    ```bash
    java SistemaReservasCine
    ```

---

### **Práctica 2_Cliente Servidor**

**Descripción:** Implementa un modelo cliente-servidor básico para un cine. El servidor gestiona el stock de boletos y atiende las peticiones de un cliente a la vez.

**Ubicación de los archivos:** `Practica 2_Cliente Servidor/`

**Pasos para la Ejecución:**

1.  Abra dos terminales y navega en ambas al directorio `Practica 2_Cliente Servidor/`.
2.  **Compile las clases:**
    ```bash
    javac ServidorCine.java ClienteCine.java
    ```
3.  **En la primera terminal (Servidor):** Inicie el servidor del cine. Se quedará esperando conexiones de clientes.
    ```bash
    java ServidorCine
    ```
4.  **En la segunda terminal (Cliente):** Ejecute el cliente para conectar al servidor.
    ```bash
    java ClienteCine
    ```

---

### **Práctica 3_Cliente Servidor (Multiclientes-Servidor)**

**Descripción:** Esta práctica implementa un servidor de Cine capaz de manejar múltiples clientes de forma concurrente utilizando hilos, demostrando la capacidad del servidor para atender varias peticiones simultáneamente. El servidor gestiona el stock de boletos y permite a los clientes realizar reservas de forma independiente.

**Ubicación de los archivos:** `Practica 3_Cliente Servidor (Multiclientes-Servidor)/`

**Pasos para la Ejecución:**

1.  Abra su terminal y navegue al directorio `Practica 3_Cliente Servidor (Multiclientes-Servidor)/`.
2.  **Compile las clases:**
    ```bash
    javac ServidorCine.java MultiClienteCine.java
    ```
3.  **En la Terminal (Servidor):**
    ```bash
    java ServidorCine
    ```
4.  **Abra otras Terminales (Clientes):** Puede abrir varias terminales para simular múltiples clientes, también puede ejecutar el cliente varias veces en la misma terminal.
    ```bash
    java MultiClienteCine
    ```

---

### **Práctica 4_Cliente Servidor (Multiclientes-Multiservidor)**

**Descripción:** Esta práctica implementa una arquitectura distribuida avanzada que simula la operación de un cine con múltiples puntos de venta. Para ello, se emplea un "Balaceador de carga" que escucha en un puerto específico y redirige las peticiones de los clientes a uno de los servidores de Cine disponibles. Imagine que en lugar de tener una sola taquilla para un cine enorme (un solo servidor), abre varias taquillas idénticas (múltiples servidores o "sucursales"). Para que los clientes no se amontonen en una sola taquilla mientras las otras están vacías, ponemos a un "recepcionista" en la entrada (el Balanceador de Carga) cuyo único trabajo es dirigir a cada cliente que llega a la taquilla que esté más libre en ese momento.

**Ubicación de los archivos:** `Practica 4_Cliente Servidor (Multiclientes-Multiservidor)/`

**Pasos para la Ejecución:**

1.  Abra tres o más terminales y navegue en todas al directorio `Práctica 4_Cliente Servidor (Multiclientes-Multiservidor)/`.
2.  **Compile todas las clases:**
    ```bash
    javac ServidorCine.java MultiClienteCine.java BalanceadorCarga.java
    ```
3.  **En la Terminal 1 (BalanceadorCarga):**
    ```bash
    java BalanceadorCarga
    ```
4.  **En la Terminal 2 (Servidor de sucursal Cine):** Puede repetir este paso en más terminales con puertos diferentes para asi crear diversas sucursales de Cine.
    ```bash
    java ServidorCine
    ```
5.  **En la Terminal 3 (Cliente):** Puede abrir varias terminales para simular múltiples clientes, también puede ejecutar el cliente varias veces en la misma terminal.
    ```bash
    java MultiClienteCine
    ```

---

### **Práctica 5_Objetos Distribuidos**

**Descripción:** Esta práctica utiliza Java RMI para la comunicación distribuida, permitiendo a los clientes invocar métodos en un objeto remoto (la panadería) que reside en el servidor. La gestión del stock también utiliza MySQL.

**Ubicación de los archivos:** `Practica 5_Objetos Distribuidos/`

**Requisitos:**

- Tener **MySQL** en ejecución y la base de datos inicializada.
- Asegurese de tener `mysql-connector-j-9.2.0.jar`, de igual manera ya esta presente en esta carpeta.
- **Importante:** Se cambió la visibilidad del constructor `CineServidorRMI` de `protected` a `public` para que pudiera ser instanciado por `ServidorRMI`.

**Pasos para la Ejecución:**

1.  Abra dos terminales y navegue en ambas al directorio `Practica 5_Objetos Distribuidos/`.
2.  **Compile todas las clases:**
    ```bash
    javac -cp ".;mysql-connector-j-9.2.0.jar" CineRMI.java CineServidorRMI.java ServidorRMI.java ClienteRMI.java
    ```
3.  **En la Terminal 1 (Servidor RMI):**
    ```bash
    java -cp ".;mysql-connector-j-9.2.0.jar" ServidorRMI
    ```
4.  **En la Terminal 2 (Cliente RMI):**
    ```bash
    java -cp ".;mysql-connector-j-9.2.0.jar" ClienteRMI
    ```

### **Práctica 6_Servicios Web**

**Descripción:** Esta práctica implementa un servicio web RESTful utilizando Spring Boot. El backend gestiona el stock de boletos del cine a través de una API REST que es consumida por un frontend web con HTML, CSS y JavaScript.

**Ubicación de los archivos:** `Practica 6_Servicios Web/`

**Requisitos:**

- Tener **Apache Maven** instalado.
- Tener la extensión **Live Server** instalada en Visual Studio Code para una mejor experiencia de desarrollo y para poder visualizar el frontend en HTML en el navegador de manera mas sencilla.
- Tener **Postman** instalado para probar el servicio web RESTful.

**Pasos para la Ejecución:**

1.  Abra una terminal y navegue al directorio `Practica 6_Servicios Web/ServicioWebCine/`.
2.  **Ejecute la aplicación Spring Boot:**
    ```bash
    mvn spring-boot:run
    ```
3.  **Acceda al servicio:** Para visualizar el servicio web desde el navegador mediante el HTML tiene que activar el servidor de Live Server y este abrirá una nueva pestaña en su navegador la URL por defecto de Live Server es `http://127.0.0.1:5500` con ella podra interactuar con la interfaz web.
    Si no tiene Live Server instalado, puede abrir el archivo HTML directamente desde su sistema de archivos.
4.  **Pruebe el servicio web:** Puede probar el servicio web utilizando la herramiento Postman para enviar peticiones HTTP a la API REST sin necesidad de abrir el frontend desde el HTML.

---

### **Práctica 7_Microservicios**

**Descripción:** Esta práctica descompone la aplicación del Cine en una arquitectura de microservicios. Incluye servicios para boletos, compras, un servidor de descubrimiento (Eureka) y un API Gateway.

**Ubicación de los archivos:** `Practica 7_Microservicios/`

**Requisitos:**

- Tener **Apache Maven** instalado.
- Tener **Postman** instalado para probar el servicio web RESTful.

**Pasos para la Ejecución:**

1.  Abra una terminal y navegue al directorio `Practica 7_Microservicios/Microservicios-Java/`.
2.  **Acceder a los servicios de manera dependiente**
    Abra una terminal por cada microservicio y navegue a su respectivo directorio.
    `../servicioboletos/`, `../serviciocompras/`, `../eurekaserver/`, `../apigateway/`, `../Cliente/`

`3. **Ejecute cada uno de los servicios:**
`bash
    mvn spring-boot:run
    `

---

### **Práctica 8_Progressive Web App (PWA)**

**Descripción:** Esta práctica convierte el frontend del Cine en una Progressive Web App (PWA). La aplicación es instalable, funciona sin conexión gracias a un Service Worker y recibe actualizaciones de stock en tiempo real mediante Server-Sent Events (SSE).

**Ubicación de los archivos:** `Practica 8_Progressive Web App (PWA)/`

**Requisitos:**

- Tener **MySQL** en ejecución y la base de datos `panaderia` inicializada.
- Tener Apache Maven instalado.

**Pasos para la Ejecución:**

1.  Abra una terminal y navegue al directorio `Practica 8_Progressive Web App (PWA)/Cine-PWA/`.

2.  **Ejecute el servidor con Node.js:**
    ```bash
    npm install
    npm start
    ```
3.  **Acceda a la PWA:** Abra su navegador y vaya a `http://localhost:3000`. Podra instalar la aplicación desde la barra de direcciones.

### **Implementaciones en la Nube**

A continuación se presentan los enlaces a las aplicaciones desplegadas en diferentes proveedores de nube.

- **Servicio Web (Panadería):**

  - **Azure:** [https://panaderia-app-bhafafb5hja4axew.canadacentral-01.azurewebsites.net/](https://panaderia-app-bhafafb5hja4axew.canadacentral-01.azurewebsites.net/)

- **Microservicio PWA (Cine - Proyecto en equipo):**

  - Este proyecto tiene como Backend una arquitectura de microservicios y en la parte del Frontend un PWA.Al ser también una Aplicación Web Progresiva (PWA), puede ser instalado en dispositivos compatibles para una experiencia similar a una app nativa.

  - **Google Cloud:** [https://storage.googleapis.com/cine-pwa/index.html](https://storage.googleapis.com/cine-pwa/index.html)
  - **Amazon Web Services (AWS):** [https://cine-pwa.s3.us-east-2.amazonaws.com/index.html](https://cine-pwa.s3.us-east-2.amazonaws.com/index.html)
  - **Microsoft Azure:** [https://witty-rock-0406c590f.1.azurestaticapps.net/](https://witty-rock-0406c590f.1.azurestaticapps.net/)

---
