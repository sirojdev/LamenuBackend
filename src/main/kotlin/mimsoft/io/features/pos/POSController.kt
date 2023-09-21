package mimsoft.io.features.pos

import mimsoft.io.entities.seles.repository.MerchantRepositoryImp
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.payment.payment_integration.IntegrationService
import mimsoft.io.features.pos.jowi.Jowi
import mimsoft.io.features.pos.jowi.JowiService
import mimsoft.io.features.product.ProductDto

object POSController {

   suspend fun getPostFromBranch( id : Long) : POSService{
        val merchant = MerchantRepositoryImp.get(id)?.post

      return when(merchant){
           "jowi"->{
               Jowi(id)
           }
           else -> Jowi(id)
       }


    }

}