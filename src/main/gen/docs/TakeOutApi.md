# TakeOutApi

All URIs are relative to *http://127.0.0.1:9000/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**merchantOrdersGet**](TakeOutApi.md#merchantOrdersGet) | **GET** /merchant/orders |  |
| [**merchantSettingsOrdersAllGet**](TakeOutApi.md#merchantSettingsOrdersAllGet) | **GET** /merchant/settings/orders/all |  |
| [**merchantSettingsOrdersCreatePost**](TakeOutApi.md#merchantSettingsOrdersCreatePost) | **POST** /merchant/settings/orders/create |  |
| [**merchantSettingsOrdersGet**](TakeOutApi.md#merchantSettingsOrdersGet) | **GET** /merchant/settings/orders |  |
| [**merchantSettingsOrdersHistoryGet**](TakeOutApi.md#merchantSettingsOrdersHistoryGet) | **GET** /merchant/settings/orders/history |  |
| [**merchantSettingsOrdersIdDelete**](TakeOutApi.md#merchantSettingsOrdersIdDelete) | **DELETE** /merchant/settings/orders/{id} |  |
| [**merchantSettingsOrdersIdGet**](TakeOutApi.md#merchantSettingsOrdersIdGet) | **GET** /merchant/settings/orders/{id} |  |
| [**merchantSettingsOrdersLiveTypeGet**](TakeOutApi.md#merchantSettingsOrdersLiveTypeGet) | **GET** /merchant/settings/orders/live/{type} |  |
| [**merchantSettingsOrdersPut**](TakeOutApi.md#merchantSettingsOrdersPut) | **PUT** /merchant/settings/orders |  |


<a name="merchantOrdersGet"></a>
# **merchantOrdersGet**
> OrderDto merchantOrdersGet(collectorId)



Returns a order by collectorId

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TakeOutApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    TakeOutApi apiInstance = new TakeOutApi(defaultClient);
    Integer collectorId = 56; // Integer | Filter orders by collectorId.
    try {
      OrderDto result = apiInstance.merchantOrdersGet(collectorId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TakeOutApi#merchantOrdersGet");
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
| **collectorId** | **Integer**| Filter orders by collectorId. | |

### Return type

[**OrderDto**](OrderDto.md)

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

<a name="merchantSettingsOrdersAllGet"></a>
# **merchantSettingsOrdersAllGet**
> List&lt;OrderDto&gt; merchantSettingsOrdersAllGet()



Returns a list of all orders

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TakeOutApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    TakeOutApi apiInstance = new TakeOutApi(defaultClient);
    try {
      List<OrderDto> result = apiInstance.merchantSettingsOrdersAllGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TakeOutApi#merchantSettingsOrdersAllGet");
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

[**List&lt;OrderDto&gt;**](OrderDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="merchantSettingsOrdersCreatePost"></a>
# **merchantSettingsOrdersCreatePost**
> Integer merchantSettingsOrdersCreatePost(orderWrapper)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TakeOutApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    TakeOutApi apiInstance = new TakeOutApi(defaultClient);
    OrderWrapper orderWrapper = new OrderWrapper(); // OrderWrapper | 
    try {
      Integer result = apiInstance.merchantSettingsOrdersCreatePost(orderWrapper);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TakeOutApi#merchantSettingsOrdersCreatePost");
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
| **orderWrapper** | [**OrderWrapper**](OrderWrapper.md)|  | [optional] |

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

<a name="merchantSettingsOrdersGet"></a>
# **merchantSettingsOrdersGet**
> List&lt;OrderDto&gt; merchantSettingsOrdersGet()



Returns a list of all orders

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TakeOutApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    TakeOutApi apiInstance = new TakeOutApi(defaultClient);
    try {
      List<OrderDto> result = apiInstance.merchantSettingsOrdersGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TakeOutApi#merchantSettingsOrdersGet");
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

[**List&lt;OrderDto&gt;**](OrderDto.md)

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

<a name="merchantSettingsOrdersHistoryGet"></a>
# **merchantSettingsOrdersHistoryGet**
> List&lt;OrderDto&gt; merchantSettingsOrdersHistoryGet()



Returns a list of all orders history

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TakeOutApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    TakeOutApi apiInstance = new TakeOutApi(defaultClient);
    try {
      List<OrderDto> result = apiInstance.merchantSettingsOrdersHistoryGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TakeOutApi#merchantSettingsOrdersHistoryGet");
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

[**List&lt;OrderDto&gt;**](OrderDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="merchantSettingsOrdersIdDelete"></a>
# **merchantSettingsOrdersIdDelete**
> merchantSettingsOrdersIdDelete(id)



Deletes order by its id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TakeOutApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    TakeOutApi apiInstance = new TakeOutApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsOrdersIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling TakeOutApi#merchantSettingsOrdersIdDelete");
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

<a name="merchantSettingsOrdersIdGet"></a>
# **merchantSettingsOrdersIdGet**
> OrderDto merchantSettingsOrdersIdGet(id)



Returns a order by id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TakeOutApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    TakeOutApi apiInstance = new TakeOutApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      OrderDto result = apiInstance.merchantSettingsOrdersIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TakeOutApi#merchantSettingsOrdersIdGet");
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

[**OrderDto**](OrderDto.md)

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

<a name="merchantSettingsOrdersLiveTypeGet"></a>
# **merchantSettingsOrdersLiveTypeGet**
> OrderDto merchantSettingsOrdersLiveTypeGet(type)



Returns a order by type

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TakeOutApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    TakeOutApi apiInstance = new TakeOutApi(defaultClient);
    String type = "type_example"; // String | 
    try {
      OrderDto result = apiInstance.merchantSettingsOrdersLiveTypeGet(type);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TakeOutApi#merchantSettingsOrdersLiveTypeGet");
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
| **type** | **String**|  | |

### Return type

[**OrderDto**](OrderDto.md)

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

<a name="merchantSettingsOrdersPut"></a>
# **merchantSettingsOrdersPut**
> merchantSettingsOrdersPut(orderDto)



Update order

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TakeOutApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    TakeOutApi apiInstance = new TakeOutApi(defaultClient);
    OrderDto orderDto = new OrderDto(); // OrderDto | A JSON object containing updated order's information
    try {
      apiInstance.merchantSettingsOrdersPut(orderDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling TakeOutApi#merchantSettingsOrdersPut");
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
| **orderDto** | [**OrderDto**](OrderDto.md)| A JSON object containing updated order&#39;s information | |

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

