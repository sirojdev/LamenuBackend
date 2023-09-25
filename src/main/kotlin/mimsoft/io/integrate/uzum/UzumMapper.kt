package mimsoft.io.integrate.uzum

import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.order.Order
import mimsoft.io.features.product.ProductDto
import mimsoft.io.integrate.uzum.module.*

object UzumMapper {
    fun toDto(order: Order?) {
        val cart: Cart? = null
        val register = UzumRegister()
        register.clientId = order?.user?.id.toString()
        register.amount = order?.totalPrice?.times(100)
        register.paymentDetails = order?.comment
        register.orderNumber = order?.id.toString()
        register.successUrl = "https://lamenu.uz"
        register.failureUrl = "https://lamenu.uz"
        register.viewType = "WEB_VIEW"
        register.paymentParams = PaymentParams(
            payType = "TWO_STEP",
            phoneNumber = null,
            operationType = "PAYMENT"
        )
        register.merchantParams = MerchantParams(
            Cart(
                items = listOf(
                    UzumItems(
                        title = order?.comment.toString(),
                        productId = order?.id.toString(),
                        quantity = order?.productCount,
                        unitPrice = order?.productPrice?.toInt(),
                        total = order?.totalPrice?.toInt(),
                    )
                )
            )
        )
        register.sessionTimeoutSecs = 900

    }
}