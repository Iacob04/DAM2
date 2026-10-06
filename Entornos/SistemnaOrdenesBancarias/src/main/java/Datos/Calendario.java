package Datos;

import java.time.LocalDate;

/**
 * Da la fecha de "hoy". Existe para que los tests puedan decidir qué día es
 * en lugar de depender de LocalDate.now().
 */
@FunctionalInterface
public interface Calendario {
    LocalDate hoy();
}
