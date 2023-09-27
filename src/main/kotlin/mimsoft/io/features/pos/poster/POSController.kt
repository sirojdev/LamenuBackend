package mimsoft.io.features.pos.poster

import mimsoft.io.entities.seles.repository.MerchantRepositoryImp
import mimsoft.io.integrate.jowi.Jowi

object POSController {
    suspend fun getPostFromBranch( id : Long) : POSService {
        val merchant = MerchantRepositoryImp.get(id)?.post

        return when(merchant){
            "jowi"->{
                Jowi(id)
            }
            else -> Jowi(id)
        }


    }

}