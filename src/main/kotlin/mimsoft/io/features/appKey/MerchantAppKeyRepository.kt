package mimsoft.io.features.appKey

import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object MerchantAppKeyRepository {
    fun add(appKeyDto: MerchantAppKeyDto) {
        // keyin tekshiramiz
        val query ="insert into $MERCHANT_APP_KEY_TABLE  (merchant_id,app_id) " +
                "values (${appKeyDto.merchantId},${appKeyDto.appKey}"

    }

    val repository: BaseRepository = DBManager

}