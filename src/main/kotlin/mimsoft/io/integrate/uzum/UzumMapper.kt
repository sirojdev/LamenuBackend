package mimsoft.io.integrate.uzum

import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.order.Order
import mimsoft.io.features.product.ProductDto
import mimsoft.io.integrate.uzum.module.*

object UzumMapper {
    fun toDto(order: Order?): UzumRegister {
        val cart: Cart? = null
        val register = UzumRegister()
        register.clientId = order?.user?.id.toString()
        register.amount = order?.totalPrice?.times(100)
        register.paymentDetails = order?.comment
        register.orderNumber = order?.id.toString()
//        register.successUrl = "https://api.lamenu.uz"
        register.successUrl = " https://test-chk-api.ipt-merch.com/ "
//        register.failureUrl = "https://google.com/"
        register.failureUrl = " https://test-chk-api.ipt-merch.com/ "
        register.viewType = "REDIRECT"
        register.paymentParams = PaymentParams(
            payType = "TWO_STEP",
            phoneNumber = null,
            operationType = "PAYMENT"
        )
//        register.merchantParams = MerchantParams(
//            Cart(
//                items = listOf(
//                    UzumItems(
//                        title = order?.comment.toString(),
//                        productId = order?.id.toString(),
//                        quantity = order?.productCount,
//                        unitPrice = order?.productPrice?.toInt(),
//                        total = order?.totalPrice?.toInt(),
//                    )
//                )
//            )
//        )
        register.sessionTimeoutSecs = 900
        return register
    }
}