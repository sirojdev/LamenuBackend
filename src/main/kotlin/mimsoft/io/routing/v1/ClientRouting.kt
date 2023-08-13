package mimsoft.io.routing.v1

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.client.branch.routeToClientBranches
import mimsoft.io.client.menu.routeToMenu
import mimsoft.io.client.order.routeToClientOrder
import mimsoft.io.client.table.routeToClientTable
import mimsoft.io.features.address.routeToAddress
import mimsoft.io.features.book.routeToBook
import mimsoft.io.features.client_promo.routeToClientPromo
import mimsoft.io.features.favourite.routeToFavourites
import mimsoft.io.routing.v1.client.*
import mimsoft.io.routing.v1.device.routeToDevice

fun Route.routeToClient() {

    route("client/auth") {
        authenticate("device"){
            routeToSMS()
        }
        routeToClientAuth()
    }
    route("client") {
        routeToDevice()
        routeToMenu()
        routeToClientProduct()
        routeToClientTable()
        routeToClientBranches()
        routeToClientCategory()
        routeToClientStory()
        clientStoryInfoRoute()
        routeToClientBasket()
        routeToAnnouncement()
        routeToCategoryByGroup()
        routeToClientDevice()
        routeToClientCart()
        routeToPaymentTypes()
        authenticate("user") {
            routeToClientPromo()
            routeToClientProfile()
            routeToAddress()
            routeToClientOrder()
            routeToBook()
            routeToFavourites()
            routeToClientOrderInfo()
//            routeToClientOrderHistory()
            routeToClientCheckout()
            routeToNotification()
            routeToClientVisit()
        }
    }
}