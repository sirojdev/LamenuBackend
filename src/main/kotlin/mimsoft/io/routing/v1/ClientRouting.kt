package mimsoft.io.routing.v1

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.features.address.routeToAddress
import mimsoft.io.features.book.routeToBook
import mimsoft.io.features.client_promo.routeToClientPromo
import mimsoft.io.features.favourite.routeToFavourites
import mimsoft.io.routing.v1.client.*
import mimsoft.io.routing.v1.client.routeToClientOrder

fun Route.routeToClient() {

  route("client/auth") { routeToClientAuth() }

  route("client") {
    routeToClientDevice()
    routeToClientProduct()
    routeToClientTable()
    routeToClientBranches()
    routeToClientCategory()
    routeToClientStory()
    routeToClientBasket()
    routeToCategoryByGroup()
    routeToClientCart()
    routeToPaymentTypes()
    routeToClientNews()
    routeToClientDineIn()
    authenticate("update-phone") { updatePhoneRoute() }
    authenticate("user") {
      routeToClientPromo()
      routeToClientProfile()
      routeToAddress()
      routeToClientOrder()
      routeToBook()
      routeToFavourites()
      routeToClientOrderInfo()
      routeToNotification()
      routeToClientVisit()
    }
  }
}