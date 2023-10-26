package mimsoft.io.integrate.jowi

import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.order.Order
import mimsoft.io.repository.BaseEnums

object JowiMapper {
    suspend fun toJowiDto(order: Order): CreateJowiOrder {
        val createdOrder = CreateJowiOrder();
        createdOrder.api_key = JowiConst.API_KEY
        createdOrder.sig = JowiConst.sig
        createdOrder.restaurant_id = order.branch?.jowiId
        createdOrder.order = JowiOrder(
            restaurant_id = order.branch?.jowiId,
            address = order.address?.description,
            phone = order.user?.phone,
            contact = order.user?.firstName,
            description = order.comment,
            order_type = if (order.serviceType == BaseEnums.DELIVERY) {
                0
            } else {
                1
            },
            amount_order = getAmount(order)?.toLong(),
            payment_method = if (order.paymentMethod?.id?.toInt() == 0) {
                0
            } else {
                1
            },
            payment_type = if (order.paymentMethod?.id?.toInt() == 0) {
                0
            } else {
                1
            },
//            delivery_price =
//            delivery_time =
//            discount_sum = order.totalDiscount?.toDouble()
            courses = getCourses(order.products)
        )
        return createdOrder;

    }

    private suspend fun getAmount(order: Order): Double? {
        var amount = 0.0
        if (order.products != null) {
            for (p in order.products) {
                if (p.extras != null) {
                    for (extra in p.extras!!) {
                        amount += extra.jowiId?.let { JowiService.getCourseById(it) }?.priceForOnlineOrder!!
                    }
                }
                if (p.option != null) {
                    amount += p.option!!.jowiId?.let { JowiService.getCourseById(it) }?.priceForOnlineOrder!!
                }
            }
        }
        return amount* order.productCount!!
    }

    private fun getCourses(products: List<CartItem>?): List<Course>? {
        val courses = ArrayList<Course>()
        if (products != null) {
            for (p in products) {
                if (p.extras != null) {
                    for (extra in p.extras!!) {
                        courses.add(
                            Course(
                                course_id = extra?.jowiId,
                                count = p.count,
                            )
                        )
                    }
                }
                if (p.option != null) {
                    courses.add(
                        Course(
                            course_id = p.option?.jowiId,
                            count = p.count,
                        )
                    )
                }
            }
        }
//        if (products != null) {
//            for (p in products) {
//                if (p.option == null) {
//                    courses.add(
//                        Course(
//                            course_id = p.product?.jowiId,
//                            count = p.count,
//                        )
//                    )
//                } else {
//                    courses.add(
//                        Course(
//                            course_id = p.option!!.jowiId,
//                            count = p.count,
//                        )
//                    )
//                }
//            }
//        }
        return courses;
    }
}