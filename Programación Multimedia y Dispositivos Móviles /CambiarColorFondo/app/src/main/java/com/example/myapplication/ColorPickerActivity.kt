package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class ColorPickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Selecciona un color", modifier = Modifier.padding(bottom = 24.dp))

                        Button(
                            onClick = {
                                val intent = Intent().apply {
                                    putExtra("COLOR_HEX", "#F44336")
                                    putExtra("COLOR_NAME", "Rojo")
                                }
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor("#F44336")))
                        ) {
                            Text(text = "Rojo")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val intent = Intent().apply {
                                    putExtra("COLOR_HEX", "#4CAF50")
                                    putExtra("COLOR_NAME", "Verde")
                                }
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor("#4CAF50")))
                        ) {
                            Text(text = "Verde")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val intent = Intent().apply {
                                    putExtra("COLOR_HEX", "#2196F3")
                                    putExtra("COLOR_NAME", "Azul")
                                }
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor("#2196F3")))
                        ) {
                            Text(text = "Azul")
                        }
                    }
                }
            }
        }
    }
}
