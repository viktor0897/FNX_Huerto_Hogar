package com.example.fnx_huerto_hogar.ui.theme.screen

import android.R
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fnx_huerto_hogar.data.CheckoutState
import com.example.fnx_huerto_hogar.data.DeliveryType
import com.example.fnx_huerto_hogar.data.model.CartItem
import com.example.fnx_huerto_hogar.ui.theme.GrayBackground
import com.example.fnx_huerto_hogar.ui.theme.GreenPrimary
import com.example.fnx_huerto_hogar.ui.theme.viewModel.CheckoutViewModel
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartItem: List<CartItem>,
    totalPrice: Double
){
    val viewModel: CheckoutViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    //Inicializamos con los datos del carrito
    LaunchedEffect(Unit) {
        viewModel.cartItems = cartItem
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

            //Resumen
            OrderSummarySection(cartItem, totalPrice)

            //Tipo de entrega
            DeliveryTypeSection(
                DeliveryType = state.deliveryType,
                onDeliveryTypeSelected = viewModel::updateDeliveryAddress
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Botton Confirmar
            ConfirmOrderButton(
                isLoading = state.isLoading,
                isConfirmed = state.isConfirmed,
                onConfirm = {
                    viewModel.confirmOrder {
                        navController.navigate("Order_confirmation")
                    }
                }
            )
            //Mostramos error si existe
            if (state.errorMessage.isNotEmpty()){
                ErrorMessage(
                    message = state.errorMessage,
                    onDimiss = viewModel::clearError
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
){
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

            //Lista de productos
            LazyColumn(
                modifier = Modifier.heightIn(max= 200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems){item ->
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
            )
        }
    }
}