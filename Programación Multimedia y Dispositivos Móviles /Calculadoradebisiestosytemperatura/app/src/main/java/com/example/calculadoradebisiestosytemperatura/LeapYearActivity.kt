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

class LeapYearActivity : ComponentActivity() {
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
                            title = { Text("Calculadora de Bisiestos") },
                            navigationIcon = {
                                TextButton(onClick = { finish() }) {
                                    Text("← Volver")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    LeapYearScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LeapYearScreen(modifier: Modifier = Modifier) {
    var yearInput by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf<String?>(null) }
    var isLeap by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = yearInput,
            onValueChange = { yearInput = it },
            label = { Text("Introduce un año") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val year = yearInput.toIntOrNull()
                if (year != null) {
                    val leap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
                    isLeap = leap
                    resultText = if (leap) {
                        "¡El año $year es bisiesto!"
                    } else {
                        "El año $year no es bisiesto."
                    }
                } else {
                    resultText = "Por favor, introduce un año válido."
                    isLeap = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Comprobar Año")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (resultText != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when (isLeap) {
                        true -> MaterialTheme.colorScheme.primaryContainer
                        false -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                )
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
