# Laboratorio 4: REST APIs

HTTP, consumo desde Java, protocolos y diseño de una API con OpenAPI. Ingeniería de Software II (2016702), Universidad Nacional de Colombia, 2026-II.

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
