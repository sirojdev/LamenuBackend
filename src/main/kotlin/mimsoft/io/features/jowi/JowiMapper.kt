package mimsoft.io.integrate.jowi

import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.order.Order

object JowiMapper {
    fun toJowiDto(order: Order): CreateJowiOrder {
        val createdOrder = CreateJowiOrder();
        createdOrder.api_key = JowiConst.API_KEY
        createdOrder.sig = JowiConst.sig
        createdOrder.restaurant_id = order.branch?.jowiPosterId
        createdOrder.order = JowiOrder(
            restaurant_id = order.branch?.jowiPosterId,
            address = order.address?.description,
            phone = order.user?.phone,
            contact = order.user?.firstName,
            description = order.comment,
            order_type = if (order.serviceType == "DELIVERY") {
                0
            } else {
                1
            },
            amount_order = 999*3,
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
                if(p.option!=null){
                    courses.add(
                        Course(
                            course_id = p.option?.jowiId,
                            count = p.count,
                        )
                    )
                }
            }
        }
        return courses;
    }
}