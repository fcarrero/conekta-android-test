package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import io.conekta.CustomersApi
import io.conekta.model.Customer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
    Button(onClick =  {
        call()
    }) {

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}

fun call() {
    // Ejecutar la solicitud de red en un hilo de fondo
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val api = CustomersApi()
            api.setApiKey("key_xxxx")
            val customer = Customer().apply {
                name = "android"
                email = "mauriciocarrero15@gmail.com"
                phone = "+573143159054"
            }

            // Realizar la solicitud de red en un hilo de fondo
            val response = api.createCustomer(customer, "es", null)

            // Si necesitas actualizar la UI, cambia al hilo principal
            withContext(Dispatchers.Main) {
                // Aquí puedes hacer algo con la respuesta, por ejemplo, mostrar un mensaje
                Log.d("API Response", "Customer created successfully: $response")
            }

        } catch (ex: Exception) {
            Log.e("API Error", "Error creating customer", ex)
        }
    }
}