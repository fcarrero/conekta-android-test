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
import io.conekta.OrdersApi
import io.conekta.TokensApi
import io.conekta.model.ChargeRequest
import io.conekta.model.ChargeRequestPaymentMethod
import io.conekta.model.Customer
import io.conekta.model.OrderRequest
import io.conekta.model.OrderRequestCustomerInfo
import io.conekta.model.Product
import io.conekta.model.Token
import io.conekta.model.TokenCard
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
        callCreateToken()
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
fun callCreateToken() {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val apiKey = "key_xxx"
            val api = TokensApi()
            api.setApiKey(apiKey)

            // Crear la tarjeta
            val card = TokenCard().apply {
                cvc = "123"
                name = "fran carrero"
                expYear = "2028"
                expMonth = "12"
                number = "4242424242424242"
            }

            // Crear el token
            val token = Token()
            token.card = card

            // Llamada a la API para crear el token, asegurándote que esté en Dispatchers.IO
            val tokenResponse = withContext(Dispatchers.IO) {
                api.createToken(token, "es")
            }

            // Log en el hilo principal para mostrar el token
            withContext(Dispatchers.Main) {
                Log.d("API Response", "Token created successfully: $tokenResponse")
            }

            // Creación de la orden usando el token
            val orderApi = OrdersApi()
            orderApi.setApiKey(apiKey)

            val paymentMethod = ChargeRequestPaymentMethod().apply {
                tokenId = tokenResponse.id
                type = "card"
            }

            val chargeRequest = ChargeRequest()
            chargeRequest.paymentMethod = paymentMethod

            val charges: List<ChargeRequest> = listOf(chargeRequest)

            val products: List<Product> = listOf(
                Product().apply {
                    quantity = 1
                    unitPrice = 5000
                    name = "test"
                }
            )

            val orderRequestCustomerInfo = OrderRequestCustomerInfo().apply {
                name = "android"
                email = "mauriciocarrero15@gmail.com"
                phone = "+573143159054"
            }

            val request = OrderRequest().apply {
                this.charges = charges
                this.currency = "MXN"
                this.customerInfo = orderRequestCustomerInfo
                this.lineItems = products
            }

            // Llamada a la API para crear la orden, asegurándote que esté en Dispatchers.IO
            val orderResponse = withContext(Dispatchers.IO) {
                orderApi.createOrder(request, "es", null)
            }

            // Log en el hilo principal para mostrar la orden
            withContext(Dispatchers.Main) {
                Log.d("API Response", "Order created successfully: $orderResponse")
            }

        } catch (ex: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("API Error", "Error creating customer or order", ex)
            }
        }
    }
}
