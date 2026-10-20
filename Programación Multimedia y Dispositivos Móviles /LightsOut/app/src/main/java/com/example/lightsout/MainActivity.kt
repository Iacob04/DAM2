package com.example.lightsout

import android.app.AlertDialog
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale
import kotlin.math.min

/**
 * PANTALLA PRINCIPAL (la "cara" del juego).
 *
 * Responsabilidades:
 *   - Crear los botones del tablero dentro del GridLayout.
 *   - Escuchar los clics (listeners) y pasárselos a LightsOutGame.
 *   - Pintar cada botón con el color que le toca.
 *   - Contador, cronómetro, sonido, vibración y aviso de victoria.
 *   - Guardar/restaurar la partida cuando se gira la pantalla (ciclo de vida).
 */
class MainActivity : ComponentActivity() {

    // ---------- Claves para guardar el estado al girar la pantalla ----------
    companion object {
        private const val CLAVE_TAMANO = "tamano"
        private const val CLAVE_CASILLAS = "casillas"
        private const val CLAVE_MOVIMIENTOS = "movimientos"
        private const val CLAVE_TERMINADA = "terminada"
        private const val CLAVE_TIEMPO = "tiempo"
        private const val CLAVE_CRONO_EN_MARCHA = "cronoEnMarcha"
        private const val TAMANO_MINIMO = 3   // la opción 0 del Spinner es 3x3
    }

    // ---------- Lógica ----------
    // "lateinit" = la inicializo más tarde (en onCreate), no al declararla.
    private lateinit var juego: LightsOutGame

    // ---------- Vistas (los elementos del XML) ----------
    private lateinit var contenedorTablero: FrameLayout
    private lateinit var gridTablero: GridLayout
    private lateinit var tvMovimientos: TextView
    private lateinit var cronometro: Chronometer
    private lateinit var spinnerTamano: Spinner

    // Lista con los botones del tablero. botones[i] corresponde a juego.casillas[i].
    private var botones: List<Button> = emptyList()

    // ---------- Estado de la partida ----------
    private var partidaTerminada = false   // true después de ganar: bloquea el tablero
    private var cronometroEnMarcha = false
    private var tiempoParado = 0L          // ms que marcaba el cronómetro al pararlo

    // ---------- Sonido, vibración y diálogo ----------
    private var sonido: ToneGenerator? = null
    private var vibrador: Vibrator? = null
    private var dialogoVictoria: AlertDialog? = null

    // =====================================================================
    // CICLO DE VIDA
    // =====================================================================

