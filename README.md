# Configuración del proyecto

## Fichero de configuración con fichero .env
En el fichero de configuración `.env` es donde tenemos que poner las variables de entorno del proyecto según el entrono en el que estemos.
Como este proyecto es para una practica de clase, subiré directamente este fichero pero lo normal es que este fichero
no esté en el proyecto y esté el `.env.example` solamente con las claves que se tienen que usar en el proyecto.

## Uso de maven para la gestión de dependencias
Las dependencias obviamente no me las iba a descargar de ninguna fuente desconodida para esto usamos un gestor de dependencias
en mi caso Maven.

### ¿Qué es Maven?
Maven es una herramienta desarrollada en Java que simplifica las tareas de compilación y construcción de aplicaciones de software, principalmente usado con lenguajes de programación que tienen como destino ser ejecutadas en la Java Virtual Machine (JVM). Además, incorpora una gestión de dependencias madura con acceso a los repositorios públicos de Maven con más de 16 Millones de librerías disponibles incluyendo todas las versiones de cada una de ellas.

### Cómo instalar Maven con SDKMan

```bash
sdk install maven 3.8.6 
```
Como usar el maven que acabamos de instalar en este proyecto

```bash
sdk use maven 3.8.6 
```

```bash
mvn -version
```

### Cómo instalar Maven en Ubuntu
De la misma manera que se ha procedido con la instalación del JDK, la herramienta de construcción y de gestión de dependencias de proyecto Java por excelencia Maven se instalará de forma equivalente:
```bash
sudo apt install maven
```

```bash
mvn -version
```

### Windows / Mac
Maven, al estar programado en Java, éste es portable, por lo que la misma distribución puede usarse en cualquier plataforma que pueda ejecutarse la JVM. Así que únicamente debemos [acceder a la página oficial del proyecto Maven](https://maven.apache.org/download.cgi) y descargar la última versión estable. Ésta se ofrece en varios formatos, aquella que tiene extensión Zip será suficiente en la mayoría de los casos.

Una vez descargada, revise la sección de su plataforma para ver cómo configurarlo correctamente.

#### Configuración de Maven en Windows
La configuración de Maven para Windows es muy sencillo, lo único que debe hacer es descomprimir el archivo Zip descargado del paso anterior y ubicando los ficheros en un directorio. Por ejemplo en el directorio donde se ha almacenado los archivos del JDK: c:\Archivos de Programa\Java\apache-maven-. Anote la ruta completa porque será necesaria en el siguiente paso.

Una vez descomprimidos los archivos se deberá crear la variable de entorno MAVEN_HOME apuntando a la ruta anterior, base de la instalación. El uso de esta variable permitirá cambiar de una distribución de Maven a otra versión más actualizada sin tener que cambiar todas las referencias a esta difundida por los proyectos.

Por último, añadiremos la ruta de los binarios de Java a la variable de entorno Path para que podamos invocar a maven con el comando mvn desde cualquier directorio:

```bash
Path=%MAVEN_HOME%\bin;%Path%
```

```bash
mvn -version
```


## Uso de SDKMan para gestionar la versión de java y el vendor
### ¿Qué es SDKMan?
SDKMAN! es una herramienta que nos permite manejar la instalación y configuración de diversas versiones de SDKs (Software Developments Kits)

En nuestro día a día como desarrolladores, nos encontramos a veces ante aplicaciones que funcionan y deben compilar en versiones muy diferentes su SDK. Por ejemplo, de Java 1.8, 11, posterior, anterior, etc. También nos ocurre lo mismo con Maven y con otros SDKs. Cada vez que queremos cambiar de versión, se hace tedioso el tener que buscar, descargar, instalar y configurar nuestro sistema operativo para que utilice esta versión recientemente instalada. Para ello, tenemos a nuestra disposición aplicaciones como SDKMAN!.

### Instalación de SDKMAN!
En su web, nos proporciona una instalación muy sencilla para Linux, Mac y Windows

La instalación en Windows nos plantea un problema inicial, y es que se realiza con un script bash. Para la instalación hemos utilizado la consola de GitBash que se nos instaló junto al Git.

```bash
curl -s "https://get.sdkman.io" | bash
```

Warning! Una vez ejecutamos este comando, me dio un error: no encuentra zip, unzip. Para solventarlo, tuve que realizar la siguiente modificación:

```bash
curl -s "https://get.sdkman.io" > sdkman
```

A continuación, editamos este fichero y comentamos el siguiente fragmento de código:

```bash
#echo "Looking for unzip..."
#if ! command -v unzip > /dev/null; then
#    echo "Not found."
#    echo "======================================================================================================"
#    echo " Please install unzip on your system using your favourite package manager."
#    echo ""
#    echo " Restart after installing unzip."
#    echo "======================================================================================================"
#    echo ""
#    exit 1
#fi

#echo "Looking for zip..."
#if ! command -v zip > /dev/null; then
#    echo "Not found."
#    echo "======================================================================================================"
#    echo " Please install zip on your system using your favourite package manager."
#    echo ""
#    echo " Restart after installing zip."
#    echo "======================================================================================================"
#    echo ""
#    exit 1
#fi
```

Una vez comentado este fragmento de código, podemos instalarlo mediante el comando:

```bash
bash sdkman
```

Ahora tenemos que instalar la versión de SDKMan que se usará en este proyecto

```bash
sdk install java 17.0.5-amzn
sdk install maven 3.8.6 
```

Ahora que tenemos instalado el software debemos usarlo para eso ejecutamos;
```bash
sdk use java 17.0.5-amzn
sdk use maven 3.8.6 
```

## Comprobar el ejercicio
Para poder probar que el ejercicio funciona he creado un test de integración que realmente es el que me ha ido guiando en la implementación.
He realizado algunas modificaciones sobre las clases que nos pedíais en el enunciado que utilizaríamos, ya qué, por el contrario, era imposible realizar ni un test de integración.

Otra manera sería lanzar primero el servidor y luego el cliente, ya que el cliente no se podría conectar en caso de que el servidor no exita.

## Conclusiones
Tengo que comentar que este ejercicio no permite que se realicen test unitarios, por el echo de que habría que crear realmente componentes que se encargasen de la conexión y esto
sería mucho más cambio.

Me he tomado la libertad de usar builder para poder hacer implementación, aun así no estoy contento, porque el ejercicio no daba mucha flexibilidad para hacerlo con buenas practicas.

PD: Este proyecto requiere que se use de Java 14 en adelante, ya que utiliza Records.

