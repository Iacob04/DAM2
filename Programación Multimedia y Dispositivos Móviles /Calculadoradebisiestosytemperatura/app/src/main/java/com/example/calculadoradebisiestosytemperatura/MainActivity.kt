package com.example.calculadoradebisiestosytemperatura

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculadoradebisiestosytemperatura.ui.theme.CalculadoraDeBisiestosYTemperaturaTheme

class MainActivity : ComponentActivity() {
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
                            title = { Text("Calculadora Bisiestos y Temperatura") }
                        )
                    }
                ) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        onNavigateToLeapYear = {
                            startActivity(Intent(this, LeapYearActivity::class.java))
                        },
                        onNavigateToTemperature = {
                            startActivity(Intent(this, TemperatureActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onNavigateToLeapYear: () -> Unit,
    onNavigateToTemperature: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Seleccione una opción",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onNavigateToLeapYear,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Calculadora de Bisiestos", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToTemperature,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Calculadora de Temperaturas", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CalculadoraDeBisiestosYTemperaturaTheme {
        MainScreen(
            onNavigateToLeapYear = {},
            onNavigateToTemperature = {}
        )
    }
}
