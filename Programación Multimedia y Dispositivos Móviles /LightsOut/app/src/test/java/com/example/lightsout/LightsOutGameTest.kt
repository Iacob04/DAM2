package com.example.lightsout

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

/**
 * Tests unitarios de la lógica del juego.
 * Se ejecutan en el ordenador (no hace falta emulador):
 * clic derecho sobre esta clase -> Run 'LightsOutGameTest'.
 */
class LightsOutGameTest {

    @Test
    fun alEmpezar_todoEnPrimario() {
        val juego = LightsOutGame(3)
        assertTrue(juego.casillas.none { it })
        assertEquals(0, juego.movimientos)
    }

    @Test
    fun pulsarCentro_cambiaCruzDe5() {
        val juego = LightsOutGame(3)
        juego.pulsar(1, 1)
        // Esperado (true = secundario):
        // . X .
        // X X X
        // . X .
        val esperado = booleanArrayOf(
            false, true, false,
            true, true, true,
            false, true, false
        )
        assertTrue(esperado.contentEquals(juego.casillas))
        assertEquals(1, juego.movimientos)
    }

    @Test
    fun pulsarEsquina_soloCambia3() {
        val juego = LightsOutGame(3)
        juego.pulsar(0, 0)
        // La esquina no tiene vecino arriba ni a la izquierda
        assertEquals(3, juego.casillas.count { it })
        assertTrue(juego.esSecundario(0, 0))
        assertTrue(juego.esSecundario(0, 1))
        assertTrue(juego.esSecundario(1, 0))
    }

    @Test
    fun pulsarDosVeces_dejaTodoIgual() {
        val juego = LightsOutGame(4)
        juego.pulsar(2, 1)
        juego.pulsar(2, 1)
        assertTrue(juego.casillas.none { it })
    }

    @Test
    fun reiniciar_vuelveAPrimario() {
        val juego = LightsOutGame(3)
        juego.pulsar(0, 0)
        juego.pulsar(2, 2)
        juego.reiniciar()
        assertTrue(juego.casillas.none { it })
        assertEquals(0, juego.movimientos)
    }

    @Test
    fun solucionConocida3x3_gana() {
        // En el 3x3, pulsar las 4 esquinas y el centro pone todo en secundario
        val juego = LightsOutGame(3)
        assertFalse(juego.haGanado())
        juego.pulsar(0, 0)
        juego.pulsar(0, 2)
        juego.pulsar(1, 1)
        juego.pulsar(2, 0)
        juego.pulsar(2, 2)
        assertTrue(juego.haGanado())
    }

    @Test
    fun aleatorio_noEmpiezaGanado() {
        for (tamano in 3..5) {
            val juego = LightsOutGame(tamano)
            juego.generarAleatorio(Random(42))
            assertFalse(juego.haGanado())
            assertEquals(0, juego.movimientos)
        }
    }
}
