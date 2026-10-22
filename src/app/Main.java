package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

import java.util.List;
import java.util.Scanner;

/** Menú interactivo del catálogo de la Biblioteca FISEI. */
public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static ArbolBiblioteca biblioteca = null;

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opción: ");
            try {
                ejecutar(opcion);
            } catch (IllegalArgumentException ex) {
                System.out.println("Dato inválido: " + ex.getMessage());
            }
        } while (opcion != 0);
        System.out.println("Hasta pronto.");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== BIBLIOTECA FISEI =====");
        System.out.println(" 1. Cargar estructura inicial");
        System.out.println(" 2. Mostrar jerarquía");
        System.out.println(" 3. Agregar unidad");
        System.out.println(" 4. Buscar unidad");
        System.out.println(" 5. Consultar altura, hojas, nivel y total de libros");
        System.out.println(" 6. Insertar libro en un estante");
        System.out.println(" 7. Buscar libro en un estante");
        System.out.println(" 8. Modificar libro");
        System.out.println(" 9. Eliminar libro");
        System.out.println("10. Mostrar recorridos con traza");
        System.out.println("11. Ejecutar retos");
        System.out.println("12. Ejecutar PruebasBiblioteca");
        System.out.println(" 0. Salir");
    }

    private static void ejecutar(int opcion) {
        if (opcion == 0) {
            return;
        }
        if (opcion == 12) {
            PruebasBiblioteca.ejecutar();
            return;
        }
        if (opcion == 1) {
            biblioteca = DatosPrueba.cargarEstructuraInicial();
            System.out.println("Estructura inicial cargada.");
            return;
        }
        if (opcion < 1 || opcion > 12) {
            System.out.println("Opción no válida.");
            return;
        }
        if (biblioteca == null) {
            System.out.println("Primero cargue la estructura inicial (opción 1).");
            return;
        }
        switch (opcion) {
            case 2:  System.out.print(biblioteca.mostrarJerarquia()); break;
            case 3:  agregarUnidad(); break;
            case 4:  buscarUnidad(); break;
            case 5:  consultas(); break;
            case 6:  insertarLibro(); break;
            case 7:  buscarLibro(); break;
            case 8:  modificarLibro(); break;
            case 9:  eliminarLibro(); break;
            case 10: recorridos(); break;
            case 11: retos(); break;
            default: break;
        }
    }

    // ------------------------------------------------------------ lectura segura
    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return SC.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            String s = leerTexto(mensaje);
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }

    private static CatalogoABB pedirEstante() {
        String codigo = leerTexto("Código del estante (ej. SW-E1): ");
        NodoUnidad nodo = biblioteca.buscar(codigo);
        if (nodo == null || nodo.getCatalogo() == null) {
            System.out.println("No existe un ESTANTE con ese código.");
            return null;
        }
        return nodo.getCatalogo();
    }

    // ---------------------------------------------------------------- opciones
    private static void agregarUnidad() {
        String padre = leerTexto("Código de la unidad padre: ");
        String codigo = leerTexto("Código de la nueva unidad: ");
        String nombre = leerTexto("Nombre: ");
        System.out.println("Tipo: 1) BIBLIOTECA  2) AREA  3) ESTANTE");
        int t = leerEntero("Tipo: ");
        if (t < 1 || t > 3) {
            System.out.println("Tipo no válido.");
            return;
        }
        UnidadBiblioteca nueva = new UnidadBiblioteca(codigo, nombre, TipoUnidad.values()[t - 1]);
        System.out.println(biblioteca.agregar(padre, nueva)
                ? "Unidad agregada."
                : "No se pudo agregar (padre inexistente, código repetido o jerarquía inválida).");
    }

    private static void buscarUnidad() {
        NodoUnidad n = biblioteca.buscar(leerTexto("Código a buscar: "));
        System.out.println(n == null ? "No existe esa unidad." : "Encontrada: " + n.getDato());
    }

    private static void consultas() {
        System.out.println("Altura (aristas): " + biblioteca.altura());
        System.out.println("Hojas: " + biblioteca.contarHojas());
        System.out.println("Total de libros: " + biblioteca.contarLibrosTotal());
        String codigo = leerTexto("Código de unidad para conocer su nivel: ");
        System.out.println("Nivel de " + codigo + ": " + biblioteca.nivel(codigo));
    }

    private static void insertarLibro() {
        CatalogoABB c = pedirEstante();
        if (c == null) return;
        int codigo = leerEntero("Código del libro: ");
        String titulo = leerTexto("Título: ");
        String autor = leerTexto("Autor: ");
        int anio = leerEntero("Año (1900-2026): ");
        Libro libro = new Libro(codigo, titulo, autor, anio);
        System.out.println(c.insertar(libro) ? "Libro insertado." : "El código ya existe.");
    }

    private static void buscarLibro() {
        CatalogoABB c = pedirEstante();
        if (c == null) return;
        Libro l = c.buscar(leerEntero("Código del libro: "));
        System.out.println(l == null ? "No existe ese libro." : "Encontrado: " + l);
    }

    private static void modificarLibro() {
        CatalogoABB c = pedirEstante();
        if (c == null) return;
        int codigo = leerEntero("Código del libro a modificar: ");
        String titulo = leerTexto("Nuevo título: ");
        String autor = leerTexto("Nuevo autor: ");
        int anio = leerEntero("Nuevo año (1900-2026): ");
        System.out.println(c.modificar(codigo, titulo, autor, anio) ? "Libro modificado." : "No existe ese libro.");
    }

    private static void eliminarLibro() {
        CatalogoABB c = pedirEstante();
        if (c == null) return;
        System.out.println(c.eliminar(leerEntero("Código del libro a eliminar: "))
                ? "Libro eliminado." : "No existe ese libro.");
    }

    private static void recorridos() {
        CatalogoABB c = pedirEstante();
        if (c == null) return;
        System.out.println("1) PREORDEN  2) INORDEN  3) POSORDEN");
        int t = leerEntero("Recorrido: ");
        if (t < 1 || t > 3) {
            System.out.println("Recorrido no válido.");
            return;
        }
        TipoRecorrido tipo = TipoRecorrido.values()[t - 1];
        for (Libro l : c.recorrer(tipo)) {
            System.out.println("  " + l);
        }
        System.out.println(c.trazaRecorrido(tipo));
    }

    private static void retos() {
        System.out.println("1) contarEnRango  2) caminoBusqueda  3) esABBValido  4) desdePreorden (extra)");
        int r = leerEntero("Reto: ");
        if (r == 4) {
            String linea = leerTexto("Preorden (números separados por espacios): ");
            String[] partes = linea.isEmpty() ? new String[0] : linea.split("\\s+");
            int[] pre = new int[partes.length];
            try {
                for (int i = 0; i < partes.length; i++) {
                    pre[i] = Integer.parseInt(partes[i]);
                }
            } catch (NumberFormatException ex) {
                System.out.println("Solo se aceptan números enteros.");
                return;
            }
            CatalogoABB nuevo = CatalogoABB.desdePreorden(pre);
            System.out.println(nuevo.trazaRecorrido(TipoRecorrido.INORDEN).replaceAll("(?s).*(Secuencia)", "$1"));
            System.out.println(nuevo.trazaRecorrido(TipoRecorrido.PREORDEN).replaceAll("(?s).*(Secuencia)", "$1"));
            return;
        }
        CatalogoABB c = pedirEstante();
        if (c == null) return;
        switch (r) {
            case 1:
                int a = leerEntero("Desde (a): ");
                int b = leerEntero("Hasta (b): ");
                System.out.println("Libros en [" + a + ", " + b + "]: " + c.contarEnRango(a, b));
                break;
            case 2:
                int cod = leerEntero("Código a buscar: ");
                List<Integer> camino = c.caminoBusqueda(cod);
                System.out.println("Camino: " + camino);
                break;
            case 3:
                System.out.println("¿Es un ABB válido? " + c.esABBValido());
                break;
            default:
                System.out.println("Reto no válido.");
        }
    }
}
