package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;

/** Construye la estructura inicial de la Biblioteca FISEI. */
public class DatosPrueba {

    private DatosPrueba() { }

    public static ArbolBiblioteca cargarEstructuraInicial() {
        ArbolBiblioteca arbol = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));

        arbol.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        arbol.agregar("BIB", new UnidadBiblioteca("TI", "Tecnologías de la Información", TipoUnidad.AREA));
        arbol.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));

        arbol.agregar("SW", new UnidadBiblioteca("SW-E1", "Estante 1 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E2", "Estante 2 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));

        // Los libros se insertan en el orden indicado en el enunciado.
        int[] sw1 = {60, 35, 80, 20, 45, 70, 90, 40, 75};
        for (int codigo : sw1) {
            arbol.buscar("SW-E1").getCatalogo().insertar(crearLibro(codigo));
        }
        int[] sw2 = {15, 10, 25};
        for (int codigo : sw2) {
            arbol.buscar("SW-E2").getCatalogo().insertar(crearLibro(codigo));
        }
        arbol.buscar("TI-E1").getCatalogo().insertar(crearLibro(50));
        return arbol;
    }

    /** Libro de ejemplo con datos válidos derivados del código. */
    public static Libro crearLibro(int codigo) {
        return new Libro(codigo, "Libro de ejemplo " + codigo, "Autor " + codigo, 2000 + codigo % 26);
    }
}
