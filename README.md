# Laboratorio 4: REST APIs

HTTP, consumo desde Java, protocolos y diseño de una API con OpenAPI. Ingeniería de Software II (2016702), Universidad Nacional de Colombia, 2026-II.

## Qué hay en el repositorio

| Ruta | Qué es |
| --- | --- |
| [docs/informe.pdf](docs/informe.pdf) | El informe |
| [postman/Lab4-00.postman_collection.json](postman/Lab4-00.postman_collection.json) | La colección de los ejercicios 0 a 3, con sus pruebas |
| [postman/Lab4-00.postman_environment.json](postman/Lab4-00.postman_environment.json) | El environment de esa colección |
| [postman/musubi-contenidos.postman_collection.json](postman/musubi-contenidos.postman_collection.json) | La colección generada desde `openapi.yaml`, base del Mock Server (ejercicio 4) |
| `postman/scripts-ejercicio*.js` | Las pruebas de cada petición, para leerlas sin abrir Postman |
| [cliente-java/](cliente-java/) | El cliente REST del ejercicio 2 |
| [openapi.yaml](openapi.yaml) | La especificación de la API de contenidos de Musubi (ejercicio 4) |

## Colección de Postman

Importe la colección y el environment (Import), seleccione el environment `Lab4-00` y ejecute la colección en el Collection Runner.

Antes, abra https://webhook.site, copie su URL única y péguela en la variable `webhookUrl`. Va vacía porque cada URL de webhook.site es un buzón público y vence a los siete días. Sin ella, falla la petición `3d - POST webhook pago.aprobado`.

Las variables `nuevoId`, `lat`, `lon` y `bytesRest` también van vacías: las llenan las pruebas durante la ejecución, así que las carpetas se ejecutan en orden.

## Cliente Java (ejercicio 2)

Requiere Java 17 o superior y Maven. Recibe el nombre del país en inglés:

```bash
cd cliente-java
mvn -q compile exec:java -Dexec.args="Colombia"
```

## Informe

La fuente está en [latex/main.tex](latex/main.tex) y se compila con LuaLaTeX. El PDF queda en [docs/informe.pdf](docs/informe.pdf):

```bash
cd latex
latexmk
```

Las capturas de Postman van en `latex/capturas/`. Mientras falta una, el PDF muestra un recuadro con su nombre. latexmk no detecta que llegó una captura nueva, así que en ese caso hay que forzar la compilación con `latexmk -g`.
