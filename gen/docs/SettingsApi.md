# SettingsApi

All URIs are relative to *http://0.0.0.0:9000/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**merchantSettingsAppIdGet**](SettingsApi.md#merchantSettingsAppIdGet) | **GET** /merchant/settings/app/{id} | 
[**merchantSettingsAppPost**](SettingsApi.md#merchantSettingsAppPost) | **POST** /merchant/settings/app | 
[**merchantSettingsBadgeIdGet**](SettingsApi.md#merchantSettingsBadgeIdGet) | **GET** /merchant/settings/badge/{id} | 
[**merchantSettingsBadgePost**](SettingsApi.md#merchantSettingsBadgePost) | **POST** /merchant/settings/badge | Create a new B
[**merchantSettingsDeliveryIdGet**](SettingsApi.md#merchantSettingsDeliveryIdGet) | **GET** /merchant/settings/delivery/{id} | 
[**merchantSettingsDeliveryPost**](SettingsApi.md#merchantSettingsDeliveryPost) | **POST** /merchant/settings/delivery | 
[**merchantSettingsPaymentIdGet**](SettingsApi.md#merchantSettingsPaymentIdGet) | **GET** /merchant/settings/payment/{id} | 
[**merchantSettingsPaymentPost**](SettingsApi.md#merchantSettingsPaymentPost) | **POST** /merchant/settings/payment | 
[**merchantSettingsPosterIdGet**](SettingsApi.md#merchantSettingsPosterIdGet) | **GET** /merchant/settings/poster/{id} | 
[**merchantSettingsPosterPost**](SettingsApi.md#merchantSettingsPosterPost) | **POST** /merchant/settings/poster | 
[**merchantSettingsSmsGetwayIdGet**](SettingsApi.md#merchantSettingsSmsGetwayIdGet) | **GET** /merchant/settings/sms-getway/{id} | 
[**merchantSettingsSmsGetwayPost**](SettingsApi.md#merchantSettingsSmsGetwayPost) | **POST** /merchant/settings/sms-getway | 
[**merchantSettingsTelephonyIdGet**](SettingsApi.md#merchantSettingsTelephonyIdGet) | **GET** /merchant/settings/telephony/{id} | 
[**merchantSettingsTelephonyPost**](SettingsApi.md#merchantSettingsTelephonyPost) | **POST** /merchant/settings/telephony | 


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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      AppDto result = apiInstance.merchantSettingsAppIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsAppIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**|  |

### Return type

[**AppDto**](AppDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |
**400** | Bad Request |  -  |

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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    AppDto appDto = new AppDto(); // AppDto | A JSON object containing updated app information
    try {
      apiInstance.merchantSettingsAppPost(appDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsAppPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **appDto** | [**AppDto**](AppDto.md)| A JSON object containing updated app information |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

<a name="merchantSettingsBadgeIdGet"></a>
# **merchantSettingsBadgeIdGet**
> BranchDto merchantSettingsBadgeIdGet(id)



Returns a badge by ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      BranchDto result = apiInstance.merchantSettingsBadgeIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsBadgeIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**|  |

### Return type

[**BranchDto**](BranchDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |
**400** | Bad Request |  -  |

<a name="merchantSettingsBadgePost"></a>
# **merchantSettingsBadgePost**
> Integer merchantSettingsBadgePost(branchDto)

Create a new B

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    BranchDto branchDto = new BranchDto(); // BranchDto | 
    try {
      Integer result = apiInstance.merchantSettingsBadgePost(branchDto);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsBadgePost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **branchDto** | [**BranchDto**](BranchDto.md)|  | [optional]

### Return type

**Integer**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**400** | Bad request |  -  |

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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      DeliveryDto result = apiInstance.merchantSettingsDeliveryIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsDeliveryIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**|  |

### Return type

[**DeliveryDto**](DeliveryDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |
**400** | Bad Request |  -  |

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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    DeliveryDto deliveryDto = new DeliveryDto(); // DeliveryDto | A JSON object containing updated delivery information
    try {
      apiInstance.merchantSettingsDeliveryPost(deliveryDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsDeliveryPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deliveryDto** | [**DeliveryDto**](DeliveryDto.md)| A JSON object containing updated delivery information |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      PaymentDto result = apiInstance.merchantSettingsPaymentIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsPaymentIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**|  |

### Return type

[**PaymentDto**](PaymentDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |
**400** | Bad Request |  -  |

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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    PaymentDto paymentDto = new PaymentDto(); // PaymentDto | A JSON object containing updated payment information
    try {
      apiInstance.merchantSettingsPaymentPost(paymentDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsPaymentPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **paymentDto** | [**PaymentDto**](PaymentDto.md)| A JSON object containing updated payment information |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      PosterDto result = apiInstance.merchantSettingsPosterIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsPosterIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**|  |

### Return type

[**PosterDto**](PosterDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |
**400** | Bad Request |  -  |

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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    PosterDto posterDto = new PosterDto(); // PosterDto | A JSON object containing updated poster information
    try {
      apiInstance.merchantSettingsPosterPost(posterDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsPosterPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **posterDto** | [**PosterDto**](PosterDto.md)| A JSON object containing updated poster information |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      SmsGatewayDto result = apiInstance.merchantSettingsSmsGetwayIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsSmsGetwayIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**|  |

### Return type

[**SmsGatewayDto**](SmsGatewayDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |
**400** | Bad Request |  -  |

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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    SmsGatewayDto smsGatewayDto = new SmsGatewayDto(); // SmsGatewayDto | A JSON object containing updated sms information
    try {
      apiInstance.merchantSettingsSmsGetwayPost(smsGatewayDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsSmsGetwayPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **smsGatewayDto** | [**SmsGatewayDto**](SmsGatewayDto.md)| A JSON object containing updated sms information |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      TelephonyDto result = apiInstance.merchantSettingsTelephonyIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsTelephonyIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**|  |

### Return type

[**TelephonyDto**](TelephonyDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |
**400** | Bad Request |  -  |

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
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    SettingsApi apiInstance = new SettingsApi(defaultClient);
    TelephonyDto telephonyDto = new TelephonyDto(); // TelephonyDto | A JSON object containing updated telephony information
    try {
      apiInstance.merchantSettingsTelephonyPost(telephonyDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsApi#merchantSettingsTelephonyPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **telephonyDto** | [**TelephonyDto**](TelephonyDto.md)| A JSON object containing updated telephony information |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

