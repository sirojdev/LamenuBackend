# SettingsIntegrationsApi

All URIs are relative to *http://127.0.0.1:9000/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**merchantSettingsAppIdGet**](SettingsIntegrationsApi.md#merchantSettingsAppIdGet) | **GET** /merchant/settings/app/{id} |  |
| [**merchantSettingsAppPost**](SettingsIntegrationsApi.md#merchantSettingsAppPost) | **POST** /merchant/settings/app |  |
| [**merchantSettingsBadgeIdDelete**](SettingsIntegrationsApi.md#merchantSettingsBadgeIdDelete) | **DELETE** /merchant/settings/badge/{id} |  |
| [**merchantSettingsBadgeIdGet**](SettingsIntegrationsApi.md#merchantSettingsBadgeIdGet) | **GET** /merchant/settings/badge/{id} |  |
| [**merchantSettingsBadgePost**](SettingsIntegrationsApi.md#merchantSettingsBadgePost) | **POST** /merchant/settings/badge |  |
| [**merchantSettingsBadgePut**](SettingsIntegrationsApi.md#merchantSettingsBadgePut) | **PUT** /merchant/settings/badge |  |
| [**merchantSettingsBadgesGet**](SettingsIntegrationsApi.md#merchantSettingsBadgesGet) | **GET** /merchant/settings/badges |  |
| [**merchantSettingsCashbackGet**](SettingsIntegrationsApi.md#merchantSettingsCashbackGet) | **GET** /merchant/settings/cashback |  |
| [**merchantSettingsCashbackIdDelete**](SettingsIntegrationsApi.md#merchantSettingsCashbackIdDelete) | **DELETE** /merchant/settings/cashback/{id} |  |
| [**merchantSettingsCashbackIdGet**](SettingsIntegrationsApi.md#merchantSettingsCashbackIdGet) | **GET** /merchant/settings/cashback/{id} |  |
| [**merchantSettingsCashbackPost**](SettingsIntegrationsApi.md#merchantSettingsCashbackPost) | **POST** /merchant/settings/cashback |  |
| [**merchantSettingsCashbackPut**](SettingsIntegrationsApi.md#merchantSettingsCashbackPut) | **PUT** /merchant/settings/cashback |  |
| [**merchantSettingsDeliveryIdGet**](SettingsIntegrationsApi.md#merchantSettingsDeliveryIdGet) | **GET** /merchant/settings/delivery/{id} |  |
| [**merchantSettingsDeliveryPost**](SettingsIntegrationsApi.md#merchantSettingsDeliveryPost) | **POST** /merchant/settings/delivery |  |
| [**merchantSettingsPaymentIdGet**](SettingsIntegrationsApi.md#merchantSettingsPaymentIdGet) | **GET** /merchant/settings/payment/{id} |  |
| [**merchantSettingsPaymentPost**](SettingsIntegrationsApi.md#merchantSettingsPaymentPost) | **POST** /merchant/settings/payment |  |
| [**merchantSettingsPosterIdGet**](SettingsIntegrationsApi.md#merchantSettingsPosterIdGet) | **GET** /merchant/settings/poster/{id} |  |
| [**merchantSettingsPosterPost**](SettingsIntegrationsApi.md#merchantSettingsPosterPost) | **POST** /merchant/settings/poster |  |
| [**merchantSettingsSmsGetwayIdGet**](SettingsIntegrationsApi.md#merchantSettingsSmsGetwayIdGet) | **GET** /merchant/settings/sms-getway/{id} |  |
| [**merchantSettingsSmsGetwayPost**](SettingsIntegrationsApi.md#merchantSettingsSmsGetwayPost) | **POST** /merchant/settings/sms-getway |  |
| [**merchantSettingsTelephonyIdGet**](SettingsIntegrationsApi.md#merchantSettingsTelephonyIdGet) | **GET** /merchant/settings/telephony/{id} |  |
| [**merchantSettingsTelephonyPost**](SettingsIntegrationsApi.md#merchantSettingsTelephonyPost) | **POST** /merchant/settings/telephony |  |


<a name="merchantSettingsAppIdGet"></a>
# **merchantSettingsAppIdGet**
> AppDto merchantSettingsAppIdGet(id)



Returns a app by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      AppDto result = apiInstance.merchantSettingsAppIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsAppIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

[**AppDto**](AppDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |
| **400** | Bad Request |  -  |

<a name="merchantSettingsAppPost"></a>
# **merchantSettingsAppPost**
> merchantSettingsAppPost(appDto)



Adds a new app

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    AppDto appDto = new AppDto(); // AppDto | A JSON object containing updated app information
    try {
      apiInstance.merchantSettingsAppPost(appDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsAppPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **appDto** | [**AppDto**](AppDto.md)| A JSON object containing updated app information | |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="merchantSettingsBadgeIdDelete"></a>
# **merchantSettingsBadgeIdDelete**
> merchantSettingsBadgeIdDelete(id)



Deletes badge by its id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsBadgeIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsBadgeIdDelete");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **400** | Bad Request |  -  |

<a name="merchantSettingsBadgeIdGet"></a>
# **merchantSettingsBadgeIdGet**
> BadgeDto merchantSettingsBadgeIdGet(id)



Returns a badge by id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      BadgeDto result = apiInstance.merchantSettingsBadgeIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsBadgeIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

[**BadgeDto**](BadgeDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |
| **400** | Bad Request |  -  |

<a name="merchantSettingsBadgePost"></a>
# **merchantSettingsBadgePost**
> Integer merchantSettingsBadgePost(badgeDto)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    BadgeDto badgeDto = new BadgeDto(); // BadgeDto | 
    try {
      Integer result = apiInstance.merchantSettingsBadgePost(badgeDto);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsBadgePost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **badgeDto** | [**BadgeDto**](BadgeDto.md)|  | [optional] |

### Return type

**Integer**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **400** | Bad request |  -  |

<a name="merchantSettingsBadgePut"></a>
# **merchantSettingsBadgePut**
> merchantSettingsBadgePut(badgeDto)



Update badge

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    BadgeDto badgeDto = new BadgeDto(); // BadgeDto | A JSON object containing updated badge's information
    try {
      apiInstance.merchantSettingsBadgePut(badgeDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsBadgePut");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **badgeDto** | [**BadgeDto**](BadgeDto.md)| A JSON object containing updated badge&#39;s information | |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="merchantSettingsBadgesGet"></a>
# **merchantSettingsBadgesGet**
> List&lt;BadgeDto&gt; merchantSettingsBadgesGet()



Returns a list of all badges

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    try {
      List<BadgeDto> result = apiInstance.merchantSettingsBadgesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsBadgesGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;BadgeDto&gt;**](BadgeDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |

<a name="merchantSettingsCashbackGet"></a>
# **merchantSettingsCashbackGet**
> List&lt;CashbackDto&gt; merchantSettingsCashbackGet()



Returns a list of all cashback

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    try {
      List<CashbackDto> result = apiInstance.merchantSettingsCashbackGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsCashbackGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;CashbackDto&gt;**](CashbackDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |

<a name="merchantSettingsCashbackIdDelete"></a>
# **merchantSettingsCashbackIdDelete**
> merchantSettingsCashbackIdDelete(id)



Deletes cashback by its id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsCashbackIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsCashbackIdDelete");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **400** | Bad Request |  -  |

<a name="merchantSettingsCashbackIdGet"></a>
# **merchantSettingsCashbackIdGet**
> CashbackDto merchantSettingsCashbackIdGet(id)



Returns a cashback by id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      CashbackDto result = apiInstance.merchantSettingsCashbackIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsCashbackIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

[**CashbackDto**](CashbackDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |
| **400** | Bad Request |  -  |

<a name="merchantSettingsCashbackPost"></a>
# **merchantSettingsCashbackPost**
> Integer merchantSettingsCashbackPost(cashbackDto)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    CashbackDto cashbackDto = new CashbackDto(); // CashbackDto | 
    try {
      Integer result = apiInstance.merchantSettingsCashbackPost(cashbackDto);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsCashbackPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cashbackDto** | [**CashbackDto**](CashbackDto.md)|  | [optional] |

### Return type

**Integer**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **400** | Bad request |  -  |

<a name="merchantSettingsCashbackPut"></a>
# **merchantSettingsCashbackPut**
> merchantSettingsCashbackPut(cashbackDto)



Update cashback

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    CashbackDto cashbackDto = new CashbackDto(); // CashbackDto | A JSON object containing updated cashback's information
    try {
      apiInstance.merchantSettingsCashbackPut(cashbackDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsCashbackPut");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cashbackDto** | [**CashbackDto**](CashbackDto.md)| A JSON object containing updated cashback&#39;s information | |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="merchantSettingsDeliveryIdGet"></a>
# **merchantSettingsDeliveryIdGet**
> DeliveryDto merchantSettingsDeliveryIdGet(id)



Returns a delivery by merchant ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      DeliveryDto result = apiInstance.merchantSettingsDeliveryIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsDeliveryIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

[**DeliveryDto**](DeliveryDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |
| **400** | Bad Request |  -  |

<a name="merchantSettingsDeliveryPost"></a>
# **merchantSettingsDeliveryPost**
> merchantSettingsDeliveryPost(deliveryDto)



Adds a new delivery

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    DeliveryDto deliveryDto = new DeliveryDto(); // DeliveryDto | A JSON object containing updated delivery information
    try {
      apiInstance.merchantSettingsDeliveryPost(deliveryDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsDeliveryPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **deliveryDto** | [**DeliveryDto**](DeliveryDto.md)| A JSON object containing updated delivery information | |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="merchantSettingsPaymentIdGet"></a>
# **merchantSettingsPaymentIdGet**
> PaymentDto merchantSettingsPaymentIdGet(id)



Returns a payment by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      PaymentDto result = apiInstance.merchantSettingsPaymentIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsPaymentIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

[**PaymentDto**](PaymentDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |
| **400** | Bad Request |  -  |

<a name="merchantSettingsPaymentPost"></a>
# **merchantSettingsPaymentPost**
> merchantSettingsPaymentPost(paymentDto)



Adds a new payment

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    PaymentDto paymentDto = new PaymentDto(); // PaymentDto | A JSON object containing updated payment information
    try {
      apiInstance.merchantSettingsPaymentPost(paymentDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsPaymentPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **paymentDto** | [**PaymentDto**](PaymentDto.md)| A JSON object containing updated payment information | |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="merchantSettingsPosterIdGet"></a>
# **merchantSettingsPosterIdGet**
> PosterDto merchantSettingsPosterIdGet(id)



Returns a poster by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      PosterDto result = apiInstance.merchantSettingsPosterIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsPosterIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

[**PosterDto**](PosterDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |
| **400** | Bad Request |  -  |

<a name="merchantSettingsPosterPost"></a>
# **merchantSettingsPosterPost**
> merchantSettingsPosterPost(posterDto)



Adds a new poster

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    PosterDto posterDto = new PosterDto(); // PosterDto | A JSON object containing updated poster information
    try {
      apiInstance.merchantSettingsPosterPost(posterDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsPosterPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **posterDto** | [**PosterDto**](PosterDto.md)| A JSON object containing updated poster information | |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="merchantSettingsSmsGetwayIdGet"></a>
# **merchantSettingsSmsGetwayIdGet**
> SmsGatewayDto merchantSettingsSmsGetwayIdGet(id)



Returns a sms by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      SmsGatewayDto result = apiInstance.merchantSettingsSmsGetwayIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsSmsGetwayIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

[**SmsGatewayDto**](SmsGatewayDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |
| **400** | Bad Request |  -  |

<a name="merchantSettingsSmsGetwayPost"></a>
# **merchantSettingsSmsGetwayPost**
> merchantSettingsSmsGetwayPost(smsGatewayDto)



Adds a new sms

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    SmsGatewayDto smsGatewayDto = new SmsGatewayDto(); // SmsGatewayDto | A JSON object containing updated sms information
    try {
      apiInstance.merchantSettingsSmsGetwayPost(smsGatewayDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsSmsGetwayPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **smsGatewayDto** | [**SmsGatewayDto**](SmsGatewayDto.md)| A JSON object containing updated sms information | |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="merchantSettingsTelephonyIdGet"></a>
# **merchantSettingsTelephonyIdGet**
> TelephonyDto merchantSettingsTelephonyIdGet(id)



Returns a telephony by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      TelephonyDto result = apiInstance.merchantSettingsTelephonyIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsTelephonyIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

[**TelephonyDto**](TelephonyDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |
| **400** | Bad Request |  -  |

<a name="merchantSettingsTelephonyPost"></a>
# **merchantSettingsTelephonyPost**
> merchantSettingsTelephonyPost(telephonyDto)



Adds a new telephony

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsIntegrationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsIntegrationsApi apiInstance = new SettingsIntegrationsApi(defaultClient);
    TelephonyDto telephonyDto = new TelephonyDto(); // TelephonyDto | A JSON object containing updated telephony information
    try {
      apiInstance.merchantSettingsTelephonyPost(telephonyDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsIntegrationsApi#merchantSettingsTelephonyPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **telephonyDto** | [**TelephonyDto**](TelephonyDto.md)| A JSON object containing updated telephony information | |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

