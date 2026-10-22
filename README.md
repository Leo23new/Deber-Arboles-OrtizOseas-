# Deber-Arboles-OrtizOseas — Catálogo de la Biblioteca FISEI

**Asignatura:** Estructura de Datos · **Nivel:** Tercero, Ingeniería de Software · **Modalidad:** individual

## Problema

La Biblioteca FISEI se organiza en **Áreas** y cada Área en **Estantes**. Esa jerarquía se modela con un
**árbol general n-ario** (`ArbolBiblioteca`). Cada Estante guarda sus libros en un **Árbol Binario de
Búsqueda** (`CatalogoABB`) ordenado por el código del libro (entero positivo, sin repetidos).

Todas las operaciones de los árboles son **recursivas** y no se usa `TreeSet`, `TreeMap`, `PriorityQueue`,
`NavigableSet`, `NavigableMap`, `Collections.sort` ni `List.sort`.

## Estructura

```
src/
├── enums/    TipoUnidad.java, TipoRecorrido.java
├── modelo/   Libro.java, UnidadBiblioteca.java
├── negocio/  NodoUnidad.java, NodoLibro.java, ArbolBiblioteca.java, CatalogoABB.java
└── app/      Main.java, DatosPrueba.java, PruebasBiblioteca.java
```

## Cómo ejecutar (JDK 11 o superior)

```bash
# Compilar
javac -encoding UTF-8 -d out $(find src -name "*.java")      # Linux / macOS / Git Bash
# En PowerShell:  javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName

# Menú interactivo
java -cp out app.Main

# Pruebas automáticas
java -cp out app.PruebasBiblioteca
```

En VS Code también se puede abrir la carpeta, y ejecutar `Main.java` o `PruebasBiblioteca.java` con *Run*.

## Funcionalidades

- Cargar la estructura inicial, mostrar la jerarquía, agregar y buscar unidades.
- Consultar altura, hojas, nivel y total de libros.
- Insertar, buscar, modificar y eliminar libros (eliminación con los tres casos: hoja, un hijo y dos hijos con sucesor inorden).
- Recorridos preorden, inorden y posorden con su traza.
- Retos: `contarEnRango`, `caminoBusqueda`, `esABBValido` y el extra `desdePreorden`.

## Captura de PruebasBiblioteca

> Reemplaza este bloque por tu captura de pantalla (`docs/pruebas.png`) y súbela al repositorio.

Salida de texto de la ejecución:

```text
=== PruebasBiblioteca ===
[OK]    Solo raíz: altura 0 y 1 hoja
[OK]    Inicial: altura 2, 4 hojas y 13 libros
[OK]    Inicial: 7 unidades en la jerarquía
[OK]    Niveles de BIB, sw, SW-E2 y XYZ: 0, 1, 2, -1
[OK]    Agregar ESTANTE como hijo de BIBLIOTECA: false
[OK]    Agregar unidad con código repetido (sw): false
[OK]    Agregar AREA válida: true y búsqueda sin distinguir mayúsculas
[OK]    Preorden SW-E1 = 60 35 20 45 40 80 70 75 90
[OK]    Inorden SW-E1 = 20 35 40 45 60 70 75 80 90
[OK]    Posorden SW-E1 = 20 40 45 35 75 70 90 80 60
[OK]    Traza de preorden termina con la secuencia correcta
[OK]    Insertar código repetido (60): false
[OK]    caminoBusqueda(42) = [60, 35, 45, 40]
[OK]    contarEnRango(40, 75) = 5
[OK]    Insertar 42 y eliminar 20, 70 y 60: preorden 75 35 45 40 42 80 90
[OK]    Buscar, modificar y eliminar código inexistente: null / false / false
[OK]    Modificar existente cambia título, autor y año
[OK]    Libro con datos inválidos: IllegalArgumentException
[OK]    esABBValido() en el árbol inicial: true
[OK]    esABBValido() con un nieto mal ubicado: false
[OK]    desdePreorden reconstruye el mismo ABB (inorden ordenado y válido)

21/21 pruebas OK
```

## Documentación adicional

- [`TRAZAS.md`](TRAZAS.md): resolución a mano y comparación con la salida del programa.
