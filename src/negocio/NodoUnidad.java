package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

import java.util.ArrayList;
import java.util.List;

/** Nodo del árbol general n-ario. Solo los ESTANTE tienen catálogo de libros. */
public class NodoUnidad {
    private final UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos;
    private final CatalogoABB catalogo;

    public NodoUnidad(UnidadBiblioteca dato) {
        this.dato = dato;
        this.hijos = new ArrayList<>();
        this.catalogo = (dato.getTipo() == TipoUnidad.ESTANTE) ? new CatalogoABB() : null;
    }

    public UnidadBiblioteca getDato() { return dato; }
    public List<NodoUnidad> getHijos() { return hijos; }

    /** @return el catálogo ABB, o null si la unidad no es un ESTANTE. */
    public CatalogoABB getCatalogo() { return catalogo; }
}
