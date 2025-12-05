package com.example.fnx_huerto_hogar.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fnx_huerto_hogar.data.DeliveryType
import com.example.fnx_huerto_hogar.data.PaymentMethod
import com.example.fnx_huerto_hogar.data.model.CartItem
import com.example.fnx_huerto_hogar.ui.theme.*
import com.example.fnx_huerto_hogar.ui.theme.viewModel.CartViewModel
import com.example.fnx_huerto_hogar.ui.theme.viewModel.CheckoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController
) {
    //ViewModel del carrtio
    val cartViewModel: CartViewModel= viewModel()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()

    val viewModel: CheckoutViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    // Inicializamos con los datos del carrito
    LaunchedEffect(Unit) {
        viewModel.cartItems = cartItems
        viewModel.totalPrice = totalPrice
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Finalizar Compra",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Volver",
                            tint = GreenPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = GrayBackground
                )
            )
        },
        containerColor = GrayBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Resumen
            OrderSummarySection(cartItems, totalPrice)

            // Tipo de entrega
            DeliveryTypeSection(
                deliveryType = state.deliveryType,
                onDeliveryTypeSelected = viewModel::setDeliveryType // CORREGIDO
            )

            // Dirección de entrega (solo si es delivery)
            if (state.deliveryType == DeliveryType.HOME_DELIVERY) {
                DeliveryAddressSection(
                    address = state.deliveryAddress,
                    onAddressChange = viewModel::updateDeliveryAddress
                )
            } else {
                StorePickupSection()
            }

            // Datos del destinatario
            RecipientInfoSection(
                name = state.recipientName,
                phone = state.recipientPhone,
                onNameChange = { name ->
                    viewModel.updateRecipientInfo(name, state.recipientPhone)
                },
                onPhoneChange = { phone ->
                    viewModel.updateRecipientInfo(state.recipientName, phone)
                }
            )

            // Método de pago
            PaymentMethodSection(
                paymentMethod = state.paymentMethod,
                onPaymentMethodSelected = viewModel::setPaymentMethod
            )

            // Instrucciones de entrega (opcional)
            DeliveryInstructionsSection(
                instructions = state.deliveryInstruction,
                onInstructionsChange = viewModel::updateDeliveryInstruction
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Confirmar
            ConfirmOrderButton(
                isLoading = state.isLoading,
                isConfirmed = state.isConfirmed,
                onConfirm = {
                    viewModel.confirmOrder {
                        navController.navigate("order_confirmation")
                    }
                }
            )

            // Mostramos error si existe
            if (state.errorMessage.isNotEmpty()) {
                ErrorMessage(
                    message = state.errorMessage,
                    onDismiss = viewModel::clearError
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun OrderSummarySection(
    cartItems: List<CartItem>,
    totalPrice: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Resumen del pedido",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )

            // Lista de productos
            LazyColumn(
                modifier = Modifier.heightIn(max = 200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.quantity} x ${item.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "$${item.subtotal}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .padding(vertical = 12.dp),
                color = GreenSecondary
            )

            // Total
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = YellowAccent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "$${totalPrice.toInt()}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun DeliveryTypeSection(
    deliveryType: DeliveryType,
    onDeliveryTypeSelected: (DeliveryType) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Tipo de entrega",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )

            // Opción delivery a domicilio
            DeliveryTypeOption(
                title = "Despacho a Domicilio",
                description = "Recibe el pedido en tu dirección",
                icon = Icons.Outlined.LocalShipping,
                isSelected = deliveryType == DeliveryType.HOME_DELIVERY,
                onClick = { onDeliveryTypeSelected(DeliveryType.HOME_DELIVERY) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Opción de Retiro en tienda
            DeliveryTypeOption(
                title = "Retiro en tienda", // CORREGIDO: "Retino" -> "Retiro"
                description = "Retira el pedido en Tienda",
                icon = Icons.Outlined.Store,
                isSelected = deliveryType == DeliveryType.STORE_PICKUP,
                onClick = { onDeliveryTypeSelected(DeliveryType.STORE_PICKUP) }
            )
        }
    }
}

@Composable
fun DeliveryTypeOption(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GreenSecondary else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) GreenPrimary else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) GreenPrimary else Color.Black
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) GreenPrimary else Color.Gray
                    )
                }
            }

            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = GreenPrimary,
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}

