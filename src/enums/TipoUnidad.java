package enums;

/** Tipos de unidad de la jerarquía de la biblioteca. */
public enum TipoUnidad {
    BIBLIOTECA, AREA, ESTANTE;

    /**
     * Regla de la jerarquía: BIBLIOTECA → AREA → ESTANTE.
     * Un ESTANTE no puede tener hijos (solo libros en su ABB).
     */
    public boolean puedeTenerHijo(TipoUnidad hijo) {
        if (hijo == null) {
            return false;
        }
        switch (this) {
            case BIBLIOTECA: return hijo == AREA;
            case AREA:       return hijo == ESTANTE;
            default:         return false;
        }
    }
}
