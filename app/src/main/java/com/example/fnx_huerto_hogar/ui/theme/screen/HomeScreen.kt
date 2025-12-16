package com.example.fnx_huerto_hogar.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fnx_huerto_hogar.R
import com.example.fnx_huerto_hogar.data.model.ProductoDestacado
import com.example.fnx_huerto_hogar.data.model.ProductosDestacadosData
import com.example.fnx_huerto_hogar.navigate.AppScreens

@Composable
fun HomeScreen(
    navController: NavController
) {
    val scrollState = rememberScrollState()

    // Imágenes del carrusel
    val imagenesCarrusel = listOf(
        R.drawable.manzana_fuji,
        R.drawable.naranja_valencia,
        R.drawable.zanahorias,
        R.drawable.platano,
        R.drawable.espinacas
    )

    // Productos destacados locales
    val productosDestacados = ProductosDestacadosData.productos

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título
        Column {
            Text(
                text = "Huerto Hogar",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 1) Carrusel de imágenes
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            items(imagenesCarrusel) { imgRes ->
                Card(
                    modifier = Modifier
                        .width(220.dp)
                        .fillMaxHeight(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Image(
                        painter = painterResource(id = imgRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // 2) Botones de navegación
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navController.navigate(AppScreens.CatalogScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver catálogo")
            }
            OutlinedButton(
                onClick = { navController.navigate(AppScreens.GeolocalizationScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nosotros (mapa y teléfonos)")
            }
            OutlinedButton(
                onClick = { navController.navigate(AppScreens.HomeScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Contacto")
            }
            OutlinedButton(
                onClick = { navController.navigate(AppScreens.HomeScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Admin")
            }
        }

        // 3) Sección de productos
        Text(
            text = "Productos Destacados",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        // Lista de productos
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            productosDestacados.forEach { producto ->
                ProductoCardLocal(
                    producto = producto,
                    onVer = { navController.navigate(AppScreens.CatalogScreen.route) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun ProductoCardLocal(
    producto: ProductoDestacado,
    onVer: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen desde drawable
            Image(
                painter = painterResource(id = producto.imagenRes),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Información del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "$${producto.precio.toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Stock: ${producto.stock}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Botón ver
            Button(onClick = onVer) {
                Text("Ver")
            }
        }
    }
}