@Composable
fun DeliveryAddressSection(
    address: String,
    onAddressChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp) // Agregado padding
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Dirección",
                    tint = GreenPrimary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Dirección de Entrega",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = onAddressChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color.LightGray // CORREGIDO
                ),
                placeholder = {
                    Text(
                        text = "Ingresa tu dirección completa",
                        color = Color.Gray
                    )
                },
                singleLine = false,
                minLines = 2,
                maxLines = 3
            )
        }
    }
}

@Composable
fun StorePickupSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Store,
                    contentDescription = "Sucursal",
                    tint = GreenPrimary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Sucursal de Retiro",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Sucursal Central\nAv. Principal 123, Santiago\nHorario: Lunes a Viernes 9:00 - 19:00",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun RecipientInfoSection(
    name: String,
    phone: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Datos del Destinatario",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color.LightGray
                ),
                placeholder = {
                    Text("Nombre completo", color = Color.Gray)
                },
                leadingIcon = {
                    Icon(Icons.Outlined.Person, null, tint = GreenPrimary)
                }
            )

            OutlinedTextField(
                value = phone,
                onValueChange = onPhoneChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color.LightGray
                ),
                placeholder = {
                    Text("Teléfono de contacto", color = Color.Gray)
                },
                leadingIcon = {
                    Icon(Icons.Outlined.Phone, null, tint = GreenPrimary)
                }
            )
        }
    }
}

@Composable
fun PaymentMethodSection(
    paymentMethod: PaymentMethod,
    onPaymentMethodSelected: (PaymentMethod) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Método de Pago",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Tarjeta de Crédito
            PaymentMethodOption(
                title = "Tarjeta de Crédito",
                icon = Icons.Outlined.CreditCard,
                isSelected = paymentMethod == PaymentMethod.CREDIT_CARD,
                onClick = { onPaymentMethodSelected(PaymentMethod.CREDIT_CARD) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tarjeta de Débito
            PaymentMethodOption(
                title = "Tarjeta de Débito",
                icon = Icons.Outlined.CreditCard,
                isSelected = paymentMethod == PaymentMethod.DEBIT_CARD,
                onClick = { onPaymentMethodSelected(PaymentMethod.DEBIT_CARD) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // PayPal
            PaymentMethodOption(
                title = "PayPal",
                icon = Icons.Outlined.Payment,
                isSelected = paymentMethod == PaymentMethod.PAYPAL,
                onClick = { onPaymentMethodSelected(PaymentMethod.PAYPAL) }
            )
        }
    }
}

@Composable
fun PaymentMethodOption(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GreenSecondary else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) GreenPrimary else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) GreenPrimary else Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = GreenPrimary,
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}

@Composable
fun DeliveryInstructionsSection(
    instructions: String,
    onInstructionsChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notes,
                    contentDescription = "Instrucciones",
                    tint = GreenPrimary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Instrucciones de Entrega (Opcional)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = instructions,
                onValueChange = onInstructionsChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color.LightGray
                ),
                placeholder = {
                    Text(
                        text = "Ej: Llamar antes de llegar, timbre rojo, etc.",
                        color = Color.Gray
                    )
                },
                singleLine = false,
                minLines = 2,
                maxLines = 3
            )
        }
    }
}

@Composable
fun ConfirmOrderButton(
    isLoading: Boolean,
    isConfirmed: Boolean,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = GreenPrimary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Procesando pago...",
                color = GreenPrimary
            )
        } else if (isConfirmed) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Confirmado",
                tint = GreenPrimary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¡Pedido Confirmado!",
                style = MaterialTheme.typography.titleMedium,
                color = GreenPrimary,
                fontWeight = FontWeight.Bold
            )
        } else {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Confirmar Pedido",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = "Al confirmar, aceptas nuestros términos y condiciones",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE5E5))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Error,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Cerrar",
                    tint = Color.Red
                )
            }
        }
    }
}