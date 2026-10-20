package com.example.calculadoradebisiestosytemperatura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.calculadoradebisiestosytemperatura.ui.theme.CalculadoraDeBisiestosYTemperaturaTheme

enum class TempUnit(val label: String) {
    CELSIUS("Celsius (°C)"),
    FAHRENHEIT("Fahrenheit (°F)"),
    KELVIN("Kelvin (K)")
}

class TemperatureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraDeBisiestosYTemperaturaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        @OptIn(ExperimentalMaterial3Api::class)
                        TopAppBar(
                            title = { Text("Calculadora de Temperaturas") },
                            navigationIcon = {
                                TextButton(onClick = { finish() }) {
                                    Text("← Volver")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    TemperatureScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TemperatureScreen(modifier: Modifier = Modifier) {
    var tempInput by remember { mutableStateOf("") }
    var sourceUnit by remember { mutableStateOf(TempUnit.CELSIUS) }
    var targetUnit by remember { mutableStateOf(TempUnit.FAHRENHEIT) }
    var resultText by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = tempInput,
            onValueChange = { tempInput = it },
            label = { Text("Temperatura a convertir") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Unidad de origen:", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TempUnit.entries.forEach { unit ->
                SelectableChip(
                    text = unit.label,
                    selected = sourceUnit == unit,
                    onClick = { sourceUnit = unit }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Unidad de destino:", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TempUnit.entries.forEach { unit ->
                SelectableChip(
                    text = unit.label,
                    selected = targetUnit == unit,
                    onClick = { targetUnit = unit }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val value = tempInput.toDoubleOrNull()
                if (value != null) {
                    val celsius = when (sourceUnit) {
                        TempUnit.CELSIUS -> value
                        TempUnit.FAHRENHEIT -> (value - 32) * 5 / 9
                        TempUnit.KELVIN -> value - 273.15
                    }

                    val result = when (targetUnit) {
                        TempUnit.CELSIUS -> celsius
                        TempUnit.FAHRENHEIT -> celsius * 9 / 5 + 32
                        TempUnit.KELVIN -> celsius + 273.15
                    }

                    resultText = "Resultado: %.2f %s".format(result, when(targetUnit) {
                        TempUnit.CELSIUS -> "°C"
                        TempUnit.FAHRENHEIT -> "°F"
                        TempUnit.KELVIN -> "K"
                    })
                } else {
                    resultText = "Por favor, introduce un número válido."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convertir Temperatura")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (resultText != null) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = resultText!!,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun SelectableChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val shortLabel = text.substringBefore(" ")
    Button(
        onClick = onClick,
        colors = if (selected) {
            ButtonDefaults.buttonColors()
        } else {
            ButtonDefaults.outlinedButtonColors()
        },
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(shortLabel, style = MaterialTheme.typography.bodySmall)
    }
}
