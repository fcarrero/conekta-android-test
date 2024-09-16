package com.example.myapplication

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.conekta.CustomersApi
import io.conekta.model.Customer

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.myapplication", appContext.packageName)
    }
    @Test
    fun createCustomer(){
        val api =  CustomersApi()
        api.setApiKey("key_xxx")
        val customer = Customer()
        customer.name = "android2"
        customer.email = "mauriciocarrero15@gmail.com"
        customer.phone = "+573143159054"
        val response = api.createCustomer(customer, "es", null)

        assertNotNull(response.createdAt)
    }
}