    /**
     * onCreate: se ejecuta al abrir la pantalla Y TAMBIÉN cada vez que se gira
     * el móvil (Android destruye la Activity y la crea de nuevo).
     * Si venimos de un giro, savedInstanceState trae los datos guardados.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()                         // la app ocupa toda la pantalla
        setContentView(R.layout.activity_main)     // carga el diseño XML
        aplicarMargenesDelSistema()

        // 1) Enlazar las variables con las vistas del XML por su id
        contenedorTablero = findViewById(R.id.contenedorTablero)
        gridTablero = findViewById(R.id.gridTablero)
        tvMovimientos = findViewById(R.id.tvMovimientos)
        cronometro = findViewById(R.id.cronometro)
        spinnerTamano = findViewById(R.id.spinnerTamano)

        // 2) Preparar sonido y vibración (MEJORA)
        sonido = try {
            ToneGenerator(AudioManager.STREAM_MUSIC, 80)   // 80 = volumen (0-100)
        } catch (e: RuntimeException) {
            null    // si el dispositivo no lo soporta, simplemente no habrá sonido
        }
        vibrador = getSystemService(Vibrator::class.java)

        // 3) Crear la lógica: nueva (3x3) o restaurada si venimos de un giro
        val tamano = savedInstanceState?.getInt(CLAVE_TAMANO, TAMANO_MINIMO) ?: TAMANO_MINIMO
        juego = LightsOutGame(tamano)
        if (savedInstanceState != null) {
            restaurarEstado(savedInstanceState)
        }

        // 4) Configurar la interfaz
        configurarSpinner(tamano)
        crearTablero()

        // 5) LISTENERS de los botones de control.
        //    setOnClickListener recibe una lambda { ... } que se ejecuta en cada clic.
        findViewById<Button>(R.id.btnReiniciar).setOnClickListener { reiniciarPartida() }
        findViewById<Button>(R.id.btnNuevaPartida).setOnClickListener { nuevaPartidaAleatoria() }
    }

    /**
     * onSaveInstanceState: Android lo llama ANTES de destruir la Activity
     * (por ejemplo al girar). Guardamos en el Bundle todo lo necesario
     * para dejar la partida exactamente igual al volver.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CLAVE_TAMANO, juego.tamano)
        outState.putBooleanArray(CLAVE_CASILLAS, juego.casillas)
        outState.putInt(CLAVE_MOVIMIENTOS, juego.movimientos)
        outState.putBoolean(CLAVE_TERMINADA, partidaTerminada)
        outState.putLong(CLAVE_TIEMPO, tiempoTranscurridoMs())
        outState.putBoolean(CLAVE_CRONO_EN_MARCHA, cronometroEnMarcha)
    }

    /**
     * onDestroy: la Activity se va a destruir. Liberamos recursos para no
     * dejar memoria ocupada ni ventanas "huérfanas".
     */
    override fun onDestroy() {
        dialogoVictoria?.dismiss()   // evita el error "WindowLeaked" al girar con el diálogo abierto
        sonido?.release()            // libera el generador de tonos
        sonido = null
        super.onDestroy()
    }

    /** Lee del Bundle lo que se guardó en onSaveInstanceState. */
    private fun restaurarEstado(estado: Bundle) {
        estado.getBooleanArray(CLAVE_CASILLAS)?.let { casillasGuardadas ->
            juego.restaurar(casillasGuardadas, estado.getInt(CLAVE_MOVIMIENTOS))
        }
        partidaTerminada = estado.getBoolean(CLAVE_TERMINADA)

        // El cronómetro funciona con una "base" (momento de inicio).
        // Base = ahora - tiempo que ya llevaba => sigue contando desde donde iba.
        val tiempo = estado.getLong(CLAVE_TIEMPO)
        tiempoParado = tiempo
        cronometro.base = SystemClock.elapsedRealtime() - tiempo
        cronometroEnMarcha = estado.getBoolean(CLAVE_CRONO_EN_MARCHA)
        if (cronometroEnMarcha) cronometro.start()
    }

