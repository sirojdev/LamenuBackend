package mimsoft.io.features.jowi



data class CreateJowiOrder(
    var api_key:String?=null,
    var sig:String?=null,
    var restaurant_id: String?=null,
    var order:JowiOrder?=null
){

}
data class JowiOrder (
    var restaurant_id:String?=null,
    var address:String?=null,
    var phone:String?=null,
    var contact:String?=null,
    var description:String?=null,
    var people_count:Int?=null,
    var order_type:Int?=null,
    var amount_order:Long?=null,
    var payment_method:Int?=null,
    var payment_type:Int?=null,
    var delivery_time:String?=null,
    var delivery_price:Long?=null,
    var discount_sum:Long?=null,
    var courses:List<Course>?=null
)

data class Course(
    var course_id:String?=null,
    var count:Int?=null,
    var price:Long?=null,
    var description:String?=null,
)