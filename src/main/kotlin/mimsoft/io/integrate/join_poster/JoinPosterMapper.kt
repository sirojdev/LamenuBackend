package mimsoft.io.integrate.join_poster

import mimsoft.io.features.order.Order
import mimsoft.io.features.order.Order.Companion.DELIVERY
import mimsoft.io.integrate.join_poster.model.ClientAddress
import mimsoft.io.integrate.join_poster.model.PosterFoodModel
import mimsoft.io.integrate.join_poster.model.PosterOrderModel

object JoinPosterMapper {

    fun toPosterOrder(order: Order?): PosterOrderModel? {
        if (order == null) return null
        return PosterOrderModel(
            id = order.id,
            products = order.products?.map { food -> PosterFoodModel(id = food.product?.joinPosterId, count = food.count?.toLong()) },
            spotId = order.branch?.joinPosterId,
            name = "${order.user?.firstName} ${order.user?.lastName}",
            phone = order.user?.phone,
            serviceMode = if (order.serviceType == DELIVERY) 3 else 2,
            paymentMethodId = if(order.paymentMethod?.id == 1L) 1 else 2,
            comment = order.comment,
            address = ClientAddress(address1 = order.address?.description)
        )
    }
}
