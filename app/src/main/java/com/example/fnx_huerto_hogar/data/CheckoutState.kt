package com.example.fnx_huerto_hogar.data

import android.location.Address

data class CheckoutState(
    val deliveryType: DeliveryType = DeliveryType.HOME_DELIVERY,
    val deliveryAddress: String = "",
    val pickupStore: String = "Sucursal",
    val paymentMethod: PaymentMethod = PaymentMethod.CREDIT_CARD,
    val recipientName: String = "",
    val recipientPhone: String = "",
    val deliveryInstruction: String = "",
    val isLoading: Boolean = false,
    val isConfirmed: Boolean = false,
    val errorMessage: String = ""
)

enum class DeliveryType {
    HOME_DELIVERY,
    STORE_PICKUP
}

enum class PaymentMethod{
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL
}

