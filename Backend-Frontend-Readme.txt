Curso: Desarrollo de Proyecto 1
Equipo: 3C


Backend - Springboot
=======================================================================================
Maven local deploy:

La instalación de Apache Maven es un proceso simple de extraer el archivo y agregar la carpeta `bin` con el comando` mvn` a la `PATH`.
Los pasos detallados son:

Tenga una instalación de JDK en su sistema. Establezca la JAVA_HOME variable de entorno que apunte a su instalación de JDK o tenga el javaejecutable en su PATH.

echo %JAVA_HOME% 
C:\Program Files\Java\jdk1.7.0_51

Extraiga el archivo de distribución en cualquier directorio

descomprimir apache - maven - 3.8 . 4 

Agregamos la ruta donde descomprimimos maven al PATH de variables de entorno y nos aseguramos de tener una versión de java 17+.

Fuente: https://maven.apache.org/install.html
Descarga: https://maven.apache.org/download.cgi


========================================================================================
Para levantar el proyecto:
Instalamos las dependencias
$ mvn install

Iniciamos el proyecto
$ mvnw spring-boot:run


Frontend - React
=======================================================================================
Para el levantamiento se requiere tener una versión actualizada de nodejs
Instalable directamente desde: https://nodejs.org/es/download/

Una vez tenemos npm podemos instalar las dependencias:
npm install

Iniciamos el proyecto
npm start


