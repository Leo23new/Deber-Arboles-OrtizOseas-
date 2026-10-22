package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Pruebas automáticas (sin JUnit). Imprime "X/N pruebas OK". */
public class PruebasBiblioteca {
    private static int total = 0;
    private static int correctas = 0;

    private static void verificar(String nombre, boolean condicion) {
        total++;
        if (condicion) {
            correctas++;
        }
        System.out.println((condicion ? "[OK]    " : "[FALLA] ") + nombre);
    }

    private static List<Integer> codigos(List<Libro> libros) {
        List<Integer> r = new ArrayList<>();
        for (Libro l : libros) {
            r.add(l.getCodigo());
        }
        return r;
    }

    private static CatalogoABB sw1(ArbolBiblioteca arbol) {
        return arbol.buscar("SW-E1").getCatalogo();
    }

    public static void main(String[] args) {
        ejecutar();
    }

    public static void ejecutar() {
        total = 0;
        correctas = 0;
        System.out.println("=== PruebasBiblioteca ===");

        // 1. Biblioteca solo con la raíz
        ArbolBiblioteca soloRaiz = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        verificar("Solo raíz: altura 0 y 1 hoja", soloRaiz.altura() == 0 && soloRaiz.contarHojas() == 1);

        // 2. Biblioteca inicial
        ArbolBiblioteca b = DatosPrueba.cargarEstructuraInicial();
        verificar("Inicial: altura 2, 4 hojas y 13 libros",
                b.altura() == 2 && b.contarHojas() == 4 && b.contarLibrosTotal() == 13);
        verificar("Inicial: 7 unidades en la jerarquía", b.mostrarJerarquia().split("\n").length == 7);

        // 3. Niveles
        verificar("Niveles de BIB, sw, SW-E2 y XYZ: 0, 1, 2, -1",
                b.nivel("BIB") == 0 && b.nivel("sw") == 1 && b.nivel("SW-E2") == 2 && b.nivel("XYZ") == -1);

        // 4. Jerarquía inválida y códigos repetidos
        verificar("Agregar ESTANTE como hijo de BIBLIOTECA: false",
                !b.agregar("BIB", new UnidadBiblioteca("E-X", "Estante suelto", TipoUnidad.ESTANTE)));
        verificar("Agregar unidad con código repetido (sw): false",
                !b.agregar("BIB", new UnidadBiblioteca("sw", "Otra", TipoUnidad.AREA)));
        verificar("Agregar AREA válida: true y búsqueda sin distinguir mayúsculas",
                b.agregar("bib", new UnidadBiblioteca("ia", "Inteligencia Artificial", TipoUnidad.AREA))
                        && b.buscar("IA") != null);

        // 5. Recorridos de SW-E1
        CatalogoABB c = sw1(b);
        verificar("Preorden SW-E1 = 60 35 20 45 40 80 70 75 90",
                codigos(c.recorrer(TipoRecorrido.PREORDEN)).equals(Arrays.asList(60, 35, 20, 45, 40, 80, 70, 75, 90)));
        verificar("Inorden SW-E1 = 20 35 40 45 60 70 75 80 90",
                codigos(c.recorrer(TipoRecorrido.INORDEN)).equals(Arrays.asList(20, 35, 40, 45, 60, 70, 75, 80, 90)));
        verificar("Posorden SW-E1 = 20 40 45 35 75 70 90 80 60",
                codigos(c.recorrer(TipoRecorrido.POSORDEN)).equals(Arrays.asList(20, 40, 45, 35, 75, 70, 90, 80, 60)));
        verificar("Traza de preorden termina con la secuencia correcta",
                c.trazaRecorrido(TipoRecorrido.PREORDEN).endsWith("Secuencia PREORDEN: 60 35 20 45 40 80 70 75 90"));

        // 6. Código repetido en el ABB
        verificar("Insertar código repetido (60): false", !c.insertar(DatosPrueba.crearLibro(60)));

        // 7. Retos
        verificar("caminoBusqueda(42) = [60, 35, 45, 40]",
                c.caminoBusqueda(42).equals(Arrays.asList(60, 35, 45, 40)));
        verificar("contarEnRango(40, 75) = 5", c.contarEnRango(40, 75) == 5);

        // 8. Inserciones y eliminaciones encadenadas
        boolean ins = c.insertar(DatosPrueba.crearLibro(42));
        boolean e1 = c.eliminar(20);   // hoja
        boolean e2 = c.eliminar(70);   // un hijo
        boolean e3 = c.eliminar(60);   // dos hijos (raíz)
        verificar("Insertar 42 y eliminar 20, 70 y 60: preorden 75 35 45 40 42 80 90",
                ins && e1 && e2 && e3
                        && codigos(c.recorrer(TipoRecorrido.PREORDEN)).equals(Arrays.asList(75, 35, 45, 40, 42, 80, 90))
                        && c.esABBValido());

        // 9. Inexistentes
        verificar("Buscar, modificar y eliminar código inexistente: null / false / false",
                c.buscar(999) == null && !c.modificar(999, "T", "A", 2000) && !c.eliminar(999));
        verificar("Modificar existente cambia título, autor y año",
                c.modificar(75, "Nuevo título", "Nuevo autor", 2020)
                        && c.buscar(75).getTitulo().equals("Nuevo título") && c.buscar(75).getAnio() == 2020);

        // 10. Validaciones del libro
        boolean lanzo = false;
        try {
            new Libro(-5, "", "X", 1800);
        } catch (IllegalArgumentException ex) {
            lanzo = true;
        }
        verificar("Libro con datos inválidos: IllegalArgumentException", lanzo);

        // 11. esABBValido
        verificar("esABBValido() en el árbol inicial: true", sw1(DatosPrueba.cargarEstructuraInicial()).esABBValido());
        CatalogoABB malo = new CatalogoABB();
        NodoLibro r = new NodoLibro(DatosPrueba.crearLibro(50));
        NodoLibro izq = new NodoLibro(DatosPrueba.crearLibro(30));
        r.setIzquierdo(izq);
        izq.setDerecho(new NodoLibro(DatosPrueba.crearLibro(60)));   // nieto mal ubicado (60 > 50)
        malo.setRaiz(r);
        verificar("esABBValido() con un nieto mal ubicado: false", !malo.esABBValido());

        // 12. Extra desdePreorden
        CatalogoABB rec = CatalogoABB.desdePreorden(new int[]{60, 35, 20, 45, 40, 80, 70, 75, 90});
        verificar("desdePreorden reconstruye el mismo ABB (inorden ordenado y válido)",
                rec.esABBValido() && codigos(rec.recorrer(TipoRecorrido.INORDEN))
                        .equals(Arrays.asList(20, 35, 40, 45, 60, 70, 75, 80, 90))
                        && codigos(rec.recorrer(TipoRecorrido.PREORDEN))
                        .equals(Arrays.asList(60, 35, 20, 45, 40, 80, 70, 75, 90)));

        System.out.println();
        System.out.println(correctas + "/" + total + " pruebas OK");
    }
}
