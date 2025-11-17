package com.example.fnx_huerto_hogar.data.repository

import com.example.fnx_huerto_hogar.R
import com.example.fnx_huerto_hogar.data.model.Product
import com.example.fnx_huerto_hogar.data.model.ProductCategory
import kotlinx.coroutines.delay

class ProductRepository (

) {
    //Obtener todos
    suspend fun getAllProducts(): List<Product>{
        delay(1500)
        return products.toList()
    }

    //Obtener por Id
    suspend fun getProductById(producId: String): Product? {
        delay(1500)
        return products.find { it.id == producId }
    }

    //Obtener por categoría
    suspend fun getProductsByCategory(category: ProductCategory): List<Product>{
        delay(1500)
        return products.filter {it.category == category}
    }

    //Actualizar Stock
    suspend fun updateStock(productId: String, newStock: Int){
        delay(1000)
        val index = products.indexOfFirst { it.id == productId }
        if (index != -1){
            products[index] = products[index].copy(stock = newStock)
        }
    }

    //Reducir Stock
    suspend fun reduceStock(productId: String, quantity: Int): Boolean{
        delay(100)
        val product = products.find { it.id == productId }
        return if (product != null && product.stockSufficient(quantity)){
            val newStock = product.stock - quantity
            updateStock(productId, newStock)
            true
        }else{
            false
        }
    }

    //Verificamos si hay disponible
    suspend fun stockAvailable(productId: String, quantity: Int): Boolean{
        delay(100)
        val product = products.find { it.id == productId }
        return product?.stockSufficient(quantity)?:false
    }

    private val products = mutableListOf<Product>(
        Product(
            id = "001",
            name = "Manzanas Fuji",
            price = 1200.0,
            category = ProductCategory.FRUTAS,
            description = "Manzanas Fuji, frescas, dulces y crujientes.",
            stock = 50,
            image = R.drawable.manzana_fuji,
            measure = "Kilo",
            origin = "Valle del Maule"
        ),
        Product(
            id = "002",
            name = "Naranjas Valencia",
            price = 1000.0,
            category = ProductCategory.FRUTAS,
            description = "Jugosas y ricas en vitamina C.",
            stock = 100,
            image = R.drawable.naranja_valencia,
            measure = "Kilo",
            origin = "Región de Valparaiso"
        ),
        Product(
            id = "003",
            name = "Zanahorias Orgánicas",
            price = 900.0,
            category = ProductCategory.ORGANICOS,
            description = "Zanahorias cultivadas sin pesticidas.",
            stock = 100,
            image = R.drawable.zanahorias,
            measure = "Kilo",
            origin = "Región de O'Higgins"
        ),
        Product(
            id = "004",
            name = "Miel Orgánica",
            price = 5000.0,
            category = ProductCategory.ORGANICOS,
            description = "Miel pura y orgánica producida por agricultores locales.",
            stock = 20,
            image = R.drawable.miel,
            measure = "Frasco",
            origin = "Región de la Araucanía"
        ),
        Product(
            id = "005",
            name = "Leche entera",
            price = 1200.0,
            category = ProductCategory.ORGANICOS,
            description = "Leche entera de vaca criada en granja local.",
            stock = 76,
            image = R.drawable.leche_entera,
            measure = "Litro",
            origin = "Región de los Lagos"
        ),
        Product(
            id = "006",
            name = "Plátanos",
            price = 800.0,
            category = ProductCategory.FRUTAS,
            description = "Plátano fresco y maduro.",
            stock = 90,
            image = R.drawable.platano,
            measure = "Kilo",
            origin = "Región de Valparaiso"
        ),
        Product(
            id = "007",
            name = "Espinacas Frescas",
            price = 800.0,
            category = ProductCategory.VERDURAS,
            description = "Espinacas frescas y nutritivas.",
            stock = 150,
            image = R.drawable.espinacas,
            measure = "Bolsa",
            origin = "Región Metropolitana"
        )
    )
}