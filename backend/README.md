# Antes de empezar

Es necesario tener Maven (3.8 o superior) y Docker (22.0 o superior) instalados para ejecutar el backend.

# INSTRUCCIONES

El backend se puede ejecutar en 2 entornos: desarrollo (_development_) y testing (_testing_).
Si se usa IntelliJ para programar, a la izquierda de todo aparece una pestaña llamada _Maven_.
Dentro de la sección _Profiles_ se marca el entorno que se quiere usar (por defecto: _development_).

## Arrancar en modo desarrollo:

```mvn spring-boot:run```

Se encarga de descargar mediante Docker una imagen de Postgres.
Crea una base de datos llamada `development` en el puerto 5430.

## Ejecutar los tests

```mvn verify -P testing```

Se ejecutan los tests en el entorno de _testing_.
Usa una base de datos llamada `testing` en el puerto 5431 y que solo está encendida mientras se ejecutan los tests.
Se apagan las bases de datos de tests que estén encendidas, para evitar conflictos.
se vuelven a arrancar (con todos sus datos borrados para ejecutar los tests limpiamente),
se ejecutan los tests y se apaga la base de datos de tests.

## Liberar los recursos

```mvn clean```
Se encarga de apagar todas las bases de datos en funcionamiento,
así como de borrar el código compilado (carpeta /target).

## Cosas a evitar

```mvn test```

Es recomendable no ejecutar este comando, pero en caso de hacerlo, ejecutar ```mvn clean``` al acabar.

Si se ejecuta en modo _development_ ```mvn test -P development``` (por defecto),
no ejecuta los tests porque este modo no está configurado para ello.
Sin embargo, sí que arrancaría la base de datos _development_ y podría dar problemas.

Si se ejecuta en modo _testing_ ```mvn test -P testing```, enciende la base de datos
_testing_ y ejecuta los tests, pero no se llega a apagar la base de datos y podría dar problemas.