    /**
     * Con edge-to-edge la app se dibuja debajo de la barra de estado y de la
     * barra de navegación. Este listener añade un margen interior (padding)
     * del tamaño de esas barras para que nada quede tapado.
     */
    private fun aplicarMargenesDelSistema() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }
    }

    // =====================================================================
    // CREACIÓN DEL TABLERO
    // =====================================================================

    /**
     * Crea los botones por código (no en el XML) porque su número depende del
     * tamaño elegido: 3x3 = 9, 4x4 = 16, 5x5 = 25.
     */
    private fun crearTablero() {
        val n = juego.tamano
        gridTablero.removeAllViews()   // borra los botones del tablero anterior
        gridTablero.columnCount = n    // GridLayout coloca n botones por fila

        // Creamos n*n botones. "i" es la posición en el array (0, 1, 2, ...).
        botones = List(n * n) { i ->
            val fila = i / n        // división entera:  i=5 en 3x3 -> fila 1
            val columna = i % n     // resto:            i=5 en 3x3 -> columna 2

            Button(this).apply {
                // Quitamos tamaños mínimos y sombra del botón por defecto
                // para que quepan bien incluso en 5x5.
                minWidth = 0
                minHeight = 0
                minimumWidth = 0
                minimumHeight = 0
                setPadding(0, 0, 0, 0)
                stateListAnimator = null

                // Accesibilidad: lo que lee TalkBack, p. ej. "Casilla fila 2, columna 3"
                contentDescription = getString(R.string.desc_casilla, fila + 1, columna + 1)

                // LISTENER de cada casilla: al pulsar, avisamos con su fila y columna.
                setOnClickListener { alPulsarCasilla(fila, columna) }
            }
        }
        botones.forEach { gridTablero.addView(it) }

        // Todavía no sabemos cuánto mide el contenedor (Android aún no lo ha dibujado).
        // post { } espera a que esté medido y entonces calcula el tamaño de las casillas.
        contenedorTablero.post { ajustarTamanoCasillas() }

        actualizarPantalla()
    }

    /**
     * Hace que las casillas sean CUADRADAS y que el tablero quepa en la pantalla
     * (tanto en vertical como en horizontal).
     */
    private fun ajustarTamanoCasillas() {
        val n = juego.tamano
        val margen = (4 * resources.displayMetrics.density).toInt()   // 4dp en píxeles
        // El lado del tablero es el menor entre ancho y alto del contenedor.
        val ladoTablero = min(contenedorTablero.width, contenedorTablero.height)
        val ladoCasilla = ladoTablero / n - 2 * margen
        if (ladoCasilla <= 0) return

        for (boton in botones) {
            val params = GridLayout.LayoutParams()
            params.width = ladoCasilla
            params.height = ladoCasilla
            params.setMargins(margen, margen, margen, margen)
            boton.layoutParams = params
        }
    }

    /** Configura el desplegable para elegir 3x3, 4x4 o 5x5 (MEJORA). */
    private fun configurarSpinner(tamanoActual: Int) {
        // El adaptador conecta la lista de textos (res/values/strings.xml) con el Spinner.
        val adaptador = ArrayAdapter.createFromResource(
            this, R.array.tamanos_tablero, android.R.layout.simple_spinner_item
        )
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTamano.adapter = adaptador
        spinnerTamano.setSelection(tamanoActual - TAMANO_MINIMO, false)

        // LISTENER del Spinner: se ejecuta cuando el usuario elige una opción.
        spinnerTamano.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val nuevoTamano = position + TAMANO_MINIMO   // posición 0 -> 3, 1 -> 4, 2 -> 5
                // Android también llama a esto al arrancar; solo cambiamos si es distinto.
                if (nuevoTamano != juego.tamano) {
                    cambiarTamano(nuevoTamano)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hace falta hacer nada
            }
        }
    }

    // =====================================================================
    // INTERACCIÓN DEL JUGADOR
    // =====================================================================

    /** Se ejecuta cada vez que se pulsa una casilla del tablero. */
    private fun alPulsarCasilla(fila: Int, columna: Int) {
        // Si ya ha ganado, no dejamos seguir tocando el tablero.
        if (partidaTerminada) {
            Toast.makeText(this, R.string.aviso_terminada, Toast.LENGTH_SHORT).show()
            return
        }

        if (!cronometroEnMarcha) iniciarCronometro()   // el tiempo empieza con el 1er clic

        juego.pulsar(fila, columna)   // 1. la lógica cambia la cruz
        actualizarPantalla()          // 2. repintamos los botones
        efectoPulsacion()             // 3. sonido + vibración

        // 4. Comprobamos la victoria DESPUÉS DE CADA MOVIMIENTO
        if (juego.haGanado()) {
            mostrarVictoria()
        }
    }

    /**
     * Pinta cada botón según su estado y actualiza el contador.
     * Los colores no se escriben aquí: están en res/drawable y res/values/colors.xml.
     */
    private fun actualizarPantalla() {
        val n = juego.tamano
        botones.forEachIndexed { i, boton ->
            val fondo = if (juego.esSecundario(i / n, i % n)) {
                R.drawable.casilla_secundaria
            } else {
                R.drawable.casilla_primaria
            }
            boton.setBackgroundResource(fondo)
        }
        tvMovimientos.text = getString(R.string.movimientos, juego.movimientos)
    }

    /** Botón REINICIAR: todo a color primario, contador y tiempo a cero. */
    private fun reiniciarPartida() {
        juego.reiniciar()
        prepararNuevaPartida()
    }

    /** Botón NUEVA PARTIDA (MEJORA): tablero aleatorio con solución garantizada. */
    private fun nuevaPartidaAleatoria() {
        juego.generarAleatorio()
        prepararNuevaPartida()
    }

    /** Spinner: se cambia de tamaño -> nueva lógica y nuevos botones. */
    private fun cambiarTamano(nuevoTamano: Int) {
        juego = LightsOutGame(nuevoTamano)
        partidaTerminada = false
        ponerCronometroACero()
        crearTablero()
    }

    /** Código común a Reiniciar y Nueva partida. */
    private fun prepararNuevaPartida() {
        partidaTerminada = false
        ponerCronometroACero()
        actualizarPantalla()
    }

    /** Victoria: paramos el tiempo, efectos y mostramos un AlertDialog. */
    private fun mostrarVictoria() {
        partidaTerminada = true
        pararCronometro()
        efectoVictoria()

        val mensaje = getString(
            R.string.victoria_mensaje,
            juego.movimientos,
            formatearTiempo(tiempoParado)
        )
        dialogoVictoria = AlertDialog.Builder(this)
            .setTitle(R.string.victoria_titulo)
            .setMessage(mensaje)
            .setPositiveButton(R.string.nueva_partida) { _, _ -> nuevaPartidaAleatoria() }
            .setNegativeButton(R.string.cerrar, null)
            .show()
    }

    // =====================================================================
    // CRONÓMETRO (MEJORA)
    // =====================================================================
    // Chronometer cuenta el tiempo desde su "base" (un instante en milisegundos
    // desde que se encendió el móvil: SystemClock.elapsedRealtime()).

    private fun iniciarCronometro() {
        cronometro.base = SystemClock.elapsedRealtime()
        cronometro.start()
        cronometroEnMarcha = true
    }

    private fun pararCronometro() {
        tiempoParado = SystemClock.elapsedRealtime() - cronometro.base
        cronometro.stop()
        cronometroEnMarcha = false
    }

    private fun ponerCronometroACero() {
        cronometro.stop()
        cronometroEnMarcha = false
        tiempoParado = 0L
        cronometro.base = SystemClock.elapsedRealtime()   // muestra 00:00
    }

    /** Milisegundos que lleva la partida (en marcha o ya parada). */
    private fun tiempoTranscurridoMs(): Long =
        if (cronometroEnMarcha) SystemClock.elapsedRealtime() - cronometro.base else tiempoParado

    /** 83000 ms -> "01:23" */
    private fun formatearTiempo(ms: Long): String {
        val segundos = ms / 1000
        return String.format(Locale.getDefault(), "%02d:%02d", segundos / 60, segundos % 60)
    }

    // =====================================================================
    // SONIDO Y VIBRACIÓN (MEJORA)
    // =====================================================================

    private fun efectoPulsacion() {
        sonido?.startTone(ToneGenerator.TONE_PROP_BEEP, 60)   // "bip" corto
        vibrar(30)
    }

    private fun efectoVictoria() {
        sonido?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 600)   // melodía corta
        vibrar(300)
    }

    /** Vibra los milisegundos indicados. Necesita el permiso VIBRATE en el Manifest. */
    @Suppress("DEPRECATION")
    private fun vibrar(milisegundos: Long) {
        val v = vibrador ?: return
        if (!v.hasVibrator()) return   // tablets/emuladores sin motor de vibración
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0 o superior: forma moderna
            v.vibrate(VibrationEffect.createOneShot(milisegundos, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // Android 7 (minSdk 24): forma antigua
            v.vibrate(milisegundos)
        }
    }
}
