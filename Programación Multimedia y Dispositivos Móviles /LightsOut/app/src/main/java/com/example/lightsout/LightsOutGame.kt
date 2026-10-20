package com.example.lightsout

import kotlin.random.Random

/**
 * LÓGICA DEL JUEGO (el "cerebro").
 *
 * Esta clase NO sabe nada de Android: no tiene botones, ni colores, ni pantallas.
 * Solo guarda el estado del tablero y aplica las reglas. Así:
 *   - El código de la pantalla (MainActivity) queda más limpio.
 *   - Se puede probar con tests normales de JUnit (ver LightsOutGameTest).
 *
 * Cómo se guarda el tablero:
 *   Un array de Boolean de tamaño (tamano * tamano). Cada posición es una casilla.
 *     false -> la casilla está en COLOR PRIMARIO   (estado inicial)
 *     true  -> la casilla está en COLOR SECUNDARIO (objetivo para ganar)
 *
 *   Para pasar de (fila, columna) a la posición del array se usa:
 *       indice = fila * tamano + columna
 *   Ejemplo en un 3x3:
 *       (0,0)=0  (0,1)=1  (0,2)=2
 *       (1,0)=3  (1,1)=4  (1,2)=5
 *       (2,0)=6  (2,1)=7  (2,2)=8
 *
 * @param tamano número de filas (y de columnas). 3 -> tablero 3x3.
 */
class LightsOutGame(val tamano: Int) {

    // Estado de cada casilla. Al crearse, todas valen false (color primario).
    val casillas = BooleanArray(tamano * tamano)

    // Número de pulsaciones que ha hecho el jugador.
    // "private set" = desde fuera se puede LEER pero no modificar.
    var movimientos = 0
        private set

    companion object {
        // Desplazamientos (fila, columna) de la "cruz" que se ve afectada al pulsar:
        // la propia casilla, arriba, abajo, izquierda y derecha. SIN diagonales.
        private val CRUZ = listOf(
            0 to 0,   // la casilla pulsada
            -1 to 0,  // arriba
            1 to 0,   // abajo
            0 to -1,  // izquierda
            0 to 1    // derecha
        )
    }

    /** Convierte (fila, columna) en la posición dentro del array. */
    fun indice(fila: Int, columna: Int): Int = fila * tamano + columna

    /** Devuelve true si la casilla está en color secundario. */
    fun esSecundario(fila: Int, columna: Int): Boolean = casillas[indice(fila, columna)]

    /**
     * Lo que pasa cuando el JUGADOR pulsa una casilla:
     * se invierte la cruz y se suma un movimiento.
     */
    fun pulsar(fila: Int, columna: Int) {
        invertirCruz(fila, columna)
        movimientos++
    }

    /**
     * ALGORITMO DE ADYACENCIA.
     * Recorre las 5 posiciones de la cruz. Si la posición existe dentro del
     * tablero (no se sale por los bordes), cambia su estado: true <-> false.
     */
    private fun invertirCruz(fila: Int, columna: Int) {
        for ((df, dc) in CRUZ) {
            val f = fila + df
            val c = columna + dc
            // Comprobamos que el vecino está dentro del tablero.
            // Ej: la casilla (0,0) no tiene vecino "arriba" ni "izquierda".
            if (f in 0 until tamano && c in 0 until tamano) {
                val i = indice(f, c)
                casillas[i] = !casillas[i]   // primario <-> secundario
            }
        }
    }

    /** Victoria: TODAS las casillas están en color secundario. */
    fun haGanado(): Boolean = casillas.all { it }

    /** Reiniciar: todas las casillas vuelven a color primario y movimientos a 0. */
    fun reiniciar() {
        casillas.fill(false)
        movimientos = 0
    }

    /**
     * MEJORA: generar un tablero aleatorio que SIEMPRE tenga solución.
     *
     * Truco: en Lights Out pulsar dos veces la misma casilla deja todo igual
     * (cada pulsación se "deshace" a sí misma). Por eso:
     *   1. Partimos del estado GANADOR (todo en secundario).
     *   2. Aplicamos N pulsaciones al azar.
     *   3. Si el jugador repite esas mismas pulsaciones, vuelve al estado ganador.
     * => Siempre existe al menos una solución.
     *
     * Si por casualidad el resultado ya es el estado ganador, se repite.
     *
     * @param random se puede pasar uno con semilla fija para los tests.
     */
    fun generarAleatorio(random: Random = Random.Default) {
        do {
            casillas.fill(true)
            repeat(tamano * tamano) {
                invertirCruz(random.nextInt(tamano), random.nextInt(tamano))
            }
        } while (haGanado())
        movimientos = 0
    }

    /**
     * Restaura un estado guardado (se usa al girar la pantalla, porque Android
     * destruye y vuelve a crear la Activity).
     */
    fun restaurar(estadoGuardado: BooleanArray, movimientosGuardados: Int) {
        if (estadoGuardado.size == casillas.size) {
            estadoGuardado.copyInto(casillas)
            movimientos = movimientosGuardados
        }
    }
}
