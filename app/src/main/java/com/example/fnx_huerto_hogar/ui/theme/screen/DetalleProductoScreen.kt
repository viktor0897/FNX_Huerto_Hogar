package com.example.fnx_huerto_hogar.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fnx_huerto_hogar.data.repository.CartRepository
import com.example.fnx_huerto_hogar.data.repository.ProductRepository
import com.example.fnx_huerto_hogar.ui.theme.GrayBackground
import com.example.fnx_huerto_hogar.ui.theme.GreenPrimary
import com.example.fnx_huerto_hogar.ui.theme.YellowAccent
import com.example.fnx_huerto_hogar.ui.theme.viewModel.DPViewModelFactory
import com.example.fnx_huerto_hogar.ui.theme.viewModel.DetalleProductoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productId: String,
    repository: ProductRepository = remember { ProductRepository() },
    cartRepository: CartRepository = CartRepository(),
    onDismiss: () -> Unit
) {
    // Factory
    val viewModel: DetalleProductoViewModel = viewModel(
        factory = DPViewModelFactory(
            repository = repository,
            cartRepository = cartRepository,
            productId = productId
        ),
        key = productId

    )

    // Estado del ViewModel
    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val quantity by viewModel.quantity.collectAsState()
    val addToCart by viewModel.addingToCart.collectAsState()

    // Estado del modal
    val sheetState = rememberModalBottomSheetState()

    // Snackbar de mensajes
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {

        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,  //Cerrar al hacer click fuera
        sheetState = sheetState,
        modifier = Modifier.fillMaxSize(),
        containerColor = GrayBackground,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = GreenPrimary
            )
        }
    ) {
        if (isLoading) {
            // Loading State
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 4.dp,
                        color = GreenPrimary
                    )
                    Text(
                        "Cargando producto...",
                        color = GreenPrimary
                    )
                }
            }
        } else if (product == null) {
            // Producto no encontrado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Producto no encontrado",
                        style = MaterialTheme.typography.titleMedium,
                        color = GreenPrimary
                    )
                    Button(
                        onClick = onDismiss,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = GreenPrimary
                        )
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Header con botón cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detalle del Producto",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Cerrar",
                            tint = GreenPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Imagen
                AsyncImage(
                    model = product!!.image,
                    contentDescription = product!!.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre y precio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product!!.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    // Precio
                    Box(
                        modifier = Modifier
                            .background(
                                color = YellowAccent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$${product!!.price.toInt()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de cantidad
                QuantitySelector(
                    quantity = quantity,
                    onIncrease = viewModel::raiseQuantity,
                    onDecrease = viewModel::lowerQuantity,
                    maxStock = product!!.stock
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Detalles del producto
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Información del Producto",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = GreenPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    DetailRow("Categoría", product!!.category)
                    DetailRow("Origen", product!!.origin)
                    DetailRow("Medida", product!!.measure)
                    DetailRow(
                        "Stock disponible",
                        if (product!!.stockAvailable()) "${product!!.stock} unidades"
                        else "Sin Stock"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Descripción
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = GreenPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = product!!.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón agregar al carrito
                Button(
                    onClick = { viewModel.addToCart(1L) },  // Cambia 1L por tu usuarioId real
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = product!!.stock > 0 && !addToCart,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = GreenPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (addToCart) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = if (product!!.stock > 0) "Agregar $quantity al carrito - $${(product!!.price * quantity).toInt()}"
                            else "Sin Stock",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Snackbar para mensajes
    if (message.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Snackbar(
                action = {
                    IconButton(onClick = { viewModel.cleanMessage() }) {
                        Icon(Icons.Outlined.Close, "Cerrar")
                    }
                }
            ) {
                Text(message)
            }
        }
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    maxStock: Int
){
    Column {
        Text(
            text = "Cantidad",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = GreenPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            //Boton Disminuir
            IconButton(
                onClick = onDecrease,
                enabled = quantity > 1
            ){
                Icon(
                    Icons.Outlined.Remove,
                    "Disminuir",
                    tint = if (quantity >1) GreenPrimary else Color.Gray
                )
            }

            //Cantidad Actual
            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center,
                color = GreenPrimary
            )

            //Boton Aumentar
            IconButton(
                onClick = onIncrease,
                enabled = quantity < maxStock
            ){
                Icon(
                    Icons.Outlined.Add,
                    "Aumentar",
                    tint = if (quantity < maxStock) GreenPrimary
                           else Color.Gray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            //Stock Máximo
            Text(
                text = "Máx: $maxStock",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical= 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
