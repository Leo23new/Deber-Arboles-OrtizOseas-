package negocio;

import enums.TipoRecorrido;
import modelo.Libro;

import java.util.ArrayList;
import java.util.List;

/**
 * Árbol binario de búsqueda de libros ordenado por código.
 * Todas las operaciones sobre el árbol son recursivas y no se usa ninguna
 * estructura ordenada de la librería estándar ni se ordenan listas.
 */
public class CatalogoABB {
    private NodoLibro raiz;

    public NodoLibro getRaiz() { return raiz; }
    public void setRaiz(NodoLibro raiz) { this.raiz = raiz; }

    // ---------------------------------------------------------------- insertar
    /** Inserta por código. false si el código ya existe (o el libro es null). */
    public boolean insertar(Libro libro) {
        if (libro == null || buscar(libro.getCodigo()) != null) {
            return false;
        }
        raiz = insertar(raiz, libro);
        return true;
    }

    private NodoLibro insertar(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);
        }
        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), libro));
        } else {
            nodo.setDerecho(insertar(nodo.getDerecho(), libro));
        }
        return nodo;
    }

    // ------------------------------------------------------------------ buscar
    /** Busca un libro por código; null si no existe. */
    public Libro buscar(int codigo) {
        NodoLibro n = buscarNodo(raiz, codigo);
        return (n == null) ? null : n.getDato();
    }

    private NodoLibro buscarNodo(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int c = nodo.getDato().getCodigo();
        if (codigo == c) {
            return nodo;
        }
        return (codigo < c) ? buscarNodo(nodo.getIzquierdo(), codigo)
                            : buscarNodo(nodo.getDerecho(), codigo);
    }

    // ------------------------------------------------------------------ contar
    public int contar() {
        return contar(raiz);
    }

    private int contar(NodoLibro nodo) {
        return (nodo == null) ? 0 : 1 + contar(nodo.getIzquierdo()) + contar(nodo.getDerecho());
    }

    // ---------------------------------------------------------------- recorrer
    public List<Libro> recorrer(TipoRecorrido tipo) {
        validarTipo(tipo);
        List<Libro> lista = new ArrayList<>();
        recorrer(raiz, tipo, lista);
        return lista;
    }

    private void recorrer(NodoLibro nodo, TipoRecorrido tipo, List<Libro> lista) {
        if (nodo == null) {
            return;
        }
        if (tipo == TipoRecorrido.PREORDEN) lista.add(nodo.getDato());
        recorrer(nodo.getIzquierdo(), tipo, lista);
        if (tipo == TipoRecorrido.INORDEN) lista.add(nodo.getDato());
        recorrer(nodo.getDerecho(), tipo, lista);
        if (tipo == TipoRecorrido.POSORDEN) lista.add(nodo.getDato());
    }

    private void validarTipo(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
    }

    // ------------------------------------------------------------------- traza
    /**
     * Traza del recorrido: "entra X", "VISITA X", "null -> regresa",
     * 2 espacios por nivel y última línea "Secuencia TIPO: ...".
     */
    public String trazaRecorrido(TipoRecorrido tipo) {
        validarTipo(tipo);
        StringBuilder sb = new StringBuilder();
        List<Integer> secuencia = new ArrayList<>();
        traza(raiz, 0, tipo, sb, secuencia);
        sb.append("Secuencia ").append(tipo).append(":");
        for (Integer codigo : secuencia) {
            sb.append(" ").append(codigo);
        }
        return sb.toString();
    }

    private void traza(NodoLibro nodo, int nivel, TipoRecorrido tipo,
                       StringBuilder sb, List<Integer> secuencia) {
        String sangria = "  ".repeat(nivel);
        if (nodo == null) {
            sb.append(sangria).append("null -> regresa\n");
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        sb.append(sangria).append("entra ").append(codigo).append("\n");
        if (tipo == TipoRecorrido.PREORDEN) visitar(codigo, sangria, sb, secuencia);
        traza(nodo.getIzquierdo(), nivel + 1, tipo, sb, secuencia);
        if (tipo == TipoRecorrido.INORDEN) visitar(codigo, sangria, sb, secuencia);
        traza(nodo.getDerecho(), nivel + 1, tipo, sb, secuencia);
        if (tipo == TipoRecorrido.POSORDEN) visitar(codigo, sangria, sb, secuencia);
    }

    private void visitar(int codigo, String sangria, StringBuilder sb, List<Integer> secuencia) {
        sb.append(sangria).append("VISITA ").append(codigo).append("\n");
        secuencia.add(codigo);
    }

    // ---------------------------------------------------------------- modificar
    /** Modifica título, autor y año (no el código). false si no existe. */
    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        NodoLibro nodo = buscarNodo(raiz, codigo);
        if (nodo == null) {
            return false;
        }
        new Libro(codigo, titulo, autor, anio);   // valida todo antes de tocar el nodo
        Libro libro = nodo.getDato();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setAnio(anio);
        return true;
    }

    // ----------------------------------------------------------------- eliminar
    /** Elimina hoja, nodo con un hijo o nodo con dos hijos (sucesor inorden). */
    public boolean eliminar(int codigo) {
        if (buscar(codigo) == null) {
            return false;
        }
        raiz = eliminar(raiz, codigo);
        return true;
    }

    private NodoLibro eliminar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int c = nodo.getDato().getCodigo();
        if (codigo < c) {
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), codigo));
        } else if (codigo > c) {
            nodo.setDerecho(eliminar(nodo.getDerecho(), codigo));
        } else {
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();          // hoja o solo hijo derecho
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();        // solo hijo izquierdo
            }
            NodoLibro sucesor = minimo(nodo.getDerecho());   // dos hijos
            nodo.setDato(sucesor.getDato());
            nodo.setDerecho(eliminar(nodo.getDerecho(), sucesor.getDato().getCodigo()));
        }
        return nodo;
    }

    private NodoLibro minimo(NodoLibro nodo) {
        return (nodo.getIzquierdo() == null) ? nodo : minimo(nodo.getIzquierdo());
    }

    // -------------------------------------------------------------------- retos
    /** Cuenta los códigos en [a, b] podando las ramas que no pueden contener rangos. */
    public int contarEnRango(int a, int b) {
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        return contarEnRango(raiz, min, max);
    }

    private int contarEnRango(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }
        int c = nodo.getDato().getCodigo();
        if (c < a) {
            return contarEnRango(nodo.getDerecho(), a, b);     // todo el izquierdo es < a
        }
        if (c > b) {
            return contarEnRango(nodo.getIzquierdo(), a, b);   // todo el derecho es > b
        }
        return 1 + contarEnRango(nodo.getIzquierdo(), a, b) + contarEnRango(nodo.getDerecho(), a, b);
    }

    /** Códigos visitados desde la raíz hasta el libro (o hasta donde termina la búsqueda). */
    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        camino(raiz, codigo, camino);
        return camino;
    }

    private void camino(NodoLibro nodo, int codigo, List<Integer> camino) {
        if (nodo == null) {
            return;
        }
        int c = nodo.getDato().getCodigo();
        camino.add(c);
        if (codigo < c) {
            camino(nodo.getIzquierdo(), codigo, camino);
        } else if (codigo > c) {
            camino(nodo.getDerecho(), codigo, camino);
        }
    }

    /** Verifica en todo el árbol que cada nodo esté dentro de sus límites mínimo y máximo. */
    public boolean esABBValido() {
        return validar(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean validar(NodoLibro nodo, long min, long max) {
        if (nodo == null) {
            return true;
        }
        long c = nodo.getDato().getCodigo();
        if (c <= min || c >= max) {
            return false;
        }
        return validar(nodo.getIzquierdo(), min, c) && validar(nodo.getDerecho(), c, max);
    }

    // ------------------------------------------------------------ extra (+0,15)
    /** Reconstruye el ABB a partir de su preorden en O(n), sin insertar uno por uno. */
    public static CatalogoABB desdePreorden(int[] preorden) {
        if (preorden == null) {
            throw new IllegalArgumentException("El preorden no puede ser null.");
        }
        CatalogoABB catalogo = new CatalogoABB();
        int[] pos = {0};
        catalogo.raiz = construir(preorden, pos, Long.MIN_VALUE, Long.MAX_VALUE);
        if (pos[0] != preorden.length) {
            throw new IllegalArgumentException("La secuencia no es un preorden válido de un ABB.");
        }
        return catalogo;
    }

    private static NodoLibro construir(int[] pre, int[] pos, long min, long max) {
        if (pos[0] >= pre.length) {
            return null;
        }
        int valor = pre[pos[0]];
        if (valor <= min || valor >= max) {
            return null;
        }
        pos[0]++;
        NodoLibro nodo = new NodoLibro(new Libro(valor, "Libro " + valor, "Autor desconocido", 2000));
        nodo.setIzquierdo(construir(pre, pos, min, valor));
        nodo.setDerecho(construir(pre, pos, valor, max));
        return nodo;
    }
}
