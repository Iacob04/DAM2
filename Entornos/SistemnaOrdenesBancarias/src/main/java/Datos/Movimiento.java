package Datos;

import java.time.LocalDate;

/**
 * Un movimiento de la cuenta. Los depósitos tienen importe positivo
 * y las retiradas importe negativo.
 */
public record Movimiento(LocalDate fecha, int importe) {
}
