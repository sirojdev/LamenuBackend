package mimsoft.io.integrate.iiko.model

data class IIkoOrganization(
    val correlationId: String? = null,
    val organizations: List<Organization>? = null,
)

data class Organization(
    val responseType: String? = null,
    val id: String? = null,
    val name: String? = null,
    val code: String? = null,
    val externalData:List<ExternalData>?=null
)

data class IIkoOrganizationsRequest(
    val organizationIds: List<String>? = null,
    val returnAdditionalInfo: Boolean? = true,
    val includeDisabled: Boolean? = true,
    val returnExternalData: List<String>? = null
)

data class ExternalData(
    val key:String?=null,
    val value:String?=null
)