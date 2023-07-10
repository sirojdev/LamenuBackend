package mimsoft.io.routing.v1

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.client.branch.routeToClientBranches
import mimsoft.io.client.menu.routeToMenu
import mimsoft.io.client.order.routeToOrderClient
import mimsoft.io.client.table.routeToClientTable
import mimsoft.io.features.address.routeToAddress
import mimsoft.io.features.book.routeToBook
import mimsoft.io.features.favourite.routeToFavourites
import mimsoft.io.routing.v1.client.*

fun Route.routeToClient() {

    route("client/auth") {
        routeToClientAuth()
    }
    route("client") {
        routeToMenu()
        routeToClientProduct()
        routeToClientTable()
        routeToClientBranches()
        routeToClientCategory()
        routeToClientStory()
        clientStoryInfoRoute()

        authenticate("user") {
            routeToClientDevice()
            routeToClientProfile()
            routeToAddress()
            routeToOrderClient()
            routeToBook()
            routeToFavourites()
            routeToClientOrderInfo()
            routeToClientOrderHistory()
            routeToClientCheckout()
        }
    }
}